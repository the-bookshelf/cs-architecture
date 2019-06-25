/*
 *
 * Embedded System Architecture
 * Copyright (c) 2018 Packt
 *
 * Author: Daniele Lacamera <root@danielinux.net>
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
#include <stdlib.h>
#include <stdint.h>
#include "system.h"
#include "systick.h"
#include "timer.h"
#include "led.h"
#include "button.h"
#include "locks.h"
#include "mpu.h"

mutex m;


#define TASK_WAITING 0
#define TASK_READY   1
#define TASK_RUNNING 2
#define TASK_NAME_MAXLEN 16
struct task_block {
    char name[TASK_NAME_MAXLEN];
    int id;
	int state;
	void (*start)(void *arg);
	void *arg;
    uint8_t *sp;
    uint32_t wakeup_time;
    uint8_t priority;
    struct task_block *next;
};

#define MAX_TASKS 16
#define MAX_PRIO  4
static struct task_block TASKS[MAX_TASKS];
#define kernel TASKS[0]
static int n_tasks = 1;
static struct task_block *t_cur = &TASKS[0];
extern uint32_t stack_space;
#define STACK_SIZE (256)


/* System calls */
#define SYS_SCHEDULE 0
#define SYS_BUTTON_READ 1
#define SYS_BLUELED_ON 2
#define SYS_BLUELED_OFF 3
#define SYS_BLUELED_TOGGLE 4
#define SYS_REDLED_ON 5
#define SYS_REDLED_OFF 6
#define SYS_REDLED_TOGGLE 7
#define SYS_GREENLED_ON 8
#define SYS_GREENLED_OFF 9
#define SYS_GREENLED_TOGGLE 10

#define SCB_ICSR (*((volatile uint32_t *)0xE000ED04))
#define schedule()  SCB_ICSR |= (1 << 28)

#define TIMESLICE (20)

struct task_block *tasklist_active[MAX_PRIO] = { };
struct task_block *tasklist_waiting = NULL;

static void tasklist_add(struct task_block **list, struct task_block *el)
{
    el->next = *list;
    *list = el;
}

static void tasklist_add_active(struct task_block *el)
{
    tasklist_add(&tasklist_active[el->priority], el);
}

static int tasklist_del(struct task_block **list, struct task_block *delme)
{
    struct task_block *t = *list;
    struct task_block *p = NULL;
    while (t) {
        if (t == delme) {
            if (p == NULL)
                *list = t->next;
            else
                p->next = t->next;
            return 0;
        }
        p = t;
        t = t->next;
    }
    return -1;
}

static int tasklist_del_active(struct task_block *el)
{
    return tasklist_del(&tasklist_active[el->priority], el);
}


static int idx;
static inline struct task_block *tasklist_next_ready(struct task_block *t)
{
    for (idx = MAX_PRIO - 1; idx >= 0; idx--) {
        if ((idx == t->priority) && 
                (t->next != NULL) && 
                (t->next->state == TASK_READY))
            return t->next;
        if (tasklist_active[idx])
            return tasklist_active[idx];
    }
    return t;
}

static void task_waiting(struct task_block *t)
{
    if (tasklist_del_active(t) == 0) {
        tasklist_add(&tasklist_waiting, t);
        t->state = TASK_WAITING;
    }
}

static void task_ready(struct task_block *t)
{
    if (tasklist_del(&tasklist_waiting, t) == 0) {
        tasklist_add_active(t);
        t->state = TASK_READY;
    }
}

static int syscall(int arg0)
{
    SVC();
    return 0;
}


/* Mutex/semaphore */
int sem_wait(semaphore *s)
{
    int i;
    if (s == NULL)
        return -1;
    if (sem_trywait(s) == 0)
        return 0;
    for (i = 0; i < MAX_LISTENERS; i++) {
        if (!s->listeners[i])
            s->listeners[i] = t_cur->id;
        if (s->listeners[i] == t_cur->id)
            break;
    }
    task_waiting(t_cur);
    syscall(SYS_SCHEDULE);
    return sem_wait(s);
}

int sem_post(semaphore *s)
{
    int i;
    if (s == NULL)
        return -1;
    if (sem_dopost(s) > 0) {
        for (i = 0; i < MAX_LISTENERS; i++) {
            if (s->listeners[i]) {
                task_ready(&TASKS[s->listeners[i]]);
                s->listeners[i] = 0;
            }
        }
        syscall(SYS_SCHEDULE);
    }
    return 0;
}

#define mutex_lock(x) sem_wait(x)
#define mutex_unlock(x) sem_post(x)

void isr_systick(void)
{
    if ((++jiffies % TIMESLICE) == 0)
        schedule();
}

struct stack_frame {
    uint32_t r0, r1, r2, r3, r12, lr, pc, xpsr;
};

struct extra_frame {
    uint32_t r4, r5, r6, r7, r8, r9, r10, r11;
};

void task_terminated(void)
{
    while(1) ;;
}

static void task_stack_init(struct task_block *t)
{
    struct stack_frame *tf;
    t->sp -= sizeof(struct stack_frame);
    tf = (struct stack_frame *)(t->sp);
    tf->r0 = (uint32_t) t->arg;
    tf->pc = (uint32_t) t->start;
    tf->lr = (uint32_t) task_terminated;
    tf->xpsr =  (1 << 24);
    t->sp -= sizeof(struct extra_frame);
}


struct task_block *task_create(char *name, void (*start)(void *arg), void *arg, int prio)
{
    struct task_block *t;
    int i;

    if (n_tasks >= MAX_TASKS)
        return NULL;
    t = &TASKS[n_tasks];
    t->id = n_tasks++;
    for (i = 0; i < TASK_NAME_MAXLEN; i++) {
        t->name[i] = name[i];
        if (name[i] == 0)
            break;
    }
    t->state = TASK_READY;
    t->start = start;
    t->arg = arg;
    t->wakeup_time = 0;
    t->priority = prio;
    t->sp = (uint8_t *)((&stack_space) + n_tasks * STACK_SIZE); 
    task_stack_init(t);
    tasklist_add_active(t);
    return t;
}

void sleep_ms(int ms)
{
    if (ms < 2)
        return;
    t_cur->wakeup_time = jiffies + ms;
    task_waiting(t_cur);
    syscall(SYS_SCHEDULE);
}

struct task_block *button_task = NULL;
int button_read(void)
{
    if (button_task)
        return 0;
    button_task = t_cur;
    task_waiting(t_cur);
    syscall(SYS_BUTTON_READ);
    return 1;
}

void button_wakeup(void)
{
    if (button_task) {
        task_ready(button_task);
        button_task = NULL;
        schedule();
    }
}

void task_test0(void *arg)
{
    while(1) {
        syscall(SYS_BLUELED_ON);
        mutex_lock(&m);
        sleep_ms(500);
        syscall(SYS_BLUELED_OFF);
        mutex_unlock(&m);
        sleep_ms(1000);
    }
}

void task_test1(void *arg)
{
    syscall(SYS_REDLED_ON);
    while(1) {
        sleep_ms(50);
        mutex_lock(&m);
        syscall(SYS_REDLED_TOGGLE);
        mutex_unlock(&m);
    }
}

void task_test2(void *arg)
{
    uint32_t toggle_time = 0;
    syscall(SYS_GREENLED_OFF);
    while(1) {
        button_read();
        if ((jiffies - toggle_time) > 120) {
            syscall(SYS_GREENLED_TOGGLE);
            toggle_time = jiffies;
        }
    }
}

static void __attribute__((naked)) store_kernel_context(void)
{
    asm volatile("mrs r0, msp");
    asm volatile("stmdb r0!, {r4-r11}");
    asm volatile("msr msp, r0");
    asm volatile("bx lr");
}

static void __attribute__((naked)) restore_kernel_context(void)
{
    asm volatile("mrs r0, msp");
    asm volatile("ldmfd r0!, {r4-r11}");
    asm volatile("msr msp, r0");
    asm volatile("bx lr");
}

static void __attribute__((naked)) store_user_context(void)
{
    asm volatile("mrs r0, psp");
    asm volatile("stmdb r0!, {r4-r11}");
    asm volatile("msr psp, r0");
    asm volatile("bx lr");
}

static void __attribute__((naked)) restore_user_context(void)
{
    asm volatile("mrs r0, psp");
    asm volatile("ldmfd r0!, {r4-r11}");
    asm volatile("msr psp, r0");
    asm volatile("bx lr");
}


void __attribute__((naked)) isr_pendsv(void)
{
    if (t_cur->id == 0) {
        store_kernel_context();
        asm volatile("mrs %0, msp" : "=r"(t_cur->sp));
    } else {
        store_user_context();
        asm volatile("mrs %0, psp" : "=r"(t_cur->sp));
    }
    if (t_cur->state == TASK_RUNNING) {
        t_cur->state = TASK_READY;
    }
    t_cur = tasklist_next_ready(t_cur);
    t_cur->state = TASK_RUNNING;
    if (t_cur->id == 0) {
        asm volatile("msr msp, %0" ::"r"(t_cur->sp));
        restore_kernel_context();
        asm volatile("mov lr, %0" ::"r"(0xFFFFFFF9));
        asm volatile("msr CONTROL, %0" ::"r"(0x00));
    } else {
        asm volatile("msr psp, %0" ::"r"(t_cur->sp));
        restore_user_context();
        mpu_task_stack_permit(((uint8_t *)((&stack_space)) + (t_cur->id << 10)));
        asm volatile("mov lr, %0" ::"r"(0xFFFFFFFD));
        asm volatile("msr CONTROL, %0" ::"r"(0x01));
    }
    asm volatile("bx lr");
}

void __attribute__((naked)) isr_svc(int arg)
{
    store_user_context();
    asm volatile("mrs %0, psp" : "=r"(t_cur->sp));
    if (t_cur->state == TASK_RUNNING) {
        t_cur->state = TASK_READY;
    }
    switch(arg) {
        case SYS_BUTTON_READ:
            button_start_read();
            /* fall through */
        case SYS_SCHEDULE:
            t_cur = tasklist_next_ready(t_cur);
            t_cur->state = TASK_RUNNING;
            break;
        case SYS_BLUELED_ON:
            blue_led_on();
            break;
        case SYS_BLUELED_OFF:
            blue_led_off();
            break;
        case SYS_BLUELED_TOGGLE:
            blue_led_toggle();
            break;
        case SYS_GREENLED_ON:
            green_led_on();
            break;
        case SYS_GREENLED_OFF:
            green_led_off();
            break;
        case SYS_GREENLED_TOGGLE:
            green_led_toggle();
            break;
        case SYS_REDLED_ON:
            red_led_on();
            break;
        case SYS_REDLED_OFF:
            red_led_off();
            break;
        case SYS_REDLED_TOGGLE:
            red_led_toggle();
            break;
    }
    if (t_cur->id == 0) {
        asm volatile("msr msp, %0" ::"r"(t_cur->sp));
        restore_kernel_context();
        asm volatile("mov lr, %0" ::"r"(0xFFFFFFF9));
        asm volatile("msr CONTROL, %0" ::"r"(0x00));
    } else {
        asm volatile("msr psp, %0" ::"r"(t_cur->sp));
        restore_user_context();
        mpu_task_stack_permit(((uint8_t *)((&stack_space)) + (t_cur->id << 10)));
        asm volatile("mov lr, %0" ::"r"(0xFFFFFFFD));
        asm volatile("msr CONTROL, %0" ::"r"(0x01));
    }
    asm volatile("bx lr");
    
}


void main(void) {
    clock_pll_on(0);
    led_setup();
    mpu_enable();
    button_setup(button_wakeup);
    systick_enable();
    kernel.name[0] = 0;
    kernel.id = 0;
    kernel.state = TASK_RUNNING;
    kernel.wakeup_time = 0;
    kernel.priority = 0;
    tasklist_add_active(&kernel);
    task_create("test0",task_test0, NULL, 1);
    task_create("test1",task_test1, NULL, 1);
    task_create("test2",task_test2, NULL, 3);
    mutex_init(&m);

    while(jiffies < 20)
        WFI();


    while(1) {
        struct task_block *t = tasklist_waiting;
        while (t) {
            if (t->wakeup_time && (t->wakeup_time < jiffies)) {
                t->wakeup_time = 0;
                task_ready(t);
                break;
            }
            t = t->next;
        }
        WFI();
    }
}

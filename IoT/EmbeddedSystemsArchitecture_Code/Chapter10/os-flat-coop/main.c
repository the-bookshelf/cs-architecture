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
#include "timer.h"
#include "led.h"
#include "button.h"

volatile int timer_elapsed = 0;
volatile uint32_t tim2_ticks = 0;
volatile uint32_t cpu_freq = 168000000;
volatile int powersave = 1;

#define TASK_WAITING 0
#define TASK_RUNNING 1
#define TASK_NAME_MAXLEN 16
struct task_block {
    char name[TASK_NAME_MAXLEN];
    int id;
	int state;
	void (*start)(void *arg);
	void *arg;
    uint8_t *sp;
};

#define MAX_TASKS 16
static struct task_block TASKS[MAX_TASKS];
#define kernel TASKS[0]
static int n_tasks = 1;
static int running_task_id = 0;

extern uint32_t stack_space;
#define STACK_SIZE (256)



/*** SYSTICK ***/

#define SYSTICK_BASE (0xE000E010)
#define SYSTICK_CSR     (*(volatile uint32_t *)(SYSTICK_BASE + 0x00))
#define SYSTICK_RVR     (*(volatile uint32_t *)(SYSTICK_BASE + 0x04))
#define SYSTICK_CVR     (*(volatile uint32_t *)(SYSTICK_BASE + 0x08))
#define SYSTICK_CALIB   (*(volatile uint32_t *)(SYSTICK_BASE + 0x0C))

static volatile unsigned int jiffies = 0;


void systick_enable(void)
{
    SYSTICK_RVR = ((cpu_freq / 1000) - 1);
    SYSTICK_CVR = 0;
    SYSTICK_CSR |= 0x07;
}


void isr_systick(void)
{
    ++jiffies;
}
#define SCB_ICSR (*((volatile uint32_t *)0xE000ED04))
#define schedule()  SCB_ICSR |= (1 << 28)

struct stack_frame {
    uint32_t r0, r1, r2, r3, r12, lr, pc, xpsr;
};

struct extra_frame {
    uint32_t r4, r5, r6, r7, r8, r9, r10, r11;
};

void task_terminated(void)
{
    while(1) 
        ;

}


static void task_stack_init(struct task_block *t)
{
    struct stack_frame *tf;
    t->sp -= sizeof(struct stack_frame);
    tf = (struct stack_frame *)(t->sp);
    tf->r0 = (uint32_t) t->arg;
    tf->pc = (uint32_t) t->start;
    tf->lr = (uint32_t) task_terminated;
    tf->xpsr =  0x01000000;
    t->sp -= sizeof(struct extra_frame);
}


struct task_block *task_create(char *name, void (*start)(void *arg), void *arg)
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
    t->state = TASK_WAITING;
    t->start = start;
    t->arg = arg;
    t->sp = (uint8_t *)((&stack_space) + n_tasks * STACK_SIZE); 
    task_stack_init(t);
    return t;
}

void task_test0(void *arg)
{
    uint32_t now = jiffies;
    blue_led_on();
    while(1) {
        if ((jiffies - now) > 1000) {
            blue_led_off();
            schedule();
            now = jiffies;
            blue_led_on();
        }
    }
}

void task_test1(void *arg)
{
    uint32_t now = jiffies;
    red_led_on();
    while(1) {
        if ((jiffies - now) > 1000) {
            red_led_off();
            schedule();
            now = jiffies;
            red_led_on();
        }
    }
}

static void __attribute__((naked)) store_context(void)
{
    asm volatile("mrs r0, msp");
    asm volatile("stmdb r0!, {r4-r11}");
    asm volatile("msr msp, r0");
    asm volatile("bx lr");
}

static void __attribute__((naked)) restore_context(void)
{
    asm volatile("mrs r0, msp");
    asm volatile("ldmfd r0!, {r4-r11}");
    asm volatile("msr msp, r0");
    asm volatile("bx lr");
}

void __attribute__((naked)) isr_pendsv(void)
{
    store_context();
    asm volatile("mrs %0, msp" : "=r"(TASKS[running_task_id].sp));
    TASKS[running_task_id].state = TASK_WAITING;
    running_task_id++;
    if (running_task_id >= n_tasks)
        running_task_id = 0;
    TASKS[running_task_id].state = TASK_RUNNING;
    asm volatile("msr msp, %0" ::"r"(TASKS[running_task_id].sp));
    restore_context();
    asm volatile("mov lr, %0" ::"r"(0xFFFFFFF9));
    asm volatile("bx lr");

}

void main(void) {
    clock_pll_on(0);
    systick_enable();
    led_setup();
    kernel.name[0] = 0;
    kernel.id = 0;
    kernel.state = TASK_RUNNING;
    task_create("test0",task_test0, NULL);
    task_create("test1",task_test1, NULL);

    while(1) {
        schedule();
    }
}


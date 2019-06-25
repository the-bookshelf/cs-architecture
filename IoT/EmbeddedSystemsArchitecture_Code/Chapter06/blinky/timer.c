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
#include <stdint.h>
#include "system.h"
#include "led.h"


/* STM32 specific defines */
#define APB1_CLOCK_ER           (*(volatile uint32_t *)(0x40023840))
#define APB1_CLOCK_RST          (*(volatile uint32_t *)(0x40023820))
#define TIM2_APB1_CLOCK_ER_VAL 	(1 << 0)

#define TIM2_BASE (0x40000000)
#define TIM2_CR1  (*(volatile uint32_t *)(TIM2_BASE + 0x00))
#define TIM2_DIER (*(volatile uint32_t *)(TIM2_BASE + 0x0c))
#define TIM2_SR   (*(volatile uint32_t *)(TIM2_BASE + 0x10))
#define TIM2_PSC  (*(volatile uint32_t *)(TIM2_BASE + 0x28))
#define TIM2_ARR  (*(volatile uint32_t *)(TIM2_BASE + 0x2c))

#define TIM_DIER_UIE (1 << 0)
#define TIM_SR_UIF   (1 << 0)
#define TIM_CR1_CLOCK_ENABLE (1 << 0)
#define TIM_CR1_UPD_RS       (1 << 2)


int timer_init(uint32_t clock, uint32_t prescaler, uint32_t interval_ms)
{
    uint32_t val = 0;
    uint32_t psc = 1;
    uint32_t err = 0;
    clock = ((clock * prescaler) / 1000) * interval_ms;

    while (psc < 65535) {
        val = clock / psc;
        err = clock % psc;
        if ((val < 65535) && (err == 0)) {
            val--;
            break;
        }
        val = 0;
        psc++;
    }
    if (val == 0)
        return -1;

    nvic_irq_enable(NVIC_TIM2_IRQN);
    nvic_irq_setprio(NVIC_TIM2_IRQN, 0);
    APB1_CLOCK_RST |= TIM2_APB1_CLOCK_ER_VAL;
    __asm__ volatile ("dmb");
    APB1_CLOCK_RST &= ~TIM2_APB1_CLOCK_ER_VAL;
    APB1_CLOCK_ER |= TIM2_APB1_CLOCK_ER_VAL;

    TIM2_CR1    = 0;
    __asm__ volatile ("dmb");
    TIM2_PSC    = psc;
    TIM2_ARR    = val;
    TIM2_CR1    |= TIM_CR1_CLOCK_ENABLE;
    TIM2_DIER   |= TIM_DIER_UIE;
    __asm__ volatile ("dmb");
    return 0;
}

volatile uint32_t tim2_ticks = 0;

void isr_tim2(void) {
    TIM2_SR &= ~TIM_SR_UIF;
    tim2_ticks++;
    led_toggle();
}

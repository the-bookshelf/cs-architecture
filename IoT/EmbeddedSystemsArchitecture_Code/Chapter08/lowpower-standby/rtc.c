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



#define RTC_BASE (0x40002800)
#define RTC_CR  (*(volatile uint32_t *)(RTC_BASE + 0x08))
#define RTC_ISR (*(volatile uint32_t *)(RTC_BASE + 0x0c))
#define RTC_WUTR  (*(volatile uint32_t *)(RTC_BASE + 0x14))
#define RTC_WPR  (*(volatile uint32_t *)(RTC_BASE + 0x24))

#define RTC_CR_WUP (0x03 << 21)
#define RTC_CR_WUTIE (1 << 14)
#define RTC_CR_WUTE  (1 << 10)

#define RTC_ISR_WUTF  (1 << 10)
#define RTC_ISR_WUTWF (1 << 2)


static void rtc_unlock(void)
{
    RTC_WPR = 0xca;
    RTC_WPR = 0x53;
}

static void rtc_lock(void)
{
    RTC_WPR = 0xb0;
}

void rtc_start(void)
{
    rtc_unlock();
    RTC_CR |= RTC_CR_WUTIE |RTC_CR_WUTE;
    while (((RTC_ISR) & (RTC_ISR_WUTWF)))
        ;
    rtc_lock();
}

void rtc_init(void)
{

    /* Enable Power controller */
    APB1_CLOCK_ER |= PWR_APB1_CLOCK_ER_VAL;
    POW_CR |= POW_CR_DPB;
    RCC_BACKUP |= RCC_BACKUP_RTCEN;
    RCC_CSR |= RCC_CSR_LSION;
    while (!(RCC_CSR & RCC_CSR_LSIRDY))
        ;
    RCC_BACKUP |= (RCC_BACKUP_RTCSEL_LSI << RCC_BACKUP_RTCSEL_SHIFT);
    
    EXTI_IMR |= (1 << 22);
    EXTI_EMR |= (1 << 22);
    EXTI_RTSR |= (1 << 22);

    rtc_unlock();
    RTC_CR &= ~RTC_CR_WUTE;
    DMB();
    while (!((RTC_ISR) & (RTC_ISR_WUTWF)))
        ;
    RTC_WUTR = (2048 * 4) - 1;
    RTC_CR |= RTC_CR_WUP;
    RTC_ISR &= ~RTC_ISR_WUTF;
    rtc_lock();
}

void isr_rtc(void)
{
    rtc_unlock();
    nvic_irq_clear(NVIC_RTC_IRQ);
    nvic_irq_disable(NVIC_RTC_IRQ);
    RTC_ISR &= ~RTC_ISR_WUTF;
    RTC_CR &= ~(RTC_CR_WUTIE | RTC_CR_WUTE);
    rtc_lock();
}

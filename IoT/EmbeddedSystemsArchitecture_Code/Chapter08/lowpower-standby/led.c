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

#define AHB1_CLOCK_ER (*(volatile uint32_t *)(0x40023830))
#define GPIOD_AHB1_CLOCK_ER (1 << 3)

#define GPIOD_BASE 0x40020c00
#define GPIOD_MODE (*(volatile uint32_t *)(GPIOD_BASE + 0x00))
#define GPIOD_OTYPE (*(volatile uint32_t *)(GPIOD_BASE + 0x04))
#define GPIOD_OTYPE (*(volatile uint32_t *)(GPIOD_BASE + 0x04))
#define GPIOD_PUPD (*(volatile uint32_t *)(GPIOD_BASE + 0x0c))
#define GPIOD_ODR  (*(volatile uint32_t *)(GPIOD_BASE + 0x14))
#define GPIOD_BSRR (*(volatile uint32_t *)(GPIOD_BASE + 0x18))
#define LED_PIN (15)


void led_setup(void)
{
    uint32_t reg;
    AHB1_CLOCK_ER |= GPIOD_AHB1_CLOCK_ER;
    reg = GPIOD_MODE & ~ (0x03 << (LED_PIN * 2));
    GPIOD_MODE = reg | (1 << (LED_PIN * 2));

    reg = GPIOD_PUPD & (0x03 <<  (LED_PIN * 2));
    GPIOD_PUPD = reg | (0x02 << (LED_PIN * 2));
}

void led_on(void)
{
    GPIOD_BSRR |= (1 << LED_PIN);
}

void led_off(void)
{
    GPIOD_BSRR |= (1 << (LED_PIN + 16));
}

void led_toggle(void)
{
    if ((GPIOD_ODR & (1 << LED_PIN)) == (1 << LED_PIN))
        led_off();
    else
        led_on();
}

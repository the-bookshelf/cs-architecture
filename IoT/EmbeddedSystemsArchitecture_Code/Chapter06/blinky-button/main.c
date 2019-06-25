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


void main(void) {
    flash_set_waitstates();
    clock_config();
    button_setup();
    led_setup();
    timer_init(CPU_FREQ, 1, 1000);
    while(1)
        WFI();
}

#define TIM2_BASE (0x40000000)
#define TIM2_SR   (*(volatile uint32_t *)(TIM2_BASE + 0x10))
#define TIM_SR_UIF   (1 << 0)
volatile uint32_t tim2_ticks = 0;


/* Reading button state periodically through timer TIM2 */
void isr_tim2(void) {
    TIM2_SR &= ~TIM_SR_UIF;
    tim2_ticks++;
    if (!button_is_pressed())
        led_toggle();
}

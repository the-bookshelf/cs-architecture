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
#include "uart.h"

#define UART3 (0x40004800)

#define UART3_SR       (*(volatile uint32_t *)(UART3))
#define UART3_DR       (*(volatile uint32_t *)(UART3 + 0x04))
#define UART3_BRR      (*(volatile uint32_t *)(UART3 + 0x08))
#define UART3_CR1      (*(volatile uint32_t *)(UART3 + 0x0c))
#define UART3_CR2      (*(volatile uint32_t *)(UART3 + 0x10))

#define UART_CR1_UART_ENABLE    (1 << 13)
#define UART_CR1_SYMBOL_LEN     (1 << 12)
#define UART_CR1_PARITY_ENABLED (1 << 10)
#define UART_CR1_PARITY_ODD     (1 << 9)
#define UART_CR1_TX_ENABLE      (1 << 3)
#define UART_CR1_RX_ENABLE      (1 << 2)
#define UART_CR2_STOPBITS       (3 << 12)
#define UART_SR_TX_EMPTY        (1 << 7)
#define UART_SR_RX_NOTEMPTY     (1 << 5)


#define CLOCK_SPEED (168000000)

#define APB1_CLOCK_ER           (*(volatile uint32_t *)(0x40023840))
#define UART3_APB1_CLOCK_ER_VAL 	(1 << 18)

#define AHB1_CLOCK_ER (*(volatile uint32_t *)(0x40023830))
#define GPIOD_AHB1_CLOCK_ER (1 << 3)
#define GPIOD_BASE 0x40020c00
#define GPIOD_MODE  (*(volatile uint32_t *)(GPIOD_BASE + 0x00))
#define GPIOD_AFL   (*(volatile uint32_t *)(GPIOD_BASE + 0x20))
#define GPIOD_AFH   (*(volatile uint32_t *)(GPIOD_BASE + 0x24))
#define GPIO_MODE_AF (2)
#define UART3_PIN_AF 7
#define UART3_RX_PIN 9
#define UART3_TX_PIN 8

static void uart3_pins_setup(void)
{
    uint32_t reg;
    AHB1_CLOCK_ER |= GPIOD_AHB1_CLOCK_ER;
    /* Set mode = AF */
    reg = GPIOD_MODE & ~ (0x03 << (UART3_RX_PIN * 2));
    GPIOD_MODE = reg | (2 << (UART3_RX_PIN * 2));
    reg = GPIOD_MODE & ~ (0x03 << (UART3_TX_PIN * 2));
    GPIOD_MODE = reg | (2 << (UART3_TX_PIN * 2));

    /* Alternate function: use high pins (8 and 9) */
    reg = GPIOD_AFH & ~(0xf << ((UART3_TX_PIN - 8) * 4));
    GPIOD_AFH = reg | (UART3_PIN_AF << ((UART3_TX_PIN - 8) * 4));
    reg = GPIOD_AFH & ~(0xf << ((UART3_RX_PIN - 8) * 4));
    GPIOD_AFH = reg | (UART3_PIN_AF << ((UART3_RX_PIN - 8) * 4));
}

int uart3_setup(uint32_t bitrate, uint8_t data, char parity, uint8_t stop)
{
    uint32_t reg;
    int pin_rx, pin_tx, pin_af;
    /* Enable pins and configure for AF7 */
    uart3_pins_setup();
    /* Turn on the device */
    APB1_CLOCK_ER |= UART3_APB1_CLOCK_ER_VAL;

    /* Configure for TX */
    UART3_CR1 |= UART_CR1_TX_ENABLE;

    /* Configure clock */
    UART3_BRR =  CLOCK_SPEED / bitrate;

    /* Configure data bits */
    if (data == 8)
        UART3_CR1 &= ~UART_CR1_SYMBOL_LEN;
    else
        UART3_CR1 |= UART_CR1_SYMBOL_LEN;

    /* Configure parity */
    switch (parity) {
        case 'O':
            UART3_CR1 |= UART_CR1_PARITY_ODD;
            /* fall through to enable parity */
        case 'E':
            UART3_CR1 |= UART_CR1_PARITY_ENABLED;
            break;
        default:
            UART3_CR1 &= ~(UART_CR1_PARITY_ENABLED | UART_CR1_PARITY_ODD);
    }
    /* Set stop bits */
    reg = UART3_CR2 & ~UART_CR2_STOPBITS;
    if (stop > 1)
        UART3_CR2 = reg & (2 << 12);
    else
        UART3_CR2 = reg;

    /* Turn on uart */
    UART3_CR1 |= UART_CR1_UART_ENABLE;

    return 0;
}

int _write(void *r, uint8_t *text, int len)
{
    char *p = (char *)text;
    int i;
    volatile uint32_t reg;
    text[len - 1] = 0;
    while(*p) {
        do {
            reg = UART3_SR;
        } while ((reg & UART_SR_TX_EMPTY) == 0);
        UART3_DR = *p;
        p++;
    }
    return len;
}


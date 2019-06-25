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
#include "spi.h"

#define SPI1 (0x40013000)

#define SPI1_CR1      (*(volatile uint32_t *)(SPI1))
#define SPI1_CR2      (*(volatile uint32_t *)(SPI1 + 0x04))
#define SPI1_SR       (*(volatile uint32_t *)(SPI1 + 0x08))
#define SPI1_DR       (*(volatile uint32_t *)(SPI1 + 0x0c))

#define SPI_CR1_CLOCK_PHASE         (1 << 0)
#define SPI_CR1_CLOCK_POLARITY      (1 << 1)
#define SPI_CR1_MASTER	    		(1 << 2)
#define SPI_CR1_BAUDRATE        	(0x07 << 3)
#define SPI_CR1_SPI_EN		    	(1 << 6)
#define SPI_CR1_LSBFIRST		    (1 << 7)
#define SPI_CR1_SSI			(1 << 8)
#define SPI_CR1_SSM			(1 << 9)
#define SPI_CR1_16BIT_FORMAT        (1 << 11)
#define SPI_CR1_TX_CRC_NEXT			(1 << 12)
#define SPI_CR1_HW_CRC_EN			(1 << 13)
#define SPI_CR1_BIDIOE			    (1 << 14)
#define SPI_CR2_SSOE			    (1 << 2)


#define SPI_SR_RX_NOTEMPTY  	        (1 << 0)
#define SPI_SR_TX_EMPTY			        (1 << 1)
#define SPI_SR_BUSY			            (1 << 7)

#define APB2_CLOCK_ER           (*(volatile uint32_t *)(0x40023844))
#define APB2_CLOCK_RST          (*(volatile uint32_t *)(0x40023824))
#define SPI1_APB2_CLOCK_ER_VAL 	(1 << 12)


#define CLOCK_SPEED (168000000) 


#define AHB1_CLOCK_ER (*(volatile uint32_t *)(0x40023830))
#define GPIOA_AHB1_CLOCK_ER (1 << 0)
#define GPIOE_AHB1_CLOCK_ER (1 << 4)
#define GPIOA_BASE 0x40020000
#define GPIOA_MODE  (*(volatile uint32_t *)(GPIOA_BASE + 0x00))
#define GPIOA_AFL   (*(volatile uint32_t *)(GPIOA_BASE + 0x20))
#define GPIOA_AFH   (*(volatile uint32_t *)(GPIOA_BASE + 0x24))
#define GPIO_MODE_AF (2)

#define SPI1_PIN_AF 5
#define SPI1_CLOCK_PIN 5
#define SPI1_MOSI_PIN 6
#define SPI1_MISO_PIN 7

#define SLAVE_PIN  3
#define GPIOE_BASE 0x40021000
#define GPIOE_MODE  (*(volatile uint32_t *)(GPIOE_BASE + 0x00))
#define GPIOE_OSPD  (*(volatile uint32_t *)(GPIOE_BASE + 0x08))
#define GPIOE_PUPD (*(volatile uint32_t *)(GPIOE_BASE + 0x0c))
#define GPIOE_BSRR (*(volatile uint32_t *)(GPIOE_BASE + 0x18))
#define GPIO_MODE_AF (2)

void slave_off(void)
{
    GPIOE_BSRR |= (1 << SLAVE_PIN);
}

void slave_on(void)
{
    GPIOE_BSRR |= (1 << (SLAVE_PIN + 16));
}


static void slave_pin_setup(void)
{
    uint32_t reg;
    AHB1_CLOCK_ER |= GPIOE_AHB1_CLOCK_ER;
    reg = GPIOE_MODE & ~ (0x03 << (SLAVE_PIN * 2));
    GPIOE_MODE = reg | (1 << (SLAVE_PIN * 2));

    reg = GPIOE_PUPD & (0x03 <<  (SLAVE_PIN * 2));
    GPIOE_PUPD = reg | (0x01 << (SLAVE_PIN * 2));

    reg = GPIOE_OSPD & ~(0x03 << (SLAVE_PIN * 2));
    GPIOE_OSPD |= (0x03 << (SLAVE_PIN * 2));

}

static void spi1_pins_setup(void)
{
    uint32_t reg;
    AHB1_CLOCK_ER |= GPIOA_AHB1_CLOCK_ER;
    /* Set mode = AF */
    reg = GPIOA_MODE & ~ (0x03 << (SPI1_CLOCK_PIN * 2));
    GPIOA_MODE = reg | (2 << (SPI1_CLOCK_PIN * 2));
    reg = GPIOA_MODE & ~ (0x03 << (SPI1_MOSI_PIN * 2));
    GPIOA_MODE = reg | (2 << (SPI1_MOSI_PIN * 2));
    reg = GPIOA_MODE & ~ (0x03 << (SPI1_MISO_PIN * 2));
    GPIOA_MODE = reg | (2 << (SPI1_MISO_PIN * 2));

    /* Alternate function: use low pins (5,6,7) */
    reg = GPIOA_AFL & ~(0xf << ((SPI1_CLOCK_PIN) * 4));
    GPIOA_AFL = reg | (SPI1_PIN_AF << ((SPI1_CLOCK_PIN) * 4));
    reg = GPIOA_AFL & ~(0xf << ((SPI1_MOSI_PIN) * 4));
    GPIOA_AFL = reg | (SPI1_PIN_AF << ((SPI1_MOSI_PIN) * 4));
    reg = GPIOA_AFL & ~(0xf << ((SPI1_MISO_PIN) * 4));
    GPIOA_AFL = reg | (SPI1_PIN_AF << ((SPI1_MISO_PIN) * 4));

}

static void spi1_reset(void)
{
    APB2_CLOCK_RST |= SPI1_APB2_CLOCK_ER_VAL;
    APB2_CLOCK_RST &= ~SPI1_APB2_CLOCK_ER_VAL;
}

uint8_t spi1_read(void)
{
    volatile uint32_t reg;
    do {
        reg = SPI1_SR;
    } while ((reg & SPI_SR_RX_NOTEMPTY) == 0);
    return (uint8_t)SPI1_DR;
}

void spi1_write(const char byte)
{
    int i;
    volatile uint32_t reg;
    SPI1_DR = byte;
    do {
        reg = SPI1_SR;
    } while ((reg & SPI_SR_TX_EMPTY) == 0);
}


void spi1_setup(int polarity, int phase)
{
    spi1_pins_setup();
    slave_pin_setup();
    APB2_CLOCK_ER |= SPI1_APB2_CLOCK_ER_VAL;
    spi1_reset();
	SPI1_CR1 = SPI_CR1_MASTER | (5 << 3) | (polarity << 1) | (phase << 0);
	SPI1_CR2 |= SPI_CR2_SSOE;
    SPI1_CR1 |= SPI_CR1_SPI_EN;
}

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
#include "i2c.h"

#define I2C1 (0x40005400)
#define APB1_SPEED_IN_MHZ (42)
#define I2C1_CR1        (*(volatile uint32_t *)(I2C1))
#define I2C1_CR2        (*(volatile uint32_t *)(I2C1 + 0x04))
#define I2C1_OAR1       (*(volatile uint32_t *)(I2C1 + 0x08))
#define I2C1_OAR2       (*(volatile uint32_t *)(I2C1 + 0x0c))
#define I2C1_DR         (*(volatile uint32_t *)(I2C1 + 0x10))
#define I2C1_SR1        (*(volatile uint32_t *)(I2C1 + 0x14))
#define I2C1_SR2        (*(volatile uint32_t *)(I2C1 + 0x18))
#define I2C1_CCR        (*(volatile uint32_t *)(I2C1 + 0x1c))
#define I2C1_TRISE      (*(volatile uint32_t *)(I2C1 + 0x20))

#define I2C_CR1_ENABLE               (1 << 0)
#define I2C_CR1_START			     (1 << 8)
#define I2C_CR1_STOP			     (1 << 9)
#define I2C_CR1_ACK			         (1 << 10)
#define I2C_CR2_FREQ_MASK            (0x3ff)
#define I2C_CCR_MASK                 (0xfff)
#define I2C_TRISE_MASK                (0x3f)


#define I2C_SR1_START                   (1 << 0)
#define I2C_SR1_RX_NOTEMPTY  	        (1 << 6)
#define I2C_SR1_TX_EMPTY			        (1 << 7)


#define I2C_SR2_MASTER              (1 << 0)
#define I2C_SR2_BUSY                (1 << 1)
#define I2C_SR2_XMIT                (1 << 2)

#define APB1_CLOCK_ER           (*(volatile uint32_t *)(0x40023840))
#define APB1_CLOCK_RST          (*(volatile uint32_t *)(0x40023820))
#define I2C1_APB1_CLOCK_ER_VAL 	(1 << 21)


#define CLOCK_SPEED (168000000) 

#define AHB1_CLOCK_ER (*(volatile uint32_t *)(0x40023830))
#define GPIOB_AHB1_CLOCK_ER (1 << 1)

#define GPIOB_BASE 0x40020400
#define GPIOB_MODE  (*(volatile uint32_t *)(GPIOB_BASE + 0x00))
#define GPIOB_AFL   (*(volatile uint32_t *)(GPIOB_BASE + 0x20))
#define GPIOB_AFH   (*(volatile uint32_t *)(GPIOB_BASE + 0x24))
#define GPIO_MODE_AF (2)

#define GPIOD_BASE

#define I2C1_PIN_AF 4
#define I2C1_SDA 9
#define I2C1_SCL 6
#define GPIO_MODE_AF (2)

static void i2c1_pins_setup(void)
{
    uint32_t reg;
    AHB1_CLOCK_ER |= GPIOB_AHB1_CLOCK_ER;
    /* Set mode = AF */
    reg = GPIOB_MODE & ~ (0x03 << (I2C1_SCL * 2));
    GPIOB_MODE = reg | (2 << (I2C1_SCL * 2));
    reg = GPIOB_MODE & ~ (0x03 << (I2C1_SDA * 2));
    GPIOB_MODE = reg | (2 << (I2C1_SDA * 2));

    /* Alternate function: */
    reg =  GPIOB_AFL & ~(0xf << ((I2C1_SCL) * 4));
    GPIOB_AFL = reg | (I2C1_PIN_AF << ((I2C1_SCL) * 4));
    reg =  GPIOB_AFH & ~(0xf << ((I2C1_SDA - 8) * 4));
    GPIOB_AFH = reg | (I2C1_PIN_AF << ((I2C1_SDA - 8) * 4));
}

static void i2c1_reset(void)
{
    APB1_CLOCK_RST |= I2C1_APB1_CLOCK_ER_VAL;
    APB1_CLOCK_RST &= ~I2C1_APB1_CLOCK_ER_VAL;
}

static void i2c1_send_start(void)
{
    volatile uint32_t sr1;
    I2C1_CR1 |= I2C_CR1_START;
    do {
        sr1 = I2C1_SR1;
    } while ((sr1 & I2C_SR1_START) == 0);;
}

static void i2c1_send_stop(void)
{
    I2C1_CR1 |= I2C_CR1_STOP;
}

void i2c1_test_sequence(void)
{
    volatile uint32_t sr1, sr2;
    const uint8_t address = 0x42;
    I2C1_CR1 &= ~I2C_CR1_ENABLE;
    I2C1_CR1 &= ~I2C_CR1_STOP;
    I2C1_CR1 &= ~I2C_CR1_ACK;
    I2C1_CR1 |= I2C_CR1_ENABLE;

    /* Wait if the bus is busy */
    do {
        sr2 = I2C1_SR2;
    } while ((sr2 & I2C_SR2_BUSY) != 0);;
    
    /* Send a start condition */
    i2c1_send_start;

    /* Send address + R/W = 0 */
    I2C1_DR = (address << 1);
    do {
        sr2 = I2C1_SR2;
    } while ((sr2 & (I2C_SR2_BUSY | I2C_SR2_MASTER)) != (I2C_SR2_BUSY | I2C_SR2_MASTER));;
    I2C1_DR = (0x00);
    do {
        sr1 = I2C1_SR1;
    } while ((sr1 & I2C_SR1_TX_EMPTY) != 0);;
    I2C1_DR = (0x01);
    do {
        sr1 = I2C1_SR1;
    } while ((sr1 & I2C_SR1_TX_EMPTY) != 0);;

    i2c1_send_stop();
}

void i2c1_setup(void)
{
    uint32_t reg;
    i2c1_pins_setup();
    APB1_CLOCK_ER |= I2C1_APB1_CLOCK_ER_VAL;
    I2C1_CR1 &= ~I2C_CR1_ENABLE;
    i2c1_reset();

    reg =   I2C1_CR2 & ~(I2C_CR2_FREQ_MASK);
    I2C1_CR2 = reg | APB1_SPEED_IN_MHZ;

    reg =   I2C1_CCR & ~(I2C_CCR_MASK);
    I2C1_CCR = reg | (APB1_SPEED_IN_MHZ * 5);
        
    reg = I2C1_TRISE & ~(I2C_TRISE_MASK);
    I2C1_TRISE = reg | (APB1_SPEED_IN_MHZ + 1);

    I2C1_CR1 |= I2C_CR1_ENABLE;
}

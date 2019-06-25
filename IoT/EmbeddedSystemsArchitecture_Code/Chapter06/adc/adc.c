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


/* ADC */

#define APB2_CLOCK_ER           (*(volatile uint32_t *)(0x40023844))
#define ADC1_APB2_CLOCK_ER_VAL 	(1 << 8)

#define ADC1_BASE       (0x40012000)
#define ADC_COM_BASE    (0x40012300)
#define ADC_COM_CCR     (*(volatile uint32_t *)(ADC_COM_BASE + 0x04))
#define ADC1_SR         (*(volatile uint32_t *)(ADC1_BASE + 0x00))
#define ADC1_CR1        (*(volatile uint32_t *)(ADC1_BASE + 0x04))
#define ADC1_CR2        (*(volatile uint32_t *)(ADC1_BASE + 0x08))
#define ADC1_SMPR1      (*(volatile uint32_t *)(ADC1_BASE + 0x0c))
#define ADC1_SMPR2      (*(volatile uint32_t *)(ADC1_BASE + 0x10))
#define ADC1_SQR3       (*(volatile uint32_t *)(ADC1_BASE + 0x34))
#define ADC1_DR         (*(volatile uint32_t *)(ADC1_BASE + 0x4c))
#define ADC_CR1_SCAN            (1 << 8)
#define ADC_CR2_EN              (1 << 0)
#define ADC_CR2_CONT            (1 << 1)
#define ADC_CR2_SWSTART         (1 << 30)
#define ADC_SR_EOC              (1 << 1)
#define ADC_SMPR_SMP_480CYC     (0x7)



/* GPIO */
#define AHB1_CLOCK_ER (*(volatile uint32_t *)(0x40023830))
#define GPIOB_AHB1_CLOCK_ER (1 << 1)
#define GPIOB_BASE  (0x40020400)
#define GPIOB_MODE  (*(volatile uint32_t *)(GPIOB_BASE + 0x00))

#define ADC_PIN     (1)
#define ADC_PIN_CHANNEL (9)

int adc_init(void)
{
    /* Enable clock */
    APB2_CLOCK_ER |= ADC1_APB2_CLOCK_ER_VAL;
    AHB1_CLOCK_ER |= GPIOB_AHB1_CLOCK_ER;

    /* Set pin in Analog mode */
    GPIOB_MODE |= (0x03 << (ADC_PIN * 2));

    /* Power off */
    ADC1_CR2 &= ~(ADC_CR2_EN);

    /* Set common clock prescaler */
    ADC_COM_CCR &= ~(0x03 << 16);

    /* Disable scan mode */
    ADC1_CR1 &= ~(ADC_CR1_SCAN);

    /* Set one-shot (disable continuous mode) */
    ADC1_CR2 &= ~(ADC_CR2_CONT);

    /* Set sample time for channel */
    if (ADC_PIN_CHANNEL > 9) {
        uint32_t val = ADC1_SMPR2;
    //    val = ADC_SMPR_SMP_480CYC << ((ADC_PIN_CHANNEL - 10) * 3);
        ADC1_SMPR2 = val;
    } else {
        uint32_t val = ADC1_SMPR1;
        val = ADC_SMPR_SMP_480CYC << (ADC_PIN_CHANNEL * 3);
        ADC1_SMPR1 = val;
    }

    ADC1_SQR3 |= (ADC_PIN_CHANNEL);
    ADC1_CR2 |= ADC_CR2_EN;
    return 0;
}


int adc_read(void)
{
    ADC1_CR2 |= ADC_CR2_SWSTART;
    while (ADC1_CR2 & ADC_CR2_SWSTART);;
    while ((ADC1_SR & ADC_SR_EOC) == 0);;
    return (int)(ADC1_DR);
}

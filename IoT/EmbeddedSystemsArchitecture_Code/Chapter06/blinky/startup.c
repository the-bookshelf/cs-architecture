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

extern unsigned int _stored_data;
extern unsigned int _start_data;
extern unsigned int _end_data;
extern unsigned int _start_bss;
extern unsigned int _end_bss;
extern unsigned int _end_stack;
extern unsigned int _start_heap;

static int zeroed_variable_in_bss;
static int initialized_variable_in_data = 42;

#define STACK_PAINTING

static volatile unsigned int avail_mem = 0;
static unsigned int sp;

extern void main(void);
extern void isr_tim2(void);
void isr_reset(void) {
    register unsigned int *src, *dst;
    src = (unsigned int *) &_stored_data;
    dst = (unsigned int *) &_start_data;
    /* Copy the .data section from flash to RAM. */
    while (dst < (unsigned int *)&_end_data) {
        *dst = *src;
        dst++;
        src++;
    }

    /* Initialize the BSS section to 0 */
    dst = &_start_bss;
    while (dst < (unsigned int *)&_end_bss) {
        *dst = 0U;
        dst++;
    }

    /* Paint the stack. */
    avail_mem = &_end_stack - &_start_heap;
#ifdef STACK_PAINTING
    {
        asm volatile("mrs %0, msp" : "=r"(sp));
        dst = ((unsigned int *)(&_end_stack)) - (8192 / sizeof(unsigned int)); ;
        while ((unsigned int)dst < sp) {
            *dst = 0xDEADC0DE;
            dst++;
        }
    }
#endif
    /* Run the program! */
    main();
}

void isr_fault(void)
{
    /* Panic. */
    while(1) ;;
}


void isr_memfault(void)
{
    /* Panic. */
    while(1) ;;
}

void isr_busfault(void)
{
    /* Panic. */
    while(1) ;;
}

void isr_usagefault(void)
{
    /* Panic. */
    while(1) ;;
}
        

void isr_empty(void)
{
    /* Ignore the event and continue */
}



__attribute__ ((section(".isr_vector")))
void (* const IV[])(void) =
{
	(void (*)(void))(&_end_stack),
	isr_reset,                   // Reset
	isr_fault,                   // NMI
	isr_fault,                   // HardFault
	isr_memfault,                // MemFault
	isr_busfault,                // BusFault
	isr_usagefault,              // UsageFault
	0, 0, 0, 0,                  // 4x reserved
	isr_empty,                   // SVC
	isr_empty,                   // DebugMonitor
	0,                           // reserved
	isr_empty,                   // PendSV
	isr_empty,                   // SysTick
    
    isr_empty,              // NVIC_WWDG_IRQ 0
    isr_empty,              // PVD_IRQ 1
    isr_empty,              // TAMP_STAMP_IRQ 2
    isr_empty,              // RTC_WKUP_IRQ 3
    isr_empty,              // FLASH_IRQ 4
    isr_empty,              // RCC_IRQ 5
    isr_empty,              // EXTI0_IRQ 6
    isr_empty,              // EXTI1_IRQ 7
    isr_empty,              // EXTI2_IRQ 8
    isr_empty,              // EXTI3_IRQ 9
    isr_empty,              // EXTI4_IRQ 10
    isr_empty,              // DMA1_STREAM0_IRQ 11
    isr_empty,              // DMA1_STREAM1_IRQ 12
    isr_empty,              // DMA1_STREAM2_IRQ 13
    isr_empty,              // DMA1_STREAM3_IRQ 14
    isr_empty,              // DMA1_STREAM4_IRQ 15
    isr_empty,              // DMA1_STREAM5_IRQ 16
    isr_empty,              // DMA1_STREAM6_IRQ 17
    isr_empty,              // ADC_IRQ 18
    isr_empty,              // CAN1_TX_IRQ 19
    isr_empty,              // CAN1_RX0_IRQ 20
    isr_empty,              // CAN1_RX1_IRQ 21
    isr_empty,              // CAN1_SCE_IRQ 22
    isr_empty,              // EXTI9_5_IRQ 23
    isr_empty,              // TIM1_BRK_TIM9_IRQ 24
    isr_empty,              // TIM1_UP_TIM10_IRQ 25
    isr_empty,              // TIM1_TRG_COM_TIM11_IRQ 26
    isr_empty,              // TIM1_CC_IRQ 27
    isr_tim2 ,              // TIM2_IRQ 28
    isr_empty,              // TIM3_IRQ 29
    isr_empty,              // TIM4_IRQ 30
    isr_empty,              // I2C1_EV_IRQ 31
    isr_empty,              // I2C1_ER_IRQ 32
    isr_empty,              // I2C2_EV_IRQ 33
    isr_empty,              // I2C2_ER_IRQ 34
    isr_empty,              // SPI1_IRQ 35
    isr_empty,              // SPI2_IRQ 36
    isr_empty,              // USART1_IRQ 37
    isr_empty,              // USART2_IRQ 38
    isr_empty,              // USART3_IRQ 39
    isr_empty,              // EXTI15_10_IRQ 40
    isr_empty,              // RTC_ALARM_IRQ 41
    isr_empty,              // USB_FS_WKUP_IRQ 42
    isr_empty,              // TIM8_BRK_TIM12_IRQ 43
    isr_empty,              // TIM8_UP_TIM13_IRQ 44
    isr_empty,              // TIM8_TRG_COM_TIM14_IRQ 45
    isr_empty,              // TIM8_CC_IRQ 46
    isr_empty,              // DMA1_STREAM7_IRQ 47



};

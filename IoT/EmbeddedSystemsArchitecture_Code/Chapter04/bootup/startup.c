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

extern void *END_STACK;

static int zeroed_variable_in_bss;
static int initialized_variable_in_data = 42;
void main(void);
void isr_reset(void) {
    unsigned int *src, *dst;

    /* Copy the .data section from flash to RAM. */
    src = (unsigned int *) &_stored_data;
    dst = (unsigned int *) &_start_data;
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

    /* Run the program! */
    main();
}

void isr_fault(void)
{
    /* Panic. */
    while(1) ;;
}

void isr_empty(void)
{
    /* Ignore the event and continue */
}

/* This is the main program loop.
 *
 * It is meant to be run within GDB to check the 
 * addresses and the values of the variables in 
 * .data and .bss sections.
 *
 */
void main(void) {

    /* Increment test variables at each loop */
    while(1) {
        zeroed_variable_in_bss++;
        initialized_variable_in_data++;
    }
}


/* This is the interrupt vector.
 * 
 * It sits at the beginning of the flash image,
 * and contains the initial stack pointer, and 
 * the service routines associated to exceptions 
 * and interrupt requests.
 *
 * Replace the default empty service routine with
 * the corresponding handler.
 */
__attribute__ ((section(".isr_vector")))
void (* const IV[])(void) =
{
    (void (*)(void))(&END_STACK),
    isr_reset,                   // Reset
    isr_fault,                   // NMI
    isr_fault,                   // HardFault
    isr_fault,                   // MemFault
    isr_fault,                   // BusFault
    isr_fault,                   // UsageFault
    0, 0, 0, 0,                  // 4x reserved
    isr_empty,                   // SVC
    isr_empty,                   // DebugMonitor
    0,                           // reserved
    isr_empty,                   // PendSV
    isr_empty,                   // SysTick

    isr_empty,                     // GPIO Port A
    isr_empty,                     // GPIO Port B
    isr_empty,                     // GPIO Port C
    isr_empty,                     // GPIO Port D
    isr_empty,                     // GPIO Port E
    isr_empty,                     // UART0 Rx and Tx
    isr_empty,                     // UART1 Rx and Tx
    isr_empty,                     // SSI0 Rx and Tx
    isr_empty,                     // I2C0 Master and Slave
    isr_empty,                     // PWM Fault
    isr_empty,                     // PWM Generator 0
    isr_empty,                     // PWM Generator 1
    isr_empty,                     // PWM Generator 2
    isr_empty,                     // Quadrature Encoder 0
    isr_empty,                     // ADC Sequence 0
    isr_empty,                     // ADC Sequence 1
    isr_empty,                     // ADC Sequence 2
    isr_empty,                     // ADC Sequence 3
    isr_empty,                     // Watchdog timer
    isr_empty,                     // Timer 0 subtimer A
    isr_empty,                     // Timer 0 subtimer B
    isr_empty,                     // Timer 1 subtimer A
    isr_empty,                     // Timer 1 subtimer B
    isr_empty,                     // Timer 2 subtimer A
    isr_empty,                     // Timer 3 subtimer B
    isr_empty,                     // Analog Comparator 0
    isr_empty,                     // Analog Comparator 1
    isr_empty,                     // Analog Comparator 2
    isr_empty,                     // System Control (PLL, OSC, BO)
    isr_empty,                     // FLASH Control
    isr_empty,                     // GPIO Port F
    isr_empty,                     // GPIO Port G
    isr_empty,                     // GPIO Port H
    isr_empty,                     // UART2 Rx and Tx
    isr_empty,                     // SSI1 Rx and Tx
    isr_empty,                     // Timer 3 subtimer A
    isr_empty,                     // Timer 3 subtimer B
    isr_empty,                     // I2C1 Master and Slave
    isr_empty,                     // Quadrature Encoder 1
    isr_empty,                     // CAN0
    isr_empty,                     // CAN1
    isr_empty,                     // CAN2
    isr_empty,                     // Ethernet
    isr_empty,                     // Hibernate
};

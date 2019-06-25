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

#define MPU_BASE (0xE000ED90)
extern uint32_t _end_stack;

/* FAULT enable register SHCSR */
#define SHCSR (*(volatile uint32_t *)(0xE000ED24))

/* Bit to enable memory fault handler */
#define MEMFAULT_ENABLE (1 << 16)

/* MPU Registers */
#define MPU_TYPE (*(volatile uint32_t *)(MPU_BASE + 0x00))
#define MPU_CTRL (*(volatile uint32_t *)(MPU_BASE + 0x04))
#define MPU_RNR  (*(volatile uint32_t *)(MPU_BASE + 0x08))
#define MPU_RBAR (*(volatile uint32_t *)(MPU_BASE + 0x0c))
#define MPU_RASR (*(volatile uint32_t *)(MPU_BASE + 0x10))

/* Some use-case specific values for RASR */
#define RASR_ENABLED    (1)
#define RASR_KERNEL_RW  (1 << 24)
#define RASR_RDONLY     (6 << 24)
#define RASR_NOACCESS   (0 << 24)
#define RASR_KERNEL_RO  (5 << 24)
#define RASR_USER_RW    (3 << 24)
#define RASR_USER_RO    (2 << 24)

#define RASR_SCB        (7 << 16)
#define RASR_SB         (5 << 16)
#define RASR_NOEXEC     (1 << 28)


/* Size */
#define MPUSIZE_1K      (0x09 << 1)
#define MPUSIZE_2K      (0x0a << 1)
#define MPUSIZE_4K      (0x0b << 1)
#define MPUSIZE_8K      (0x0c << 1)
#define MPUSIZE_16K     (0x0d << 1)
#define MPUSIZE_32K     (0x0e << 1)
#define MPUSIZE_64K     (0x0f << 1)
#define MPUSIZE_128K    (0x10 << 1)
#define MPUSIZE_256K    (0x11 << 1)
#define MPUSIZE_512K    (0x12 << 1)
#define MPUSIZE_1M      (0x13 << 1)
#define MPUSIZE_2M      (0x14 << 1)
#define MPUSIZE_4M      (0x15 << 1)
#define MPUSIZE_8M      (0x16 << 1)
#define MPUSIZE_16M     (0x17 << 1)
#define MPUSIZE_32M     (0x18 << 1)
#define MPUSIZE_64M     (0x19 << 1)
#define MPUSIZE_128M    (0x1a << 1)
#define MPUSIZE_256M    (0x1b << 1)
#define MPUSIZE_512M    (0x1c << 1)
#define MPUSIZE_1G      (0x1d << 1)
#define MPUSIZE_2G      (0x1e << 1)
#define MPUSIZE_4G      (0x1f << 1)



static void mpu_set_region(int region, uint32_t start, uint32_t attr)
{
    MPU_RNR = region;
    MPU_RBAR = start;
    MPU_RNR = region;
    MPU_RASR = attr;
}

void mpu_task_stack_permit(void *start)
{
    uint32_t attr = 
        RASR_ENABLED | MPUSIZE_1K | RASR_SCB | RASR_USER_RW;
    MPU_CTRL = 0;
    DMB();
    mpu_set_region(3, (uint32_t)start, attr);
    MPU_CTRL = 1;
}


int mpu_enable(void)
{
    volatile uint32_t type;
    volatile uint32_t start;
    volatile uint32_t attr;

    type = MPU_TYPE;
    if (type == 0) {
        /* MPU not present! */
        return -1;
    }


    /* Disable MPU to reconfigure regions */
    MPU_CTRL = 0;

    /* Set flash area as system-wide read-only, executable */
    start = 0;
    attr = RASR_ENABLED | MPUSIZE_256K | RASR_SCB | RASR_RDONLY;
    mpu_set_region(0, start, attr);

    /* Set SRAM as read-write, not executable */
    start = 0x20000000;
    attr = RASR_ENABLED | MPUSIZE_128K | RASR_SCB | RASR_USER_RW | RASR_NOEXEC;
    mpu_set_region(1, start, attr);

    /* Reserve CCRAM for kernel use */
    start = 0x10000000;
    attr = RASR_ENABLED | MPUSIZE_64K | RASR_SCB | RASR_KERNEL_RW | RASR_NOEXEC;
    mpu_set_region(2, start, attr);

    /* Peripherals region */
    start = 0x40000000;
    attr = RASR_ENABLED | MPUSIZE_1G | RASR_SB | RASR_KERNEL_RW | RASR_NOEXEC;
    mpu_set_region(4, start, attr);

    /* Set System register area */
    start = 0xE0000000;
    attr = RASR_ENABLED | MPUSIZE_256M | RASR_SB | RASR_KERNEL_RW | RASR_NOEXEC;
    mpu_set_region(5, start, attr);
    
    /* Enable MEMFAULT */
    SHCSR |= MEMFAULT_ENABLE;

    /* Enable the MPU, no background region */
    MPU_CTRL = 1;
    return 0;

}

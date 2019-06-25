#include <stdint.h>
#include "system.h"

extern uint32_t cpu_freq;

static void flash_set_waitstates(int waitstates)
{
    FLASH_ACR |=  waitstates | FLASH_ACR_ENABLE_DATA_CACHE | FLASH_ACR_ENABLE_INST_CACHE;
}

void clock_pll_off(void)
{
    uint32_t reg32;
    /* Enable internal high-speed oscillator. */
    RCC_CR |= RCC_CR_HSION;
    DMB();
    while ((RCC_CR & RCC_CR_HSIRDY) == 0) {};

    /* Select HSI as SYSCLK source. */
    reg32 = RCC_CFGR;
    reg32 &= ~((1 << 1) | (1 << 0));
    RCC_CFGR = (reg32 | RCC_CFGR_SW_HSI);
    DMB();

    /* Turn off PLL */
    RCC_CR &= ~RCC_CR_PLLON;
    DMB();
}


void clock_pll_on(int powersave)
{
    uint32_t reg32;
    uint32_t plln, pllm, pllq, pllp, pllr, hpre, ppre1, ppre2, flash_waitstates;
    
    /* Enable Power controller */
    APB1_CLOCK_ER |= PWR_APB1_CLOCK_ER_VAL;

    /* Select clock parameters */
    if (powersave) { /* 48 MHz */
        cpu_freq = 48000000;
        pllm = 8;
        plln = 96;
        pllp = 2;
        pllq = 2;
        pllr = 0;
        hpre = RCC_PRESCALER_DIV_NONE;
        ppre1 = RCC_PRESCALER_DIV_4; 
        ppre2 = RCC_PRESCALER_DIV_2;
        flash_waitstates = 5;
    } else { /* 168 MHz */
        cpu_freq = 168000000;
        pllm = 8;
        plln = 336;
        pllp = 2;
        pllq = 7;
        pllr = 0;
        hpre = RCC_PRESCALER_DIV_NONE;
        ppre1 = RCC_PRESCALER_DIV_4; 
        ppre2 = RCC_PRESCALER_DIV_2;
        flash_waitstates = 3;
    }

    flash_set_waitstates(flash_waitstates);

    /* Enable internal high-speed oscillator. */
    RCC_CR |= RCC_CR_HSION;
    DMB();
    while ((RCC_CR & RCC_CR_HSIRDY) == 0) {};

    /* Select HSI as SYSCLK source. */
    reg32 = RCC_CFGR;
    reg32 &= ~((1 << 1) | (1 << 0));
    RCC_CFGR = (reg32 | RCC_CFGR_SW_HSI);
    DMB();

    /* Enable external high-speed oscillator 8MHz. */
    RCC_CR |= RCC_CR_HSEON;
    DMB();
    while ((RCC_CR & RCC_CR_HSERDY) == 0) {};

    /*
     * Set prescalers for AHB, ADC, ABP1, ABP2.
     */
    reg32 = RCC_CFGR;
    reg32 &= ~(0xF0);
    RCC_CFGR = (reg32 | (hpre << 4));
    DMB();
    reg32 = RCC_CFGR;
    reg32 &= ~(0x1C00);
    RCC_CFGR = (reg32 | (ppre1 << 10));
    DMB();
    reg32 = RCC_CFGR;
    reg32 &= ~(0x07 << 13);
    RCC_CFGR = (reg32 | (ppre2 << 13));
    DMB();

    /* Set PLL config */
    reg32 = RCC_PLLCFGR;
    reg32 &= ~(PLL_FULL_MASK);
    RCC_PLLCFGR = reg32 | RCC_PLLCFGR_PLLSRC | pllm | 
        (plln << 6) | (((pllp >> 1) - 1) << 16) | 
        (pllq << 24); 
    DMB();

    /* Enable power-save mode if selected */
    if (powersave) { 
        POW_CR |= (POW_CR_VOS);
    }

    /* Enable PLL oscillator and wait for it to stabilize. */
    RCC_CR |= RCC_CR_PLLON;
    DMB();
    while ((RCC_CR & RCC_CR_PLLRDY) == 0) {};

    /* Select PLL as SYSCLK source. */
    reg32 = RCC_CFGR;
    reg32 &= ~((1 << 1) | (1 << 0));
    RCC_CFGR = (reg32 | RCC_CFGR_SW_PLL);
    DMB();

    /* Wait for PLL clock to be selected. */
    while ((RCC_CFGR & ((1 << 1) | (1 << 0))) != RCC_CFGR_SW_PLL) {};

    /* Disable internal high-speed oscillator. */
    RCC_CR &= ~RCC_CR_HSION;
}


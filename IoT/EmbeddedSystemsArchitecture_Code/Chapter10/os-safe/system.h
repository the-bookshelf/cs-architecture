#ifndef SYSTEM_H_INCLUDED
#define SYSTEM_H_INCLUDED

/* System specific: PLL with 8 MHz external oscillator, CPU at 168MHz */
#define CPU_FREQ (48000000)
#define PLL_FULL_MASK (0x7F037FFF)

/* STM32 specific defines */
#define APB1_CLOCK_ER           (*(volatile uint32_t *)(0x40023840))
#define APB1_CLOCK_RST          (*(volatile uint32_t *)(0x40023820))
#define TIM2_APB1_CLOCK_ER_VAL 	(1 << 0)
#define PWR_APB1_CLOCK_ER_VAL   (1 << 28)

#define APB2_CLOCK_ER (*(volatile uint32_t *)(0x40023844))
#define APB2_CLOCK_RST          (*(volatile uint32_t *)(0x40023824))
#define SYSCFG_APB2_CLOCK_ER (1 << 14)

/* SCB for sleep configuration */
#define SCB_SCR (*(volatile uint32_t *)(0xE000ED10))
#define SCB_SCR_SEVONPEND	(1 << 4)
#define SCB_SCR_SLEEPDEEP		(1 << 2)
#define SCB_SCR_SLEEPONEXIT (1 << 1)

/* Assembly helpers */
#define DMB() __asm__ volatile ("dmb")
#define WFI() __asm__ volatile ("wfi")
#define WFE() __asm__ volatile ("wfe")
#define SEV() __asm__ volatile ("sev")
#define SVC() __asm__ volatile ("svc 0")

/* Master clock setting */
void clock_pll_on(int powersave);
void clock_pll_off(void);


/* NVIC */
/* NVIC ISER Base register (Cortex-M) */
#define NVIC_EXTI0_IRQN          (6)
#define NVIC_TIM2_IRQN          (28)
#define NVIC_ISER_BASE (0xE000E100)
#define NVIC_ICER_BASE (0xE000E180)
#define NVIC_ICPR_BASE (0xE000E280)
#define NVIC_IPRI_BASE (0xE000E400)

static inline void nvic_irq_enable(uint8_t n)
{
    int i = n / 32;
    volatile uint32_t *nvic_iser = ((volatile uint32_t *)(NVIC_ISER_BASE + 4 * i));
    *nvic_iser |= (1 << (n % 32));
}

static inline void nvic_irq_disable(uint8_t n)
{
    int i = n / 32;
    volatile uint32_t *nvic_icer = ((volatile uint32_t *)(NVIC_ICER_BASE + 4 * i));
    *nvic_icer |= (1 << (n % 32));
}

static inline void nvic_irq_setprio(uint8_t n, uint8_t prio)
{
    volatile uint8_t *nvic_ipri = ((volatile uint8_t *)(NVIC_IPRI_BASE + n));
    *nvic_ipri = prio;
}


static inline void nvic_irq_clear(uint8_t n)
{
    int i = n / 32;
    volatile uint8_t *nvic_icpr = ((volatile uint8_t *)(NVIC_ICPR_BASE + 4 * i));
    *nvic_icpr = (1 << (n % 32));
}



/*** FLASH ***/
#define FLASH_BASE (0x40023C00)
#define FLASH_ACR  (*(volatile uint32_t *)(FLASH_BASE + 0x00))
#define FLASH_ACR_ENABLE_DATA_CACHE (1 << 10)
#define FLASH_ACR_ENABLE_INST_CACHE (1 << 9)

/*** RCC ***/

#define RCC_BASE (0x40023800)
#define RCC_CR      (*(volatile uint32_t *)(RCC_BASE + 0x00))
#define RCC_PLLCFGR (*(volatile uint32_t *)(RCC_BASE + 0x04))
#define RCC_CFGR    (*(volatile uint32_t *)(RCC_BASE + 0x08))
#define RCC_CR      (*(volatile uint32_t *)(RCC_BASE + 0x00))

#define RCC_CR_PLLRDY               (1 << 25)
#define RCC_CR_PLLON                (1 << 24)
#define RCC_CR_HSERDY               (1 << 17)
#define RCC_CR_HSEON                (1 << 16)
#define RCC_CR_HSIRDY               (1 << 1)
#define RCC_CR_HSION                (1 << 0)

#define RCC_CFGR_SW_HSI             0x0
#define RCC_CFGR_SW_HSE             0x1
#define RCC_CFGR_SW_PLL             0x2


#define RCC_PLLCFGR_PLLSRC          (1 << 22)

#define RCC_PRESCALER_DIV_NONE 0
#define RCC_PRESCALER_DIV_2    8
#define RCC_PRESCALER_DIV_4    9

/* POWER CONTROL REGISTER */
#define POW_BASE (0x40007000)
#define POW_CR (*(volatile uint32_t *)(POW_BASE + 0x00))
#define POW_SCR (*(volatile uint32_t *)(POW_BASE + 0x04))

#define POW_CR_VOS (1 << 14)
#define POW_CR_FPDS (1 << 9)
#define POW_CR_CSBF  (1 << 3)
#define POW_CR_CWUF  (1 << 2)
#define POW_CR_PDDS  (1 << 1)
#define POW_CR_LPDS  (1 << 0)
#define POW_SCR_WUF   (1 << 0)
#define POW_SCR_EWUP (1 << 4)
#define POW_SCR_BRE (1 << 9)


#if (CPU_FREQ == 168000000)
#   define PLLM 8
#   define PLLN 336
#   define PLLP 2 
#   define PLLQ 7
#   define PLLR 0
#   define POWER_SAVE 1
#   define HPRE RCC_PRESCALER_DIV_NONE
#   define PPRE1 RCC_PRESCALER_DIV_4
#   define PPRE2 RCC_PRESCALER_DIV_2
#   define FLASH_WAITSTATES 5
#elif (CPU_FREQ == 120000000)
#   define PLLM 8
#   define PLLN 240
#   define PLLP 2 
#   define PLLQ 5
#   define PLLR 0
#   define HPRE RCC_PRESCALER_DIV_NONE
#   define PPRE1 RCC_PRESCALER_DIV_4
#   define PPRE2 RCC_PRESCALER_DIV_2
#   define FLASH_WAITSTATES 3
#elif (CPU_FREQ == 100000000)
#   define PLLM 8
#   define PLLN 192
#   define PLLP 2 
#   define PLLQ 4
#   define PLLR 0
#   define POWER_SAVE 1
#   define HPRE RCC_PRESCALER_DIV_NONE
#   define PPRE1 RCC_PRESCALER_DIV_2
#   define PPRE2 RCC_PRESCALER_DIV_NONE
#   define FLASH_WAITSTATES 2
#elif (CPU_FREQ == 84000000)
#   define PLLM 8
#   define PLLN 336
#   define PLLP 4 
#   define PLLQ 7
#   define PLLR 0
#   define HPRE RCC_PRESCALER_DIV_NONE
#   define PPRE1 RCC_PRESCALER_DIV_2
#   define PPRE2 RCC_PRESCALER_DIV_NONE
#   define FLASH_WAITSTATES 2
#elif (CPU_FREQ == 48000000)
#   define PLLM 8
#   define PLLN 96
#   define PLLP 2 
#   define PLLQ 2
#   define PLLR 0
#   define POWER_SAVE 1
#   define HPRE RCC_PRESCALER_DIV_NONE
#   define PPRE1 RCC_PRESCALER_DIV_4 
#   define PPRE2 RCC_PRESCALER_DIV_2
#   define FLASH_WAITSTATES 3
#else 
# error "Please select a valid CPU_FREQ in system.h"
#endif


#endif

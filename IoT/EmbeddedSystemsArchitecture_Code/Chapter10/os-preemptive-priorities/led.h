#ifndef GPIO_H_INCLUDED
#define GPIO_H_INCLUDED
#define BLUE_LED_PIN (15)
#define RED_LED_PIN (14)
#define ORANGE_LED_PIN (13)
#define GREEN_LED_PIN (12)

void led_setup(void);
void led_on(int pin);
void led_off(int pin);
void led_toggle(int pin);

#define blue_led_on() led_on(BLUE_LED_PIN)
#define blue_led_off() led_off(BLUE_LED_PIN)
#define blue_led_toggle() led_toggle(BLUE_LED_PIN)

#define red_led_on() led_on(RED_LED_PIN)
#define red_led_off() led_off(RED_LED_PIN)
#define red_led_toggle() led_toggle(RED_LED_PIN)

#define orange_led_on() led_on(ORANGE_LED_PIN)
#define orange_led_off() led_off(ORANGE_LED_PIN)
#define orange_led_toggle() led_toggle(ORANGE_LED_PIN)

#define green_led_on() led_on(GREEN_LED_PIN)
#define green_led_off() led_off(GREEN_LED_PIN)
#define green_led_toggle() led_toggle(GREEN_LED_PIN)
#endif

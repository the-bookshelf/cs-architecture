#ifndef I2C_H_INCLUDED
#define I2C_H_INCLUDED
#include <stdint.h>


void i2c1_setup(void);
void i2c1_write(uint8_t addr, const char byte);
uint8_t i2c1_read(uint8_t addr);

void i2c1_test_sequence(void);

#endif

#ifndef SPI_H_INCLUDED
#define SPI_H_INCLUDED
#include <stdint.h>


void spi1_setup(int polarity, int phase);
void spi1_write(const char byte);
uint8_t spi1_read(void);
void slave_on(void);
void slave_off(void);


#endif

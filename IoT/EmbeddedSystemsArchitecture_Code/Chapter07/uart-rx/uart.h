#ifndef UART_H_INCLUDED
#define UART_H_INCLUDED
#include <stdint.h>


int uart3_setup(uint32_t bitrate, uint8_t data, char parity, uint8_t stop);
void uart3_write(const char *text);
char uart3_read(void);

#endif

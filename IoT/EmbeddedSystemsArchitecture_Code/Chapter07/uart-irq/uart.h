#ifndef UART_H_INCLUDED
#define UART_H_INCLUDED
#include <stdint.h>


int uart3_setup(uint32_t bitrate, uint8_t data, char parity, uint8_t stop);
void uart3_write(const char *text);
int uart3_read(char *buf, int len);

#endif

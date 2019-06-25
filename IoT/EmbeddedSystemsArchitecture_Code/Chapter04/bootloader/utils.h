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


#ifndef UTILS_H
#define UTILS_H

#ifdef BOOTLOADER
int utils_open(void);
int utils_write(const void *buf, int size);
int utils_read(void *buf, int size);
void utils_close(void);

__attribute__((section(".utils"),used)) 
static void *utils_interface[4] = {
    (void *)utils_open,
    (void *)utils_write,
    (void *)utils_read,
    (void *)utils_close
};

#else

static void **utils_interface = (void**)(0x00000400);

static inline int utils_open(void) {
    int (*do_open)(void) = (int (*)(void))(utils_interface[0]);
    return do_open();
}

static inline int utils_write(const void *buf, int size) {
    int (*do_write)(const void*,int) = (int (*)(const void*,int))(utils_interface[1]);
    return do_write(buf, size);
}

static inline int utils_read(void *buf, int size) {
    int (*do_read)(void*, int) = (int (*)(void*,int))(utils_interface[2]);
    return do_read(buf, size);
}

static inline void utils_close(void) {
    void (*do_close)(void) = (void (*)(void))(utils_interface[3]);
    do_close();
}

#endif /* ifdef BOOTLOADER */

#endif

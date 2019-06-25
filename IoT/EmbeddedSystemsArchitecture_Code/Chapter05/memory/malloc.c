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


extern unsigned int _start_heap;
#define NULL (((void *)0))

#ifdef OWN_MALLOC
struct malloc_block {
    unsigned int signature;
    unsigned int size;
};

#define SIGNATURE_IN_USE (0xAAC0FFEE)
#define SIGNATURE_FREED  (0xFEEDFACE)

void *malloc(unsigned int size)
{
    static unsigned int *end_heap = 0;
    struct malloc_block *blk;
    char *ret = NULL;
    if (((size >>2) << 2) != size)
        size = ((size >> 2) + 1) << 2;
    if (!end_heap) {
        end_heap = &_start_heap;
    }
    blk = (struct malloc_block *)&_start_heap;
    while ((unsigned int *)blk < end_heap) {
        if ((blk->signature == SIGNATURE_FREED) && (blk->size <= size)) {
            blk->signature = SIGNATURE_IN_USE;
            ret = ((char *)blk) + sizeof(struct malloc_block);
            return ret;
        }
        blk = (struct malloc_block *)(((char *)blk) + sizeof(struct malloc_block) + blk->size);
    }
    blk = (struct malloc_block *)end_heap;
    blk->signature = SIGNATURE_IN_USE;
    blk->size = size;
    ret = ((char *)end_heap) + sizeof(struct malloc_block);
    end_heap = (unsigned int *)(ret + size);
    return ret;
}

void free(void *ptr)
{
    struct malloc_block *blk = (struct malloc_block *) (((char *)ptr)-sizeof(struct malloc_block));
    if (!ptr)
        return;
    if (blk->signature != SIGNATURE_IN_USE)
        return;
    blk->signature = SIGNATURE_FREED;
}

#else /* Use newlib's malloc, only implement _sbrk() */

void * _sbrk(unsigned int incr)
{
    static unsigned char *heap = (unsigned char *)&_start_heap;
    void *old_heap = heap;
    if (((incr >> 2) << 2) != incr)
        incr = ((incr >> 2) + 1) << 2;

    if (heap == NULL)
		heap = (unsigned char *)&_start_heap;
	else
        heap += incr;
    return old_heap;
}

void * _sbrk_r(unsigned int incr)
{
    static unsigned char *heap = NULL;
    void *old_heap = heap;
    if (((incr >> 2) << 2) != incr)
        incr = ((incr >> 2) + 1) << 2;

    if (old_heap == NULL)
		old_heap = heap = (unsigned char *)&_start_heap;
    heap += incr;
    return old_heap;
}

#endif

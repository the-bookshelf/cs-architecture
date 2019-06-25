CROSS_COMPILE:=arm-none-eabi-
CC:=$(CROSS_COMPILE)gcc
LD:=$(CROSS_COMPILE)gcc

LSCRIPT:=target.ld

OBJCOPY:=$(CROSS_COMPILE)objcopy
CFLAGS:=-mcpu=cortex-m3 -mthumb -g -ggdb -Wall -Wno-main -Wstack-usage=200 -ffreestanding -Wno-unused -nostdlib #-DOWN_MALLOC
LDFLAGS:=-T $(LSCRIPT) -Wl,-gc-sections -Wl,-Map=image.map -mcpu=cortex-m3 -mthumb -nostartfiles

all: image.bin

image.bin: image.elf
	$(OBJCOPY) -O binary $^ $@

image.elf: main.o startup.o malloc.o mpu.o $(LSCRIPT)
	$(LD) $(LDFLAGS) startup.o main.o malloc.o mpu.o -o $@
	
startup.o: startup.c

main.o: main.c

clean:
	rm -f image.bin image.elf *.o image.map

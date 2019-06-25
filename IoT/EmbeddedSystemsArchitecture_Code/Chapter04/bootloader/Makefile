CROSS_COMPILE:=arm-none-eabi-
CC:=$(CROSS_COMPILE)gcc
LD:=$(CROSS_COMPILE)ld
OBJCOPY:=$(CROSS_COMPILE)objcopy

CFLAGS:=-mcpu=cortex-m3 -mthumb -g -ggdb -Wall -Wno-main -fstack-check
LDFLAGS:=-gc-sections -nostdlib

all: image.bin

image.bin: app.bin bootloader.bin
	cat bootloader.bin app.bin > image.bin

app.bin: app.elf
	$(OBJCOPY) -O binary $< $@

bootloader.bin: bootloader.elf
	$(OBJCOPY) -O binary --pad-to=0x001000 --gap-fill=0xFF $< $@

app.elf: startup.o app.ld
	$(LD) $(LDFLAGS) startup.o -o $@ -Map=app.map -T app.ld
	

bootloader.elf: startup_bl.o utils.o bootloader.ld
	$(LD) $(LDFLAGS) startup_bl.o utils.o -o $@ -Map=bootloader.map -T bootloader.ld

clean:
	rm -f *.bin *.elf *.o *.map

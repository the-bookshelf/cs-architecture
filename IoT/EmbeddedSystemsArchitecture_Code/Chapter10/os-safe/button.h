#ifndef BUTTON_H_INCLUDED
#define BUTTON_H_INCLUDED
void button_setup(void (*callback)(void));
void button_start_read(void);
int button_is_pressed(void);

#endif

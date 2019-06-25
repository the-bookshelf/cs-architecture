#ifndef MPU_H_INCLUDED
#define MPU_H_INCLUDED

int mpu_enable(void);
void mpu_task_stack_permit(void *start);

#endif

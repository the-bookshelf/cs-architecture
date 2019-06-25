.syntax unified

.global sem_trywait
sem_trywait:
   LDREX   r1, [r0]
   CMP      r1, #0
   BEQ     sem_trywait_fail
   SUBS    r1, #1
   STREX   r2, r1, [r0]
   CMP     r2, #0
   BNE     sem_trywait
   DMB
   MOVS    r0, #0
   BX      lr
sem_trywait_fail:
   DMB
   MOV     r0, #-1
   BX      lr

.global sem_dopost
sem_dopost:
   LDREX   r1, [r0]
   ADDS    r1, #1
   STREX   r2, r1, [r0]     
   CMP     r2, #0
   BNE     sem_dopost
   CMP     r0, #1
   DMB
   BGE     sem_signal_up
   MOVS    r0, #0
   BX      lr
sem_signal_up:
   MOVS    r0, #1
   BX      lr


#ifndef LOCKS_H_INCLUDED
#define LOCKS_H_INCLUDED
#define MAX_LISTENERS 4

struct semaphore {
    uint32_t value;
    uint8_t listeners[MAX_LISTENERS];
};

typedef struct semaphore semaphore;
typedef semaphore mutex;

int sem_trywait(semaphore *s);
int sem_dopost(semaphore *s);

static inline int sem_init(semaphore *s, int val)
{
    int i;
    s->value = val;
    for (i = 0; i < MAX_LISTENERS; i++)
        s->listeners[i] = 0;
    return 0;
}

#define mutex_init(m) sem_init(m, 1)
#define mutex_trylock(m) sem_trywait(m)

#endif

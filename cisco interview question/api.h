#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>

#define TOTAL_BUF 256
#define INT_MAX 32767
#define INT_MIN -32768
#define SLEEP_TIME 0

//sem_t s1;
//sem_t s2;

static int writer = 1;
static int reader = 0;


struct trie {
        char *value;
        struct trie *level[TOTAL_BUF];
        enum {FALSE, TRUE} flag;
};


int add(struct trie **, char *, char *);
char* display(struct trie *, char *);
int delete(struct trie **, char *);
void action(char, struct trie **, long, char *);
void lookupkey(struct trie *, long);
void lookupvalue(struct trie *, char *);
void insert(struct trie **, long, char *);
void rembykey(struct trie **, long);
void rembyvalue(struct trie **, char *);

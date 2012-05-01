#include <stdio.h>

void swap(int *, int *);
void swap1(int *, int *);

int main() {
	int i = 10, j = 20;
	swap1(&i,&j);
	printf("i = %d, j = %d \n",i,j);
}

void swap(int *i, int *j) {
	if(*i != *j) 
		*i ^= *j ^= *i ^= *j;
}

void swap1(int *i, int *j) {
	*i = *i + *j;
	*j = *i - *j;
	*i = *i - *j;
}

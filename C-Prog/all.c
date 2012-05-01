#include <stdio.h>
#include <stdlib.h>

void main() {
	int *ptr, num,i =0;
	printf("Enter the size : ");
	scanf("%d",&num);

	ptr = (int *)calloc(num, sizeof(int));
	for(i = 0;i<num;i++) {
		*(ptr + i) = num + i;
	}
	for(i = 0;i<num;i++) {
		printf("%d ",*(ptr+i));
	}
	printf("\n");
}

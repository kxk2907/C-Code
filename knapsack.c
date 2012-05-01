#include <stdio.h>
#include <stdlib.h>

int main() {
	int weight[3] = {2,8,6};
	int cost[3] = {100,200,300};
	int W = 10;
	int knapsack[4][11];
	int i = 0,j =0;

	for(i = 0;i<4;i++) {
                for(j = 0;j<11;j++) {
                	knapsack[i][j] = 0;
		}
        }


	for(j = 1;j<11;j++) {
		for(i = 1;i<4;i++) {
			knapsack[i][j] = knapsack[i-1][j];
			if(weight[i-1] <= j && (knapsack[i-1][j-weight[i-1]] + cost[i-1])> knapsack[i][j]) {
				knapsack[i][j] = knapsack[i-1][j-weight[i-1]] + cost[i-1];
			}
		}
	}
	

	for(i = 0;i<4;i++) {
		for(j = 0;j<11;j++) {
			printf("%d\t",knapsack[i][j]);
		}
		printf("\n");
	}
	printf("\n");
	return 0;
}

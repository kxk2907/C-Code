#include <stdio.h>
#include <stdlib.h>

#define MAX(X,Y)         ((X < Y) ?  Y : X)

struct node {
	struct node *left,*right;
	int num;
};

void add(struct node **,int);
void display(struct node *);

void main() {
        struct node *T = NULL;
        add(&T,5);
        add(&T,3);
        add(&T,7);
        add(&T,2);
        add(&T,4);
        add(&T,6);
        add(&T,8);
        add(&T,1);
        add(&T,10);
        add(&T,9);
        add(&T,13);
        add(&T,12);
        add(&T,11);
        display(T);
	printf("\n");
	printf("%d\n",MAX(11,10));
	printf("%d\n",MAX(10,11));
	printf("%d\n",MAX(10,10));
}

void add(struct node **tree,int num) {
	struct node *temp = *tree;
	if(*tree == NULL) {
		*tree = malloc(sizeof(struct node));
		(*tree)->left = NULL;
		(*tree)->right = NULL;
		(*tree)->num = num;
	}
	else {
		if(num <= temp->num) {
			add(&((*tree)->left),num);
		}
		else {
			add(&((*tree)->right),num);
		}
	}
}

void display(struct node *tree) {
	if(tree != NULL) {
		display(tree->left);
		printf("%d ",tree->num);
		display(tree->right);
	}
}

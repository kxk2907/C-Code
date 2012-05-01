#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

struct node {
	struct node *left;
	struct node *right;
	int num;
};

void add(struct node **,int);
void display(struct node *);
void delete(struct node **, struct node **, int);
int checker(struct node *,int,int);
int pathSum(struct node *,int,int);
void interchange(struct node **);
int count(struct node *,int);

int main() {
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
	interchange(&T);
	display(T);
	printf("\n");
	printf("Count :: %d\n",count(T,0));
}


void interchange(struct node **T) {
	struct node *temp;
	if(*T != NULL) {
		temp = (*T)->left;
		(*T)->left = (*T)->right;
		(*T)->right = temp;
		interchange(&((*T)->left));
		interchange(&((*T)->right));
	}
}

int pathSum(struct node *T, int current, int sum) {
	if(T == NULL) {
		printf("\n");
		if (sum == current) 
			return 1;
		else 
			return 0;
	}
	else {
		printf("%d ",T->num);
		return pathSum(T->left,current+T->num,sum)
			|| pathSum(T->right,current+T->num,sum);
	}
}

int checker(struct node *T, int min, int max) {
	if(T == NULL)
		return 1;
	if(T->num < min || T->num > max) {
		printf("Doesn't comply !!\n");
		return 0;
	}
	return checker(T->left,min,T->num) && 
	       checker(T->right,T->num+1,max);
}

void add(struct node **T, int num) {
	struct node *trav = *T;
	if(*T == NULL) {
		(*T) = malloc(sizeof(struct node));
		(*T)->left = NULL;
		(*T)->right = NULL;
		(*T)->num = num;
	}
	else {
		if(num <= trav->num) 
			add(&(trav->left),num);
		else 
			add(&(trav->right),num);
	}
}

void delete(struct node **root, struct node **parent, int num) {
	struct node *current = *root;
	struct node *temp = *root;
	if(*root == NULL) {
		printf("Not found !!\n");
		return;
	}
	else {
		if((*root)->num == num) {
			if((*root)->left == NULL && (*root)->right == NULL) {
				*root = NULL;
				return;
			}
			else if ((*root)->left == NULL){
				if((*parent)->num >= (*root)->num) 
					(*parent)->left = (*root)->right;
				else 
					(*parent)->right = (*root)->right;
				return;
			}
			else if ((*root)->right == NULL) {
				if((*parent)->num >= (*root)->num)
                                        (*parent)->left = (*root)->left;
                                else
                                        (*parent)->right = (*root)->left;
				return;
			}
			else {
				temp = temp->right;
				if(temp->left == NULL) {
					temp->left = (*root)->left;
					*root = temp;
					return;
				}
				while(temp->left != NULL) 
					temp = temp->left;
				(*root)->num = temp->num;
				return delete(&(*root)->right,root,temp->num);	
			}
		}
		else {
			if(num <= (*root)->num) 
				return delete(&((*root)->left),root,num);
			else 
				return delete(&((*root)->right),root,num);
		}
	}
}

void display(struct node *root) {
	if(root != NULL) {
		display(root->left);
		printf("%d ",root->num);
		display(root->right);
	}
}

void roottoleaf (struct node *root, stack *s) {
	if(root != NULL) {
		push(&s,root->data);
		if(root->left == NULL && root->right == NULL) {
			printstack(s);
			pop(&s);
		}
		roottoleaf(root->left, s);
		roottoleaf(root->right, s);
	}
}







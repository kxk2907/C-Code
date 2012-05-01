#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

struct node {
	struct node *left;
	struct node *right;
	int data; 
};

struct stack {
	struct stack *next;
	int data;
};

//tree 
int delete(struct node **, int);
void insert(struct node **, int);
void display(struct node *);
int count(struct node *,int);
int depth(struct node *);
void findPaths(struct node *, struct stack *);
void mirror(struct node **);
void duplicate(struct node *);
int sametree(struct node *, struct node *);
int checktree(struct node *,int,int);
void ancestor(struct node *,int,int);
void buildpath(struct node *, struct stack **, int);
struct node * copy(struct node *);
void serialize(struct node *);
int countleaves(struct node *);
int summupleaves(struct node **);
void treetodoublelist(struct node *, struct node **, struct node **);
void displaydoublelist(struct node *);
void arraytotree(struct node **, int[],int,int);
void symmetricstructure(struct node *);
int diameter(struct node *);
int height(struct node *);

//stack
void push(struct stack **,int);
void pop(struct stack **);
int printSum(struct stack *);

int global = 0;

int main() {
	struct node *T = NULL;
	struct node *Tcopy = NULL;
	struct stack *S = NULL;
	int ret;
	insert(&T,6);
	insert(&T,3);
	insert(&T,9);
	insert(&T,1);
	insert(&T,5);
	insert(&T,7);
	insert(&T,11);
	insert(&T,0);
	insert(&T,2);
	insert(&T,4);
	insert(&T,8);
	insert(&T,10);
	insert(&T,12);
	display(T);
	printf("\n");
	printf("Counter :: %d\n",count(T,0));
	//ret = delete(&T,6);
	if(ret == 1) 
		printf("Deleted\n");
	else 
		printf("Not found\n");
	display(T);
	printf("\n");
	printf("Counter :: %d\n",count(T,0));
	printf("Depth :: %d\n",depth(T));
	findPaths(T,S);
	//mirror(&T);
	//display(T);
	//printf("\n");
	//duplicate(T);
	//display(T);
	//printf("\n");
	ret = checktree(T,INT_MIN,INT_MAX);
	if(ret == 1) 
		printf("Its a b tree \n");
	else 
		printf("Not a btree \n");
	ancestor(T,-1,-2);	
	Tcopy = copy(T);
	display(Tcopy);
	printf("\n");
	serialize(T);
	printf("Total leaves : %d\n",countleaves(T));
	//printf("Grand sum :: %d\n",summupleaves(&T));
	//display(T);
	//printf("\n");
	struct node *head = NULL, *tail = NULL;
	treetodoublelist(T,&head,&tail);
	displaydoublelist(head);
	int array[12] = {0,1,2,3,4,5,6,7,8,9,10,11};
	struct node *ABT = NULL;
	arraytotree(&ABT, array, 0, 11);
	display(ABT);
	printf("\n");
//	insert(&T, 13);
//	insert(&T, 8);
//	insert(&T, 8);
//	insert(&T, 8);
//	insert(&T, 8);
//	insert(&T, 8);
//	insert(&T, 8);
//	insert(&T, 13);
//	insert(&T, 13);
//	insert(&T, 13);
//	insert(&T, 13);
//	insert(&T, 13);
	printf("Diameter :: %d\n",diameter(T));
	return 0;
}

int diameter(struct node *root) {
	int lh = 0, rh = 0, ld = 0 , rd = 0;
	if(root != NULL) {
		lh = height(root->left);
		rh = height(root->right);

		ld = diameter(root->left);
		rd = diameter(root->right);

		if( (lh + rh ) >= (ld + rd))
			return (lh + rh + 1 );
		else 
			return (ld + rd);
	}
	else return 0;
}

int height(struct node *root) {
	int hl = 0, hr = 0;
	if(root != NULL) {
		if((hl = height(root->left)) >= (hr = height(root->right)))
			return hl + 1;
		else 
			return hr + 1;
	}
	return 0;
}

void inorderstack(struct node *root, struct stack **head) {
	if(root != NULL) {
		inorderstack(root->left, head);
		push(head, root->data);
		inorderstack(root->right, head);
	}
	else 
		push(head, -100);
}

void arraytotree(struct node **root, int array[], int left, int right) {
	if(left <= right) {
		int mid = (left + right)/2;
		insert(root,array[mid]);
		arraytotree(root, array, left, mid - 1);
		arraytotree(root, array, mid + 1, right);
	}
}

void displaydoublelist(struct node *head) {
	while(head != NULL) {
		printf("%d ",head->data);
		head = head->right;
	}
	printf("\n");
}

void treetodoublelist(struct node *root, struct node **head, struct node **tail) {
	struct node *temp = NULL;
	if(root != NULL) {
		treetodoublelist(root->left,head,tail);
		if(*head == NULL && *tail == NULL)  {
			*head = (struct node *)malloc(sizeof(struct node));
			(*head)->left = NULL;
			(*head)->right = NULL;
			(*head)->data = root->data;
			*tail = *head;
		}
		else {
			temp = (struct node *)malloc(sizeof(struct node));
			temp->left = (*tail);
			temp->right = NULL;
			temp->data = root->data;
			(*tail)->right = temp;
			*tail = temp;
		}
		treetodoublelist(root->right,head,tail);
	}
}

int summupleaves(struct node **root) {
	int sum = 0, left = 0, right = 0;
	if(*root != NULL) {
		left = summupleaves(&((*root)->left));
		right = summupleaves(&((*root)->right));
		sum = (*root)->data + left + right;
		(*root)->data = sum;
		return sum;
	}
	else 
		return 0;
}

int countleaves(struct node *root) {
	static int leaf = 0;
	if(root != NULL) {
		if(root->left == NULL && root->right == NULL)
			leaf++;
		countleaves(root->left)	;
		countleaves(root->right);
	 }
	 return leaf;
}

void serialize(struct node *root) {
	struct node *queue[100];
	int i = 0, iter1 = 1, iter2 = 1;

	for(i = 1;i<100;i++) 
		queue[i] = NULL;
	
	queue[0] = root;

	while(root != NULL)  {
		if(root->left != NULL) 	
			queue[iter1++] = root->left;
		if(root->right != NULL) 
			queue[iter1++] = root->right;
		root = 	queue[iter2++];
	}
	iter1 = 0;
	while((root = queue[iter1++]) != NULL)
		printf("%d ",root->data);
	printf("\n");	
}

struct node * copy(struct node *root) {
	struct node *local = NULL;
	if(root != NULL) {
		local = (struct node *)malloc(sizeof(struct node));
		local->data = root->data;
		local->left = copy(root->left);
		local->right = copy(root->right);
	}
	return local;
}


void ancestor(struct node *root, int num1, int num2) {
	struct stack *beg1 = NULL, *beg2 = NULL;
	int common = -1;
	buildpath(root,&beg1,num1);
	buildpath(root,&beg2,num2);
	printSum(beg1);
	printSum(beg2);
	while(beg1 != NULL && beg2 != NULL) {
		if(beg1->data != beg2->data) {
			printf("Common ancestor :: %d\n",common);
			return;
		}
		else {
			common = beg1->data;
		}
		pop(&beg1);
		pop(&beg2);
	}
	printf("Common ancestor :: %d\n",common);
}

void buildpath(struct node *root, struct stack **beg, int data) { 
	if(root != NULL) {
		if(data < root->data) 
			buildpath(root->left, beg, data);
		else if(data > root->data) 
			buildpath(root->right, beg, data);
		push(beg,root->data);
	}
}

int checktree(struct node *root, int min, int max) {
	int ret = 0;
	if(root != NULL) {
		if(root->data >= min && root->data < max) 
			ret = 1;
		return ret && checktree(root->left,min,root->data + 1)   //+1 just to ensure to check for boundary of duplicates
			&& checktree(root->right,root->data, max);
	}
	else 
		return 1;
}

int sametree(struct node *root, struct node *root1) {
	int ret = 0;
	if(root != NULL && root1 != NULL) {
		if(root->data == root1->data) 
			ret = 1;
		else 
			ret = 0;
		return ret && sametree(root->left, root1->left) && 
			sametree(root->right, root1->right);
	}
	else if(root != NULL || root1 != NULL) {
		return 0;
	}
	else {
		return 1;
	}
}

void duplicate(struct node *root) {
	if(root != NULL) {
		duplicate(root->left);
		insert(&root,root->data);
		duplicate(root->right);
	}
}


void mirror(struct node **root) {
	struct node *temp;
	if(*root != NULL) {
		temp = (*root)->left;
		(*root)->left = (*root)->right;
		(*root)->right = temp;
		mirror(&((*root)->left));
		mirror(&((*root)->right));
	}
}

void findPaths(struct node *root, struct stack *beg) {
	if(root != NULL) {
		push(&beg,root->data);
		findPaths(root->left,beg);
		findPaths(root->right,beg);
		if(root->left == NULL && root->right == NULL) 
			printf("Path :: %d\n",printSum(beg));
	}
	else {
		pop(&beg);
	}
}

int printSum(struct stack *beg) {
	int sum = 0;
	while(beg != NULL) {
		printf("%d ",beg->data);
		sum += beg->data;
		beg = beg->next;
	}
	printf("\n");
	return sum;
}

void pop(struct stack **beg) {
	if(*beg != NULL) 
		*beg = (*beg)->next;
}

void push(struct stack **beg, int data) {
	struct stack *temp;
	if(*beg == NULL) {
		*beg = (struct stack *)malloc(sizeof(struct stack));
		(*beg)->next = NULL;
		(*beg)->data = data;
	}
	else {
		temp = malloc(sizeof(struct stack));
		temp->data = data;
		temp->next = *beg;
		*beg = temp;
	}
}

int depth(struct node *root) {
	int depthl, depthr;
	if(root != NULL) {
		depthl = depth(root->left);
		depthr = depth(root->right);
		if(depthl <= depthr) 
			return (depthr + 1);
		else 
			return (depthl + 1);
	}	
}

int count(struct node *root, int counter) {
	if(root != NULL) {
		counter = count(root->left,counter);
		++counter;
		counter = count(root->right,counter);
	}
	return counter;
}


int delete(struct node **root, int data) {
	struct node *temp;
	if(*root == NULL) 
		return -1;
	else {
		if((*root)->data == data) {
			if((*root)->left == NULL && (*root)->right == NULL) {
				(*root) = NULL;
				return 1;
			}
			else if((*root)->left == NULL) {
				(*root) = (*root)->right;
				return 1;
			}
			else if((*root)->right == NULL) {
				(*root) = (*root)->left;
				return 1;
			}
			else {
				temp = (*root)->right;
				while(temp->left != NULL) 
					temp = temp->left;
				(*root)->data = temp->data;
				return delete(&((*root)->right),temp->data);
			}
		}
		else {
			if(data < (*root)->data) 
				return delete(&((*root)->left),data);
			else
				return delete(&((*root)->right),data);
		}	
	}
}


void insert(struct node **root, int data) {
	if(*root == NULL) {
		void * v = malloc(sizeof(struct node));
		while((int)v % 1000 != 0) {
			v = malloc(sizeof(struct node));
		}
		printf("%d\n",(int)v);
		*root = (struct node *)v;
		(*root)->left = NULL;
		(*root)->right = NULL;
		(*root)->data = data;
	}
	else {
		if(data <= (*root)->data) 
			insert(&((*root)->left),data);
		else 
			insert(&((*root)->right),data);
	}
}

void display(struct node *root) {
	if(root != NULL) {
		display(root->left);
		printf("%d ",root->data);
		display(root->right);
	}
}

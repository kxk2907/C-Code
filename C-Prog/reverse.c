#include<stdio.h>
#include<stdlib.h>

struct node {
	int num;
	struct node *next;
};

void add(struct node **, struct node **, int);
void display(struct node *);
void reverse(struct node **, struct node **);
struct node *reverse1(struct node **);
struct node *merge(struct node *, struct node *);
struct node *mergeSort(struct node *);


int main() {
	struct node *head = NULL, *tail = NULL;
	add(&head,&tail,1);
	add(&head,&tail,2);
	add(&head,&tail,3);
	add(&head,&tail,4);
	add(&head,&tail,5);
	add(&head,&tail,6);
	display(head);
	reverse(&head,&tail);
	display(head);
	//head = reverse1(&head);
	//display(head);
	head = mergeSort(head);
	display(head);
}

struct node* merge(struct node *left, struct node *right) {
	struct node *temp;
	if(left == NULL && right == NULL) {
		return NULL;
	}
	else if(left == NULL) {
		return right;
	}
	else if(right == NULL) {
		return left;
	}
	else {
		if(left->num <= right->num) {
			temp = left;
			temp->next = merge(left->next, right);
		}
		else {
			temp = right;
			temp->next = merge(left, right->next);
		}
	}
	return temp;
}

struct node* mergeSort(struct node *head) {
	struct node *tempright= head, *temp1 = head, *temp2 = NULL, *templeft = head;
	if(tempright == NULL || tempright->next == NULL) {
		return head;
	}
	while(temp1 != NULL || temp1->next != NULL) {
		temp1 = temp1->next->next;
		temp2 = tempright;
		tempright = tempright->next;
	}
	temp2->next = NULL;
	return merge(mergeSort(templeft),mergeSort(tempright));
}

void reverse(struct node **head, struct node **tail) {
	struct node *p = *head;
	struct node *q = (*head)->next;
	struct node *r;
	p->next = NULL;
	*tail = p;
	while(q->next != NULL) {
		r = q->next;
		q->next = p;
		p = q;
		q = r;
	}
	q->next = p;
	*head = q;
}

struct node * reverse1(struct node **head) {
	struct node *current = *head;
	struct node *ret;
	if(current->next != NULL) {
		ret = reverse1(&(current->next));
		current->next->next = current;
	}
	else {
		*head = current;
		ret = *head;
	}
	return ret;
}

void add(struct node **head, struct node **tail, int num) {
	struct node *temp;
	temp = malloc(sizeof(struct node));
	temp->num = num;
	temp->next = NULL;
	if(*head == NULL && *tail == NULL) {
		*head = temp;
		*tail = temp;
	}
	else {
		(*tail)->next = temp;
		*tail = temp;
	}
}

void display(struct node *head) {
	struct node *current = head;
	while(current != NULL) {
		printf("%d ",current->num);
		current = current->next;
	}
	printf("\n");
}

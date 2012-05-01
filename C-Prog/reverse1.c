#include <stdio.h>
#include <stdlib.h>

struct node {
        int num;
        struct node *next;
};

void add(int);
void display();
void reverse();
struct node * mergesort(struct node *);
struct node * merge(struct node *, struct node *);

struct node *head = NULL, *tail = NULL;

void main() {
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        display();
	reverse();
	display();
	struct node *beg = head;
	beg = mergesort(beg);
	display();
}

struct node * mergesort(struct node *beg) {
	struct node *mid = beg, *temp = beg, *temp1 = NULL, *begg = beg;
	if(beg == NULL || beg->next == NULL)
		return beg;
	while(temp != NULL || temp->next != NULL) {
		temp = temp->next->next;
		temp1 = mid;
		mid = mid->next;
	}
	temp1->next = NULL;
	return merge(mergesort(begg),mergesort(mid));
}

struct node * merge(struct node *left, struct node *right) {
	struct node *ret;
	if(left == NULL && right == NULL)
		return NULL;
	else if(left == NULL) 
		return right;
	else if (right == NULL)
		return left;
	else {
		if(left->num <= right->num)  {
			ret = left;
			ret->next = merge(left->next,right);
		}
		else {
			ret = right;
			ret->next = merge(left, right->next);
		}
	}
	return ret;
}

void reverse() {
	struct node *p = head;
	struct node *q = p->next;
	struct node *r;
	p->next = NULL;
	while(q != NULL) {
		r = q->next;
		q->next = p;
		p = q;
		q = r;
	}
	head = p;
}

void add(int num) {
        struct node *temp;
        temp = malloc(sizeof(int));
        temp->num = num;
        temp->next = NULL;

        if(head == NULL && tail == NULL) {
                head = temp;
                tail = temp;
        }
        else {
                tail->next = temp;
                tail = temp;
        }
}

void display() {
        struct node *temp = head;
        while(temp != NULL) {
                printf("%d ",temp->num);
                temp = temp->next;
        }
	printf("\n");
}

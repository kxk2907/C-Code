#include <stdio.h>
#include <stdlib.h>

struct node {
	char ch;
	struct node *next;
};

void reverse(char [],char []); 
void add(struct node **,struct node **,char );
void adder(char *, struct node **, struct node **);
void display(struct node *);

void main() {
	int i = 0;
	char input[50] = "I am a good boy\0";
	char output[50] = "\0";
	struct node *head = NULL, *tail = NULL;
	reverse(input,output);
	for(i = 0;i<50;i++) 
		printf("%c",output[i]);
	printf("\n");

	adder(input,&head,&tail);
	display(head);	
}

void display(struct node *head) {
	while(head != NULL) 
		printf ("%c",head->ch);
	printf("\n");	
}

void adder(char *ch,struct node **head,struct node **tail) {
	while(ch != '\0')  {
		printf("%c",*ch);
		//add(head,tail,*ch);
		ch++;
	}
	printf("\n");
}

void add(struct node **head, struct node **tail, char ch) {
	struct node *temp;
	temp = malloc(sizeof(struct node));
	temp->ch = ch;
	temp->next = NULL;
	if(*head == NULL && &tail == NULL){
		*head = temp;
		*tail = temp;
	}
	else {
		(*tail)->next = temp;
		*tail = temp;
	}
}

void reverse(char input[],char output[]) {
	int i =0 ,len = 0,start = 0, end = -1 , j = 0, k = 0;
	char temp;
	for(i = 0;i<50;i++) {
		if(input[i] == '\0')  {
			len = i;
		 	break;
		}
	}
	for(i = len-1,j=0;i>=0;i--,j++)
		output[j] = input[i];
	output[len] = '\0';
	for(i = 0;i<len;i++) {
		if(output[i] == ' ' || output[i] == '\0') {
			j = start;
			k = end;
			while(j <= (start + end)/2 ) {
				temp = output[j];
				output[j] = output[k];
				output[k] = temp;
				j++;
				k--;
			}
			start = i + 1;
			end = i ;
		}
		else {
			end++;
		}
	}
}


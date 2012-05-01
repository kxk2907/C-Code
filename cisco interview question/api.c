#include "api.h"

void action(char act, struct trie **root, long key, char value[]) {
	if(act == 'I') {
		insert(root, key, value);
	}
	else if (act == 'L') {
		if(value == NULL) 
			lookupkey(*root, key);
		else 
			lookupvalue(*root, value);
	}
	else if (act == 'D') {
		if(value == NULL)
			rembykey(root, key);
		else 
			rembyvalue(root, value);
	}
}

void rembyvalue(struct trie **root, char value[]) {
	char ch1[1000];
	snprintf(ch1, 0, "%s", value);
	printf("Deleting key :: %s, Value :: %s\n", display(*root, ch1), value);
	delete(root, display(*root, ch1));
	delete(root, value);
}

void rembykey(struct trie **root, long key) {
	char ch[20];
	char ch1[1000];
	char *value;
	if(key >= INT_MAX || key <= INT_MIN) {
                printf ("ERROR :: Invalid range, enter number that fits integer range\n");
                return;
        }
	snprintf(ch, 10, "%d", (int)key);
	value = display(*root, ch);	
	snprintf(ch1, 1000, "%s", value);
	 printf("Deleting key :: %s, Value :: %s\n", ch, ch1);
	delete(root, ch);
	delete(root, ch1);
}

void lookupvalue(struct trie *root, char value[]) {
	char ch[1000];
	snprintf(ch, 10, "%s", value);
	//sem_post(&s1);
	//sem_wait(&s2);
	writer = 0;
	while(reader == 0);
	printf("Lookup :: %s\n",display(root, ch));
	writer = 1;
	sleep(SLEEP_TIME);
}

void lookupkey(struct trie *root, long key) {
	char string_key[20];
	if(key >= INT_MAX || key <= INT_MIN) {
                printf ("ERROR :: Invalid range, enter number that fits integer range\n");
                return;
        }
	snprintf(string_key, 10, "%d", (int)key);
	//sem_post(&s1);
	//sem_wait(&s2);
	writer = 0;
	while(reader == 0);
	printf ("Lookup :: %s\n",display(root, string_key));
	writer = 1;
	sleep(SLEEP_TIME);
}

void insert(struct trie **root, long key, char *value) {
	char string_key[20];
	int ret1, ret2;
	if(key >= INT_MAX || key <= INT_MIN) {
		printf ("ERROR :: Invalid range, enter number that fits integer range\n");
		return;
	}
	snprintf(string_key, 10, "%d", (int)key);
	reader = 0;
	while(writer == 0);
	//sem_wait(&s1);
	ret1 = add(root, string_key, value);
	printf("Inserted Key : %d, Value : %s\n", (int)key, value);
	ret2 = add(root, value, string_key);
	reader = 1;
	//sem_post(&s2);
	if(ret1 == 1 && ret2 == 1) 
		printf("Inserted successfully\n");
	else 
		printf("ERROR: Insert failure\n");
	sleep(SLEEP_TIME);	
}

int add(struct trie **root, char *ch, char *value) {
        int item = *ch;
        if((*root)->level[item] == NULL) {
                (*root)->level[item] = (struct trie *)malloc(sizeof(struct trie));
                (*root)->level[item]->flag = FALSE;
        }
        if(*(ch+1) == '\0') {
                (*root)->level[item]->flag = TRUE;
		(*root)->level[item]->value = malloc(strlen(value) + 1);
		strcpy((*root)->level[item]->value, value);
                return 1;
        }
        return add(&((*root)->level[item]), ++ch, value);
}


char* display(struct trie *root, char *ch) {
        int item;
        while(*(ch+1) != '\0') {
                item = *ch;
                if(root->level[item] == NULL) {
                        return "Invalid item !!";
                }
                root = root->level[item];
                ++ch;
        }
        item = *ch;
        if(root->level[item] != NULL) {
                if(root->level[item]->flag == TRUE) {
			return root->level[item]->value;
		}	
                else
        		return "Junk is present !!";
	}
        else {
                return "Invalid item !!";
        }
}

int delete(struct trie **root, char *ch) {
        int item = *ch;
        int ret = 0;
        int i;
        if((*root)->level[item]) {
                if(*(ch + 1) == '\0') {
                        for(i = 0;i<TOTAL_BUF;i++) {
                                if((*root)->level[item]->level[i])
                                        return -1;
                        }
                        (*root)->level[item] = NULL;
                        free((*root)->level[item]);
                        return 1;
                }
                else {
                        ret = delete(&((*root)->level[item]), ++ch);
                        if(ret == 1) {
                                for(i = 0;i<TOTAL_BUF;i++) {
                                        if((*root)->level[item]->level[i])
                                                return -1;
                                }
                                if((*root)->level[item]->flag == 1) {
                                        return -1;
                                }
                                (*root)->level[item] = NULL;
                                free((*root)->level[item]);
                                return 1;
                        }
                        else
                                return -1;
                }
        }
        else {
                return -1;
        }
}


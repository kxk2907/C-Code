#include <stdio.h>
#include <stdlib.h>

#include "api.h"

int main() {
	struct trie *root = (struct trie *)malloc(sizeof(struct trie));
	char ch[] = "hello\0";
	FILE* spIn;
	int filekey;
	char filevalue[33];

	printf("Test case : 1\n");
	printf("=============\n");
	printf("Adding a key : 4567 value : hello\n");
	action('I', &root,4567, ch);
	printf("Lookup the key and value entering key and value \n");
	action('L', &root,4567, NULL);
	action('L', &root,0,ch);
	printf("\n\n\n");

	printf("Test case : 2\n");
	printf("=============\n");
	printf("Deleting based on key : 4567\n");
	action('D', &root,4567, NULL);
	printf("Lookup the key and value entering key and value \n");
	action('L', &root,4567, NULL);
	action('L', &root,0,ch);
	printf("\n\n\n");

	printf("Test case : 3\n");
	printf("=============\n");
	printf("Adding again to check, key : 4567 value : hello\n");
        action('I', &root,4567, ch);
        printf("Lookup the key and value entering key and value \n");
        action('L', &root,4567, NULL);
        action('L', &root,0,ch);
        printf("\n\n\n");	

        printf("Test case : 4\n");
        printf("=============\n");
        printf("Deleting based on value : hello\n");
        action('D', &root,0, &ch[0]);
        printf("Lookup the key and value entering key and value \n");
        action('L', &root,4567, NULL);
        action('L', &root,0,ch);
        printf("\n\n\n");
	
	printf("Test case : 5\n");
	printf("=============\n");
	printf("Adding values \"hello\" and \"hell\" : Same path testing\n");
	action('I', &root, 1234, "hello\0");
	action('I', &root, 4567, "hell\0");
	printf("Deleting only \"hello\", leaving \"hell\" intact\n");
	action('D', &root, 0, "hello\0");
	action('L', &root, 0, "hell\0");
	action('D', &root, 0, "hell\0");
        printf("\n\n\n");
	
	printf("Test case : 6\n");
	printf("=============\n");
	printf("Adding key : 12345 value : hello");
	action('I', &root, 12345, "hello\0");
	printf("Modifying the value : world");
	action('I', &root, 12345, "world\0");
	action('L', &root, 0, "world\0");
	action('L', &root, 12345, NULL);
	printf("Cleanup\n");
	action('D', &root, 12345, NULL);
	printf("\n\n\n");

	printf("Test case : 7\n");
	printf("=============\n");
	printf("Integer boundary checking key : 11111111\n");
	action('I', &root, 11111111, "test\0");
	printf("\n\n\n");

	printf("Test case : 8\n");
	printf("=============\n");
	printf("Reading large set of inputs from file and adding and deleting\n");
	if ((spIn = fopen("input", "r")) == NULL)
	      printf("Could not open input file!");
	while (fscanf(spIn,"%d\t%s", &filekey, filevalue)!=EOF) {
		action('I', &root, filekey, filevalue);
	    //  printf("Reading :: %d  %s\n", key, value);
	}
	fclose(spIn);
	if ((spIn = fopen("input", "r")) == NULL)
	              printf("Could not open input file!");
	while (fscanf(spIn,"%d\t%s", &filekey, filevalue)!=EOF) {
		action('L', &root, filekey, NULL);
	}
	fclose(spIn);
	if ((spIn = fopen("input", "r")) == NULL)
	              printf("Could not open input file!");
	while (fscanf(spIn,"%d\t%s", &filekey, filevalue)!=EOF) {
		action('D', &root, filekey, NULL);
	}
	fclose(spIn);
	if ((spIn = fopen("input", "r")) == NULL)
                      printf("Could not open input file!");
        while (fscanf(spIn,"%d\t%s", &filekey, filevalue)!=EOF) {
                action('L', &root, filekey, NULL);
        }
        fclose(spIn);



//	printf("first time \n");
//	action('I', &root,4567, ch);
	
	//before
//	action('L', &root,4567, NULL);
//	action('L', &root,0,ch);
/*	
	action('D', &root,4567, NULL);

	//after
	action('L', &root,4567, NULL);
	action('L', &root,0,ch);

	printf("second time \n");
	action('I', &root,4567, ch);
	//before
	action('L', &root,4567, NULL);
	action('L', &root,0,ch);

	action('D', &root,0,ch);
	
	//after
        action('L', &root,4567, NULL);
        action('L', &root,0,ch);
*/
}


executable : unit_test.o api.o
	gcc -o executable unit_test.o api.o $(LIB)

api.o : api.c api.h
	gcc $(DEBUG) -c -o api.o api.c $(LIB)

unit_test.o : unit_test.c api.h 
	gcc $(DEBUG) -c -o unit_test.o unit_test.c $(LIB)

clean: 
	rm -rf unit_test.o api.o executable

#LIB = -Wall -pedantic -std=c99 -D_XOPEN_SOURCE=700 -pthread

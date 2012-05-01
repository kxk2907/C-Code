# Karthikeyan Karur Balu (kxk2907)

Allfiles = Scanner.h ScannerException.h SymbolTable.h Scanner.cpp ScannerException.cpp SymbolTable.cpp tli.cpp
AllObject = Scanner.o ScannerException.o SymbolTable.o tli.o core

tli: tli.o Scanner.o ScannerException.o SymbolTable.o
	g++ -o tli tli.o Scanner.o ScannerException.o SymbolTable.o

tli.o : tli.cpp Scanner.h ScannerException.h
	g++ $(DEBUG) -c -o tli.o tli.cpp

Scanner.o : Scanner.cpp Scanner.h ScannerException.h
	g++ $(DEBUG) -c -o Scanner.o Scanner.cpp

ScannerException.o : ScannerException.cpp ScannerException.h Scanner.h
	g++ $(DEBUG) -c -o ScannerException.o ScannerException.cpp 

SymbolTable.o : SymbolTable.cpp SymbolTable.h
	g++ $(DEBUG) -c -o SymbolTable.o SymbolTable.cpp 

Scanner.h : ScannerException.h SymbolTable.h
	touch Scanner.h

Scanner.cpp : Scanner.h ScannerException.h
	touch Scanner.cpp

ScannerException.cpp : Scanner.h ScannerException.h
	touch ScannerException.cpp

SymbolTable.cpp : SymbolTable.h
	touch SymbolTable.cpp

tli.cpp : Scanner.h ScannerException.h
	touch tli.cpp

clean : 
	rm -r $(AllObject) 2> /dev/null

realclean :
	rm -r tli tli.ps tli.zip $(AllObject) 2> /dev/null

tli.ps : $(Allfiles)
	 enscript --color -Ecpp -p tli.ps $(Allfiles)

print : tli.ps
	lp tli.ps

tli.zip: $(Allfiles) Makefile
	zip tli.zip $(Allfiles) Makefile
	

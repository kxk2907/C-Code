# Karthikeyan Karur Balu (kxk2907)

# $Id: Makefile,v 1.5 2012/03/29 21:52:17 kxk2907 Exp $
# $Header: /home/stu13/s8/kxk2907/practice/sdt/lab1/RCS/Makefile,v 1.5 2012/03/29 21:52:17 kxk2907 Exp $
# $Date: 2012/03/29 21:52:17 $
# $Revision: 1.5 $
# $Author: kxk2907 $

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

ScannerException.cpp : Scanner.h ScannerException.h

SymbolTable.cpp : SymbolTable.h

tli.cpp : Scanner.h ScannerException.h

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
	

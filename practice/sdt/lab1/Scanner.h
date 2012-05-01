// Scanner.h
// Author: James Heliotis
// Contributors: {}

// Description: Scans *and*interprets* tiny language programs

#ifndef RITCS_SCANNER_H
#define RITCS_SCANNER_H

#include <iostream>
#include "SymbolTable.h"
#include "ScannerException.h"

namespace RITCS {

class Scanner {
public: // creation

	// constructor
	//
	// Initialize the scanner to an "empty" state
	//
	Scanner();

	// readAndInterpret
	//
	// Read a Tiny Language (TL) program from the given file
	// and execute it.
	// Arguments:	inFile:	the source program
	// Pre:	inFile is open (not checkable for all types of streams)
	//
	void readAndInterpret( std::istream &inFile )
		 throw ( ScannerException );

	// postMortem
	//
	// Dump out the final values of all the variables in
	// this format:
	//	<empty line>
	//	Post-Mortem:
	//	============
	//	<do a SymbolTable dump>
	// Pre:	readAndInterpret has been called
	//
	void postMortem();

	// execute
	//
	// takes a command and up to two variables and performs the
	// correct action.
	//
	void execute( char *command, char *var1, char *var2 )
		 throw ( ScannerException );

	// read_string
	//
	// reads in a string from an input stream
	char *read_string( std::istream &in );

private: // storage

	SymbolTable table;

private: // parameters

	static const int BufferIncrement = 20;

}; // Scanner

}

#endif

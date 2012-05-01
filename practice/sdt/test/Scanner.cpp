// $Id$
// Author: James Heliotis
// Contributors: {Jeremiah Brazeau}

// Description: Scans *and*interprets* tiny language programs

// Revisions:
//	$Log$
//

#include <cstdlib>
#include <cstring>
#include <cassert>
#include <iostream>
#include <iomanip>
#include <limits>

#include "Scanner.h"
#include "ScannerException.h"

using namespace std;

namespace RITCS {

static const char *Declare = "declare";
static const char *Set = "set";
static const char *Print = "print";
static const char *End = "end";

// constructor
//
Scanner::Scanner(): table() {
}

// readAndInterpret
//
void Scanner::readAndInterpret( istream &inFile ) throw ( ScannerException ) {
    char *command( 0 );
    char *op1( 0 ), *op2( 0 );
    try {

	bool endFound( false );
	while( !inFile.eof() && !inFile.fail() && !endFound ) {

		command = read_string( inFile );
		if ( !inFile.eof() && !inFile.fail() ) {

			if( strcmp( command, Set ) == 0 ) {
				execute( command, op1=read_string( inFile ),
					  	op2=read_string( inFile ) );
				delete[] op1; op1 = 0;
				delete[] op2; op2 = 0;
			}
			else if( strcmp( command, Declare ) == 0 ||
		    	strcmp( command, Print ) == 0 ){
				execute( command, 
				 	op1=read_string( inFile ), 0 );
				delete[] op1; op1 = 0;
			}
			else if( strcmp( command, End ) == 0 ){
				endFound = true;
			}
			else{
				throw ScannerException(
				  ScannerException::illegalCommand, command );
			}
			delete[] command; command = 0;
		}
	}
	if ( !endFound ) {
		throw ScannerException( ScannerException::prematureEOF );
	}
	if ( command ) {
	    delete[] command;
	    command = 0;
	}
    }
    catch ( ... ) {
    	if ( op1 != 0 ) delete[] op1;
    	if ( op2 != 0 ) delete[] op2;
    	if ( command != 0 ) delete[] command;	
	throw;
    }

}


void Scanner::execute(
    char *command, char *var1, char *var2 )
     throw ( ScannerException ) {

	if( strcmp( command, Declare ) == 0 ){
		if( !table.contains( var1 ) ){
			table.declare( var1 );
		}
		else{
			throw ScannerException(
				ScannerException::redeclaredVariable, var1 );
		}
	}
	else if( strcmp( command, Set ) == 0 ){
		if( table.contains( var1 ) ){
			int v = atoi( var2 );
			table.set( var1, v );
		}
		else{
			throw ScannerException(
				ScannerException::undeclaredVariable, var1 );
		}
	}
	else if( strcmp( command, Print ) == 0 ){
		if( table.contains( var1 ) ){
			cout << table.get( var1 ) << endl;
		}
		else{
			throw ScannerException(
				ScannerException::undeclaredVariable, var1 );
		}
	}
	else{
		assert( false );
	}

}


char *Scanner::read_string( istream &in ){
	char ch;
	int len( Scanner::BufferIncrement );
	char *buffer( new char[len] );
	int used(0);

	in >> ws;
	if( in.fail() || in.eof() ){
		in.setstate( ios::failbit );
	}
	else{
		while( in.get( ch ) && ch != ' ' && ch != '\n' &&
		           ch != '\r' && ch != '\t' && !in.fail() ){
			if( used + 1 >= len ){
				len += Scanner::BufferIncrement;
				char *bigger( new char[len] );
				buffer[used] = '\0';
				strcpy( bigger, buffer );
				delete [] buffer;
				buffer = bigger;
			}

			buffer[ used++ ] = ch;
		}

		buffer[ used ] = '\0';

		if( in ){
			in.putback( ch );
		}
	}

	return buffer;
}


// postMortem
//
void Scanner::postMortem() {
	cout << endl;
	cout << "Post-Mortem:";
	cout << endl;
	cout << "============";
	cout << endl;
	cout << endl;
	table.dump( cout );
}
}

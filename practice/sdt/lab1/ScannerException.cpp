#include <cassert>
#include <iostream>
#include <cstring>

#include "Scanner.h"
#include "ScannerException.h"

using namespace std;

namespace RITCS {

ScannerException::ScannerException( Error errorCode, char *badItem ):
		item( badItem == 0 ? 0 : new char[strlen(badItem)+1] ),
		code( errorCode ) {
	if (item != 0 ) {
		strcpy( item, badItem );
	}
	assert( errorCode == illegalCommand ||
		errorCode == undeclaredVariable ||
		errorCode == redeclaredVariable ||
		errorCode == prematureEOF );
			// just adding a silly mistake check
	assert( badItem == 0 ?
	              item == 0 :
	              strlen(badItem) == strlen(item) );
} // ScannerException

ScannerException::ScannerException( const ScannerException &other ):
	  item( other.item == 0 ? 0 : new char[strlen(other.item) + 1] ),
	  code( other.code ) {
	if (item != 0 ) {
		strcpy( item, other.item );
	}
			// just adding a silly mistake check
	assert( other.item == 0 ?
	              item == 0 :
	              strlen(other.item) == strlen(item) );
}

ScannerException::~ScannerException() {
	if ( item != 0 ) {
		delete item;	// clean up the memory
	}
}

ostream &operator<<( ostream &out, const ScannerException &e ){
	out << "Error: ";
	switch( e.code ){
	case ScannerException::redeclaredVariable:
		out << "Redeclared variable \"" << e.item << "\".";
		break;
	case ScannerException::undeclaredVariable:
		out << "Undeclared variable \"" << e.item << "\".";
		break;
	case ScannerException::illegalCommand:
		out << "Illegal command \"" << e.item << "\".";
		break;
	case ScannerException::prematureEOF:
		out << "Premature end of file.";
		break;
	default:
		assert( false );
	}

	return out;

}

}

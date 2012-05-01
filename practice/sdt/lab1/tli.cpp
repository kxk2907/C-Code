// tli.C
// Author: James Heliotis

// Description: Execute programs written in TL (Tiny Language)

#include <cstdlib>
#include <iostream>
#include "Scanner.h"
#include "ScannerException.h"

using namespace RITCS;

int main() {
	try {
		Scanner scanner;
		scanner.readAndInterpret( std::cin );
		scanner.postMortem();
	}
	catch( ScannerException e ) {
		cerr << e << endl;
	}

	return EXIT_SUCCESS;
}

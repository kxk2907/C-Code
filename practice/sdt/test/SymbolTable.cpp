// $Id$
// Author: James Heliotis
// Contributors: {}
// Description: A symbol table whose symbols are integer variables

// Revisions:
//	$Log$
//

#include <assert.h>

#include <iostream>
#include <string>
#include <map>
#include <algorithm>
#include <vector>
#include <utility>

#include "SymbolTable.h"

namespace RITCS {

using std::string;
using std::map;
using std::vector;
typedef map< string, int >::value_type Pair;

// constructor
//
// Description:	creates an empty symbol table
// Post:	size() is 0
//
SymbolTable::SymbolTable(): variables() {
	assert( size() == 0 );
}

// declare
//
// Description:	Add a newly declared variable to the table
// Arguments:	name: variable name
//		init: initial integer value
// Post:	contains( name )
// Post:	variable named by 'name' has the value of 'init'
//
void SymbolTable::declare( string name, int init ) {
	variables.insert( Pair( name, init ) );

	assert( contains( name ) );
	assert( get( name ) == init );
}

// set
//
// Description:	Change the value of a variable in the table
// Arguments:	name: variable name
// 		val: new integer value
// Pre:		contains( name )
// Post:	contains( name )
// Post:	variable named by 'name' has the value of 'val'
//
void SymbolTable::set( string name, int val ) {
	assert( contains( name ) );

	variables.find( name )->second = val;

	assert( contains( name ) );
	assert( get( name ) == val );
}

// get
//
// Returns:	The value of the variable
// Argument:	name: variable name
// Pre:		contains( name )
// Post:	table is unchanged
//
int SymbolTable::get( string name ) const {
	assert( contains( name ) );

	const Pair result( *variables.find( name ) );

	return result.second;
}

// size
//
// Returns:	the number of variables currently in the table
// Post:	returned value >= 0
// Post:	table is unchanged
//
unsigned int SymbolTable::size() const {
	unsigned int result( variables.size() );

	assert( result >= 0 );
	return result;
}

// contains
//
// Returns:	true iff a variable with given name is in the table
// Argument:	name: name of variable being sought
// Post:	table is unchanged
//
bool SymbolTable::contains( string name ) const {
	return variables.find( name ) != variables.end();
}

// dump
//
// Description: display the contents of the table on the given
//		output stream. Each line contains one variable
//		listing of the form "<variable>: <value>"
// Arguments:	out:	where the dump will be written
// Post:	table is unchanged
//
void SymbolTable::dump( std::ostream &out ) const {
	vector< string > dumper;
		/*
		 * I tried to do this with a copy operation and an
		 * ostream iterator but was unable to get it to
		 * work. -- JEH
		 */
	for ( map< string, int >::const_iterator i = variables.begin();
		i != variables.end();
		i++ ) {
			dumper.push_back( i->first );
	}
	std::sort( dumper.begin(), dumper.end() );
	for ( vector< string >::const_iterator i = dumper.begin();
		i != dumper.end();
		i++ ) {
			out << *i << ": " << get( *i ) << std::endl;
	}
}

} // RITCS


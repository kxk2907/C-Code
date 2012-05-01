# /usr/bin/perl
open (FILE, "<" , "mail.txt") or die "FILE NOT FOUND !!";
my @input = <FILE>;
close FILE;
my @output = grep(/^From:|^To:|^Subject:|^Cc:/,@input);
print (@output);
open (FILE, ">", "mail1.txt") or die "FILE NOT FOUND !!";
print FILE @output;
close FILE;

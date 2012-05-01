use IO::Socket::INET;
use strict;

my $name = shift
    or die "Missing server name\n";
my $port = shift
    or die "Missing port number\n";

my $socket = IO::Socket::INET->new('PeerAddr' => $name,
				   'PeerPort' => $port,
				   'Proto' => 'tcp')
    or die "Can't create socket ($!)\n";
print "Client sending\n";
#while (<STDIN>) {
    my $inp = `cat 123`;
    print $socket $inp;
    print scalar <$socket>;
#}
close $socket
    or die "Can't close socket ($!)\n";

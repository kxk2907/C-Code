use IO::Socket::INET;
use strict;

my $port = shift
    or die"Missing port number\n";

my $socket = IO::Socket::INET->new('LocalPort' => $port,
				   'Proto' => 'tcp',
				   'Listen' => SOMAXCONN)
    or die "Can't create socket ($!)\n";
print "Server listening\n";
while (my $client = $socket->accept) {
    my $name = gethostbyaddr($client->peeraddr, AF_INET);
    my $port = $client->peerport;
    while (<$client>) {
	print "[$name $port] $_";
	print $client "$.: $_";
    }
    close $client
	or die "Can't close ($!)\n";
}
die "Can't accept socket ($!)\n";

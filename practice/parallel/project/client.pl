use strict;
use IO::Socket;

if ($#ARGV < 2 || $#ARGV > 4) {
	print "Usage : perl client.pl <server ip> <register/unregister> <java_location> <pj_location>\n";
	exit(1);
}

my $socket = new IO::Socket::INET (
		PeerAddr  => $ARGV[0],
		PeerPort  =>  5000,
		Proto => 'tcp',
		) or die "Couldn't connect to Server\n";


my $reg = $ARGV[1];
my $user = `who am i`;
   $user =~ s/ .*//g;
   chomp($user);
my $proc = `cat /proc/cpuinfo | grep processor | wc -l`;
   if($proc =~ m/No such file/) {
	print "I am not sure about your processor, so considering default 1 \n";
	$proc = "1";
   }
chomp($proc);
my $hostname = `hostname`;
chomp($hostname);
my $java = $ARGV[2];
my $pj = $ARGV[3];

my $output = $reg." ".$user." ".$proc." ".$hostname." ".$java." ".$pj;
chomp($output);
print $output."\n";
$socket->send($output);
my $input;
$socket->recv($input,1024);
close $socket;
print $input."\n"; 

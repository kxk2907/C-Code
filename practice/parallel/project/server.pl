use strict;
use IO::Socket;
use threads;
use Thread::Queue;

my $socket = new IO::Socket::INET (
		LocalHost => '127.0.0.1',
		LocalPort => '5000',
		Proto => 'tcp',
		Listen => 5,
		Reuse => 1
		);


my $clients = {};
my $queue = Thread::Queue->new;

my $configfile = shift || 'configfile';

while(my $connection = $socket->accept()) {
	my $nonblocking = 1;
	ioctl($connection, 0x8004667e, \\$nonblocking);
	$connection->autoflush(1);
	$clients->{time()} = $connection;
	threads->create("readData",$connection);
}

sub readData {
	my ($client) = @_;
	my $input = '';
	$client->recv($input,1024);
	my $output = processData($input);
	$client->send($output);
	close $client;
}

sub processData {
	my $input = shift;
	my @inp = split(/ /,$input);
	if(scalar(@inp) != 6) {
		print "<ERROR> :: $inp[3] is trying to cheat with less arguments \n"; 
		return "$inp[3], Don't cheat me with less arguments buddy !!";
	}
	my $reg = $inp[0];
	my $user = $inp[1];
	my $proc = $inp[2];
	my $host = $inp[3];
	my $java = $inp[4];
	my $pj = $inp[5];
	my $grep = `grep $host $configfile`;
	
	if($reg =~ m/un/) {
                if($grep =~ m/$host/) {
                        my $gen1 = $user."@".$host;
                        my $gen = `grep \-v $gen1 $configfile` ;
			open FILE, ">$configfile" or return "Oops, I am unable to open the file !!";
			print FILE $gen;
			close FILE;
                        print "<PASS> :: $host, successfully removed from the list !! \n";
                        return "Hurray !! you are relieved from your DUTY !!";
                }
                else {
                        print "<ERROR> :: $host is not a part of grid, but trying to fool you, nice try haha !!\n";
                        return "Hey $host, you are not a part of my grid, nice try !!";
                }
	}
	else {
                if($grep =~ m/$host/ ) {
                        print "<ERROR> :: $host is already part of grid, but trying to fool you, nice try haha !!\n";
                        return "Hey $host, you are a part of my grid already, nice try !!";
                }
                else {
                        open FILE, ">>$configfile" or return "Oops, I am unable to open the file !!";
                        my $lastline = `cat $configfile | tail -1`;
                        chomp($lastline);
                        $lastline =~ s/^backend //g;
                        $lastline =~ s/^pc//;
                        $lastline =~ s/ .*//g;
                        $lastline++;
                        my $beforeprint = "backend pc".$lastline." ".$proc." ".$user."@".$host." ".$java." ".$pj."\n";
                        print FILE $beforeprint;
                        close FILE;
                        #print "ADDING ::: $beforeprint\n";
                        print "<PASS> :: $host, successfully added to the list !!\n";
                        return "Successfully Added, Now you are a part of the GRID !! ";
                }
	}
}

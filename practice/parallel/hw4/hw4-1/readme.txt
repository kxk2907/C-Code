Output 
======

Sun May 15 16:12:11 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-1]java hw4q1seq 1 10000 output_seq.bin
Job 10426, thug12
2595 msec
Sun May 15 16:12:42 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-1]java -Dpj.np=8 hw4q1clu 1 10000 output_clu.bin
Job 10427, thug13, thug14, thug15, thug16, thug17, thug18, thug19, thug20
12679 msec
12650 msec
12714 msec
12698 msec
12639 msec
12725 msec
12610 msec
13583 msec
Sun May 15 16:13:19 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-1]diff  output_clu.bin output_seq.bin
Sun May 15 16:13:31 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-1]


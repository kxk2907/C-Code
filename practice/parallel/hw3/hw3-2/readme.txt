Speedup
========
Since the image given is very small in size I couldn't significantly prove the speed up.

A big image will give defnitely good speedup

For the given image speedup calculation is as follows

N       K       T       Spdup   Effic   EDSF    Devi

100     seq     2689                            0%
100     1       2590    1.038   1.038           0%
100     2       3144    0.855   0.428   1.428   0%
100     3       3990    0.674   0.225   1.811   0%
100     4       3217    0.836   0.209   1.323   0%
100     5       3138    0.857   0.171   1.264   0%
100     6       2987    0.900   0.150   1.184   0%
100     7       2876    0.935   0.134   1.129   0%
100     8       2613    1.029   0.129   1.010   0%

Sample run :
=============

Sun May  1 20:08:28 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-2]java hw3q2seq julia.pjg 600 200 julia_shift_seq.pjg
Job 4430, thug04
2689 msec
Sun May  1 20:09:46 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-2]java -Dpj.np=1 hw3q2clu julia.pjg 600 200 julia_shift_seq.pjg
Job 4434, thug25
2590 msec
Sun May  1 20:10:55 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-2]java -Dpj.np=4 hw3q2clu julia.pjg 600 200 julia_shift_seq.pjg
Job 4439, thug10, thug11, thug12, thug13
3217 msec
Sun May  1 20:13:18 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-2]java -Dpj.np=8 hw3q2clu julia.pjg 600 200 julia_shift_seq.pjg
Job 4450, thug09, thug10, thug11, thug12, thug13, thug14, thug15, thug16
2613 msec


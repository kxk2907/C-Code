
Speedup Calculation
===================


N	        K       T       Spdup   Effic   EDSF    Devi

100000000       seq     86283                           0%
100000000       1       89183   0.967   0.967           0%
100000000       2       43455   1.986   0.993   -0.025  0%
100000000       3       30349   2.843   0.948   0.010   0%
100000000       4       22410   3.850   0.963   0.002   0%
100000000       5       18462   4.674   0.935   0.009   0%
100000000       6       14837   5.815   0.969   -0.000  0%
100000000       7       12446   6.933   0.990   -0.004  0%
100000000       8       11100   7.773   0.972   -0.001  0%


Output 
======

Sun May 15 16:25:49 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-3]java hw4q3seq 1 100
Job 10453, thug17
33,-5
71 msec
Sun May 15 16:26:11 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-3]java -Dpj.np=7 hw4q3clu 1 100
Job 10454, thug18, thug19, thug20, thug21, thug22, thug23, thug24
33,-5
556 msec
Sun May 15 16:26:31 [kxk2907@paranoia:~/practice/parallel/hw4/hw4-3]


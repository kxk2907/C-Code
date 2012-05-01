Speedup Calculation
===================

In case for input size = 10000000 

Speedup = 7.231 for 8 processors

N       	K       T       Spdup   Effic   EDSF    Devi

10000000        seq     26450                           0%
10000000        1       26531   0.997   0.997           0%
10000000        2       13270   1.993   0.997   0.000   0%
10000000        3       9207    2.873   0.958   0.021   0%
10000000        4       7071    3.741   0.935   0.022   0%
10000000        5       5684    4.653   0.931   0.018   0%
10000000        6       4816    5.492   0.915   0.018   0%
10000000        7       4213    6.278   0.897   0.019   0%
10000000        8       3658    7.231   0.904   0.015   0%


In case for input size = 100000000

Speedup = 7.88 for 8 processors 

N       	K       T       Spdup   Effic   EDSF    Devi

100000000        seq     299961                          0%
100000000        1       300264  0.999   0.999           0%
100000000        2       150152  1.998   0.999   0.000   0%
100000000        3       100661  2.980   0.993   0.003   0%
100000000        4       76750   3.908   0.977   0.007   0%
100000000        5       60096   4.991   0.998   0.000   0%
100000000        6       50163   5.980   0.997   0.000   0%
100000000        7       43172   6.948   0.993   0.001   0%
100000000        8       38066   7.880   0.985   0.002   0%

Sample Output 
=============

Sun May  1 19:17:04 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-3]java hw3q3seq 1000000
Job 4289, thug11
837799
2540 msec
Sun May  1 19:17:32 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-3]java -Dpj.np=4 hw3q3clu 1000000
Job 4292, thug02, thug03, thug04, thug05
837799
1040 msec
Sun May  1 19:17:52 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-3]



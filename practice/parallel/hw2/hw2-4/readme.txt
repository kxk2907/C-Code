The calculation of speedup is as followd

N       K       T       Spdup   Effic   EDSF    Devi

3000000 seq     9567                            0%
3000000 1       10257   0.933   0.933           0%
3000000 2       5549    1.724   0.862   0.082   0%
3000000 3       3838    2.493   0.831   0.061   0%
3000000 4       3190    2.999   0.750   0.081   0%
3000000 5       2997    3.192   0.638   0.115   0%
3000000 6       2679    3.571   0.595   0.113   0%
3000000 7       2443    3.916   0.559   0.111   0%
3000000 8       2212    4.325   0.541   0.104   0% 


Ouput 
=====

Sun Apr 17 22:14:02 [kxk2907@paradise:~/practice/parallel/hw2/hw2-4]
Sun Apr 17 22:14:02 [kxk2907@paradise:~/practice/parallel/hw2/hw2-4]/usr/local/versions/jdk-1.5.0_15/bin/java hw2q4smp 15 3 output_file.txt
Run time :: 177 msec
Sun Apr 17 22:14:26 [kxk2907@paradise:~/practice/parallel/hw2/hw2-4]less output_file.txt

Run time :: 177 msec

Original data
5 8 4 6 2 1 8 4 5 9 8 6 4 3 3

[5, 13, 12, 10, 8, 3, 9, 12, 9, 14, 17, 14, 10, 7, 6]
[5, 13, 17, 23, 20, 13, 17, 15, 18, 26, 26, 28, 27, 21, 16]
[5, 13, 17, 23, 25, 26, 34, 38, 38, 39, 43, 43, 45, 47, 42]
[5, 13, 17, 23, 25, 26, 34, 38, 43, 52, 60, 66, 70, 73, 76]

Final data
5 13 17 23 25 26 34 38 43 52 60 66 70 73 76
Sun Apr 17 22:14:36 [kxk2907@paradise:~/practice/parallel/hw2/hw2-4]
Sun Apr 17 22:14:36 [kxk2907@paradise:~/practice/parallel/hw2/hw2-4]



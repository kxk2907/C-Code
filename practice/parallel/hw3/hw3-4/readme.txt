Sample output 
=============

Set 1 
=====

Sun May  1 19:51:40 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java hw3q4seq 9 1 output_seq.bin
Job 4374, thug29
185 msec
Sun May  1 19:51:55 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java -Dpj.np=9 hw3q4clu 9 1 output_parallel.bin
Job 4375, thug30, thug31, thug32, thug01, thug02, thug03, thug04, thug05, thug06
629 msec
Sun May  1 19:52:15 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]diff output_seq.bin output_parallel.bin
Sun May  1 19:52:21 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]


Sun May  1 19:52:24 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]cat output_seq.bin
Matrix C :
20249 11788 19929 18009 12808 12859 21989 17696 15045
36833 19350 29976 25774 25405 23732 30414 29221 29267
27119 19369 25539 26314 22497 18731 21436 20505 19736
26061 14655 23438 25964 21549 21915 24672 25231 20023
23044 15437 19676 17768 19017 18245 24257 21117 17226
29180 17479 21092 17496 22202 19572 17092 18586 23254
22218 15001 23183 21941 17058 13908 24140 20524 15264
32344 19647 24633 18696 27670 20248 27578 24293 28010
33267 17187 33304 28212 24012 19071 25207 22953 27642
Sun May  1 19:52:27 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]cat output_parallel.bin
Matrix C :
20249 11788 19929 18009 12808 12859 21989 17696 15045
36833 19350 29976 25774 25405 23732 30414 29221 29267
27119 19369 25539 26314 22497 18731 21436 20505 19736
26061 14655 23438 25964 21549 21915 24672 25231 20023
23044 15437 19676 17768 19017 18245 24257 21117 17226
29180 17479 21092 17496 22202 19572 17092 18586 23254
22218 15001 23183 21941 17058 13908 24140 20524 15264
32344 19647 24633 18696 27670 20248 27578 24293 28010
33267 17187 33304 28212 24012 19071 25207 22953 27642
Sun May  1 19:52:38 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]


set 2  (for simplicity I am not copying the entire output )
=====

Sun May  1 19:52:38 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java -Dpj.np=9 hw3q4clu 9 1 output_parallel.bin
Sun May  1 19:53:43 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]rm -rf output_*
rm: remove output_parallel.bin (yes/no)? y
rm: remove output_seq.bin (yes/no)? y
Sun May  1 19:53:49 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java -Dpj.np=16 hw3q4clu 64 1 output_parallel.bin
Job 4384, thug21, thug22, thug23, thug24, thug25, thug26, thug27, thug28, thug05, thug06, thug07, thug08, thug13, thug14, thug15, thug16
809 msec
Sun May  1 19:54:24 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java hw3q4seq 64 1 output_seq.bin
Job 4386, thug22
415 msec
Sun May  1 19:54:42 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]diff output_seq.bin output_parallel.bin
Sun May  1 19:54:51 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]
Sun May  1 19:54:52 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]cat output_seq.bin
Matrix C :
138263 136205 177624 125615 161213 157884 158763 144256 132913 158619 142570 146324 169973 147764 156570 153591 155042 149029 167754 138062 141009 137345 148622 127543 151513 156093 154277 131782 151480 161428 142231 133272 145654 146162 138679 144770 145941 180653 132024 138667 138858 133687 157286 146346 119441 154918 143665 164724 157698 141585 146165 130536 145327 146892 149979 133511 134186 147794 149985 144848 145154 141271 132326 154713


Set 3 : as the input size increases it appears to be increase in speedup  
======

Sun May  1 19:57:47 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java -Dpj.np=8 hw3q4clu 10 1 output_parallel.bin
Sun May  1 19:57:58 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java -Dpj.np=16 hw3q4clu 256 1 output_parallel.bin
Job 4400, thug11, thug12, thug13, thug14, thug15, thug16, thug17, thug18, thug19, thug20, thug21, thug22, thug23, thug24, thug25, thug26
1367 msec
Sun May  1 19:58:44 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]java hw3q4seq 256 1 output_seq.bin
Job 4402, thug07
2101 msec
Sun May  1 19:59:12 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]
Sun May  1 19:59:12 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]diff output_seq.bin output_parallel.bin
Sun May  1 20:00:05 [kxk2907@paranoia:~/practice/parallel/hw3/hw3-4]



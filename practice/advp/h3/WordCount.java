/**
 * Find word count in a file.
 *
 *
 * @version   $Id: WordCount.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.Scanner;

/*
 *  Use the delimiters \n and " " to find the wc -w 
 *  Mon Sep 27 01:40:09 [kxk2907@joplin:~/practice/advp/h3]wc -w filename
 *  6934 filename
 *	Mon Sep 27 01:40:49 [kxk2907@joplin:~/practice/advp/h3]cat filename | java WordCount
 *	Word Count : 6934
 *	Mon Sep 27 01:41:00 [kxk2907@joplin:~/practice/advp/h3]
 */

class WordCount {
	public static void main(String args[]) {
		int wordcount = 0;
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()) {
			sc.next();
			wordcount ++;
		}
		System.out.println("Word Count : " + wordcount);
		sc.close();
	}
}



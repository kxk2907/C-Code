Importing "project2.jar" 
========================
1. Create a new project in eclipse "or" in the current project which is already created.
2. Goto File->Import->General(Archive File)  and enter the location of the jar file.

Build and Run Project
=====================
1. After import be sure to check for following files in respective directories
	a. team/Team.java
	b. viewer/Viewer.java
	   viewer/Table.java
	   viewer/table.properties
	c. judge/Judge.java
	d. config.txt
	e. demo.txt
	f. javadoc.xml
	g. policy
	
2. Check for any build errors for libraries pointing to cscl.jar. If any error occurs include the cscl.jar to "Referenced Libraries" to get rid of those.
3. Once built modify config.txt and demo.txt
       In config.txt
	   -------------
	   a. modify HOME, CSCL and JAVA to point the env variables to appropriate library locations
	   In demo.txt
	   -----------
	   a. modify Common, Tuples and Policy to point to appropriate locations. 
	   b. Also, be sure to change '\\' to '/' if mac or linux is used.
	   c. Also, be sure to change ';' to ':' if mac or linux is used.
4. Include "os.java" in the project and run with arguments
       arg[0] : config.txt          arg[1] : demo.txt
5. Click run to test the code. 	   

Generating JavaDoc
==================
1. Goto Project->Generate JavaDoc...
2. Select the project for which javadoc to be created. In this case "project2"
3. Be sure to specify the location of "javadoc.exe" [in windows]
4. Enable option button for "create javadoc for members with visibility" as 
        O  protected 
5. Specify the destination folder for javadoc.
6. Documentation for "project2" requires some references docs from "cscl.jar". Specify the location url for javadoc of cscl.jar as "http://www.cs.rit.edu/~ark/cscl/doc/". 
7. Be sure to check box - "open generated files in browser" to use eclipse browser directly after generating
8. Click submit/generate 


References
==========

1. Table.java belongs from Prof Schreiner's solution from project #1. I made modifications to methods submit and judged to work for project #2
2.  SwingUtilities.invokeLater - http://stackoverflow.com/questions/7196889/swingutilities-invokelater
    I referred to this document to use the method to gracefully update the table display when multiple threads are trying to access the JTextPane
    It is an asynronous method but as per my understanding it does queue the events for updating and refreshing the queue. Adding this method I 
    tend to avoid Thread Exception, unfortunatly I was not able to find the reason for the exception. 
3. I have attached the sequence diagram as a picture just in case if it doesn't get display properly in the text editor.    

Coding specific documentation 
=============================

List of Tuples 
--------------

SUBMIT_SEQ sseq
	- To maintain the order of submission order to judge. sseq is an Integer object. 
	  Init : This tuple is initialized by the first team joining the contest. 
JUDGE_SEQ jseq
    - To maintain the order of judging in the same of submission. jseq is an Integer object.
	  Init : This tuple is initialized by the first judge incase there are multiples judges judging the contest.
SUBMIT team problem time sseq
    - A team submits a tuple with team, problem, time and sseq where team, problem and time are string object. 
	  sseq is read everytime from ts and incremented and it is submitted back to ts.
JUDGED team problem time true/false jseq
    - A judge judges a SUBMIT tuple by taking it with lease with mathcing jseq (for the order) and judges yes/no by submitting back the JUDGED tuple.
	  team, problem and time are String object and true/false is a Boolean object. jseq is an Integer.
PENDING team problem time 
    - Whenever a problem is already submitted and not yet judged it is added to pending. 
	  Whenever a problem is judged and the judgment is true, this tuple is flushed out of ts. If judgment is false, this tuple is added as a SUBMIT tuple. 
	  

Sequence Diagram 
-----------------
			Team 										Judge												Viewer
			====										=====												======
																								it1=SpaceIterator SUBMIT * * *
																								it2=SpaceIterator JUDGED * * * *
																								while it1(timeout=0) buildtable
																								while it2(timeout=0) buildtable
//Init 										//Init
if !read(timeout = 0) SUBMIT_SEQ *   		if !read(timeout = 0) JUDGE_SEQ, *
	write SUBMIT_SEQ  Integer(0)				write JUDGE_SEQ Integer(0)
											
																								sseq = read SUBMIT_SEQ *
																								jseq = read JUDGE_SEQ *
while(standard input) 													
	if !read(timeout = 0) SUBMIT name problem * *
	  && !read(timeout = 0) PENDING name problem *																
		  sseq,L1 = takewithlease SUBMIT_SEQ *
		  write SUBMIT name problem time sseq						
		  write SUBMIT_SEQ ++sseq																Thread 1 
		  cancelLease(L1)																		read SUBMIT * * * ++sseq  buildtable 
									while(standare input) 
										jseq,L1 = takewithlease JUDGE_SEQ *
										submit,L2 = takewithlease SUBMIT * * jseq
										write JUDGED team,problem,time,yes/no,jseq
										write JUDGE_SEQ ++jseq									Thread 2
										cancelLease L1											read JUDGED * * * * ++jseq buildtable
										cancelLease L2
										pending,L3 = takewithlease(timeout = 0)PENDING team,problem,*
										if pending.tuple
											if (no)
												sseq, L4 = takewithlease SUBMIT_SEQ *
												write SUBMIT name problem time sseq
												write SUBMIT_SEQ ++sseq							Thread 1
												cancelLease(L4)									read SUBMIT * * * ++sseq buildtable
												cancelLease(L3)		
											












	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
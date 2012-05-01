
/**
 * Demonstrate Producer and Consumer problem
 *
 *
 * @version   $Id: ConsumerProducer.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nhs5732 (Nipun Harihare Sud) 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

/*
 * Producer Consumer problem
 */
public class ConsumerProducer extends Thread{
	final static int arraySize = 100;		//buffer size for the producer to put in ..
	final int Sleep = 10;
	static Integer[] content = new Integer[arraySize];
	static Integer tracker = 0;				// tracker holds the current position of the producer 
	String type;
	String name;
	public ConsumerProducer(String type,String name) {
		this.type = type;
		this.name = name;
	}
	public synchronized static void incTracker() { 
		++tracker;
	}
	public synchronized static void decTracker() {
		--tracker;
	}

	public synchronized void proccons() throws InterruptedException {
		if(this.getT().equals("producer")) {
			synchronized(tracker) {
				synchronized(content) {
					if(tracker >= 0 && tracker < arraySize) {	//based on tracker put the produced item into the array 
						sleep(Sleep);
						int num = (int) (Math.random() * 1000);
						content[tracker] = num;
						System.out.println(this.getN() + " Number produced " + num + " : tracker : " + tracker);
						incTracker();
					}
					else if(tracker == arraySize) {
						notifyAll();
						sleep(Sleep);
						System.out.println("Producer " + this.getN() + " waits !!");
						this.setPriority(MIN_PRIORITY);
						this.wait(Sleep*5);
					}
				}
			}
		}
		if(this.getT().equals("consumer")) {	// if consumer consume the item from the array by sticking to the tracker value.
			synchronized(tracker) {
				synchronized(content) {
					if(tracker == 0) {
						notifyAll();
						sleep(Sleep);
						System.out.println("Consumer " + this.getN() + " waits !!");
						this.wait(Sleep*5);
					}
					else if(tracker > 0 && tracker < arraySize) {
						sleep(Sleep);
						decTracker();
						int num = content[tracker];
						System.out.println(this.getN() + " Number consumed " + num + " : tracker : " + tracker);
						notifyAll();
					}
				}
			}
		}
	}
	/*
	 * Bubble sort
	 */
	public static void sort() {			// After an item produced or consumed make sure to sort the items in the array 
		synchronized(tracker) {
			synchronized(content) {	
				for(int i = 0;i<tracker;i++) {
					for(int j = 0;j<tracker;j++) {
						if(content[i] > content[j]) {
							content[i] = content[i] ^ content[j];
							content[j] = content[i] ^ content[j];
							content[i] = content[i] ^ content[j];
						}
					}
				}
			}
		}
	}
	public void run() {				
		for(int i = 0;i<50;i++){
			try {
				proccons();
			} catch (InterruptedException e) {
				System.err.println("SOME ERROR IN PROCONS !! in RUN METHOD !!");
			}
			ConsumerProducer.sort();	// After an item produced or consumed make sure to sort the items in the array 
		}
	}
	public String getT() {
		return this.type;
	}
	public String getN() {
		return this.name;
	}
	public static void main(String args[]) throws InterruptedException {
		int producers = 0;
		int consumers = 0;
		for(int i = 0;i< args.length;i++){				//checking conditions to take input from the user 
			if(args[0].equals("-consumer")){
				 consumers = Integer.parseInt(args[1]);
				 producers= Integer.parseInt(args[3]);
			}
			else {
				producers= Integer.parseInt(args[1]);
				consumers= Integer.parseInt(args[3]);
			}
		}
		ConsumerProducer[] pro = new ConsumerProducer[producers];		//creating producers and consumers based on the input 
		ConsumerProducer[] con = new ConsumerProducer[consumers];	
		for(int i = 0;i<arraySize;i++) 
			content[i] = 0;
		for(int i = 0;i<producers;i++) {
			pro[i] = new ConsumerProducer("producer","p"+i);
		}
		for(int i = 0;i<consumers;i++) {
			con[i] = new ConsumerProducer("consumer","c"+i);
		}
		int it = (producers > consumers) ? producers : consumers;
		for(int i=0,j=0;i<it;i++,j++) {				// starting the producers and the consumers 
			if(it == producers) {
				pro[i].start();
				if(j<consumers) con[j].start();
			}
			else if(it == consumers) {
				con[i].start();
				if(j<producers) pro[j].start();
			}
		}
		for(int i=0,j=0;i<it;i++,j++) {			// Join method to ensure that the producers / consumers doesn't end before the last one is done 
			if(it == producers) {
				pro[i].join();
				if(j<consumers) con[j].join();
			}
			else if(it == consumers) {
				con[i].join();
				if(j<producers) pro[j].join();
			}
		}
	}
}

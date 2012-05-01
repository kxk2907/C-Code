/**
 * hw4q1clu.java
 *
 *
 * @version   $Id: hw4q1clu.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */

import java.io.File;
import java.util.Scanner;
import edu.rit.mp.IntegerBuf;
import edu.rit.mp.buf.IntegerItemBuf;
import edu.rit.pj.Comm;
import edu.rit.pj.CommStatus;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelSection;
import edu.rit.pj.ParallelTeam;

/**
 * 
 * Master-worker pattern cluster parallel program for finding the shortest path
 * from a single source
 * 
 * @author kbkarthi
 * 
 */
public class hw4q2clu {
	public static int[][] matrix; 	// input matrix
	public static int n; 			// number of vertex
	public static int[] dist; 		// distance array from source
	public static int size; 		// # of processors
	public static int rank; 		// rank of the processor
	public static Comm world; 		// world for send and receive
	public static final int WORKER_MSG = 0; // messages tag sent between client
											// and server
	private static final int WORKER_MSG_2 = 1;
	private static final int DATA_MSG = 2;

	public static void main(String[] args) throws Exception {
		// cluster parallel section
		Comm.init(args);
		world = Comm.world();
		size = world.size();
		rank = world.rank();

		// user arguments to read from the file for adjacecy matrix
		if (args.length != 1)
			Usage();
		File file = new File(args[0]);
		Scanner sc = new Scanner(file);
		if (sc.hasNextLine())
			n = Integer.parseInt(sc.nextLine());
		matrix = new int[n][n];
		dist = new int[n];
		int k = 0;
		while (sc.hasNextLine()) {
			String[] temp = sc.nextLine().split(" ");
			for (int j = 0; j < temp.length; j++) {
				matrix[k][j] = Integer.parseInt(temp[j]);
			}
			k++;
		}

		// rank = 0 runs as both master and worker in the master and worker
		// pattern
		// other ranks are workers
		if (rank == 0) {
			new ParallelTeam(2).execute(new ParallelRegion() {
				public void run() throws Exception {
					execute(new ParallelSection() {
						public void run() throws Exception {
							masterSection();
						}
					}, new ParallelSection() {
						public void run() throws Exception {
							workerSection();
						}
					});
				}
			});
		} else {
			workerSection();
		}
	}

	/**
	 * Master section of the code : rank - 0
	 * @throws Exception 
	 */
	public static void masterSection() throws Exception {
		boolean[] queue = new boolean[n];			//queue containing the vertices
		boolean[] workers = new boolean[size];		//list of available worker nodes
		queue[0] = true; // starting point			//starting vertex
		int activeWorkers = 1;						//activeWorkers to check if there is any 

		while (activeWorkers > 0) {					
			int sendcount = 0;						//number of workers the job is sent to 
			boolean flag = true;
			if (!check(queue)) {
				activeWorkers = 0;
			}
			//check queue returns of there is any vertex in the queue to be processed
			//flag is true if there are available workers to process
			while (check(queue) && flag) {			
				int worker = getWorker(workers);	//get the worker
				if (worker != -1) {					// -1 is illegal worker
					int work = remove(queue);		//remove the vertex from the queue
					IntegerItemBuf buf = IntegerBuf.buffer();
					buf.item = work;
					world.send(worker, WORKER_MSG, buf);	//send the vertex to the worker
					IntegerBuf bufsend = IntegerBuf.buffer(dist);
					world.send(worker, WORKER_MSG_2, bufsend); //send the current distance array to the worker
					sendcount++;
				} else {
					flag = false;					//if not worker there flag = false;
				}
			}
			for (int j = 0; j < sendcount; j++) {	//for each worker the job send to result should be received
				int[] remotedist = new int[n];		
				IntegerBuf bufrecv = IntegerBuf.buffer(remotedist);
				//updated distance array received
				CommStatus status = world.receive(null, DATA_MSG, bufrecv);	
				int worker = status.fromRank;
				workers[worker] = false;
				//processing the distance to update to the actual dist[]
				for (int i = 1; i < n; i++) {
					if (remotedist[i] > dist[i] && dist[i] == 0) {
						dist[i] = remotedist[i];
						queue[i] = true;
					} else if (remotedist[i] < dist[i]) {
						dist[i] = remotedist[i];
						queue[i] = true;
					}
				}
			}
		}
		
		//when the queue if empty it comes here 
		//display the result and exit
		System.out.print("[");
		for (int i = 0; i < n - 1; i++) {
			System.out.print(dist[i] + ", ");
		}
		System.out.print(dist[n - 1] + "]\n");
		
		//each worker should be exited before master exit 
		//so item = -1 will exit the worker nodes.
		for (int i = 0; i < size; i++) {
			IntegerItemBuf ex = IntegerBuf.buffer();
			ex.item = -1;
			world.send(i, WORKER_MSG, ex);
		}
		System.exit(1);
	}

	public static void workerSection() throws Exception {
		int[] localdist = new int[n];
		for (;;) {	//infinite listening in the worker section 
					//it will exit if the worker receives -1
			IntegerItemBuf bufrecvitem = IntegerBuf.buffer();	
			IntegerBuf bufrecv = IntegerBuf.buffer(localdist);	
			world.receive(0, WORKER_MSG, bufrecvitem);//recieves node
			if (bufrecvitem.item == -1) {
				System.exit(1);
			}
			world.receive(0, WORKER_MSG_2, bufrecv);//recieves dist array
			int i = bufrecvitem.item;
			for (int j = 0; j < n; j++) {			//shortest path algorithm
				if ((matrix[i][j] != -1) && i != j) {
					int newdist = localdist[i] + matrix[i][j];
					if (newdist < localdist[j] || localdist[j] == 0) {
						localdist[j] = newdist;
					}
				}
			}
			//send the result back
			IntegerBuf bufsend = IntegerBuf.buffer(localdist);
			world.send(0, DATA_MSG, bufsend);
		}
	}

	/**
	 * Check if there are any vertices in the queue
	 * @param queue
	 * @return
	 */
	public static boolean check(boolean[] queue) {
		for (int i = 0; i < n; i++) {
			if (queue[i])
				return true;
		}
		return false;
	}

	/**
	 * remove an element from the queue
	 * @param queue
	 * @return
	 */
	public static int remove(boolean[] queue) {
		for (int i = 0; i < n; i++) {
			if (queue[i]) {
				queue[i] = false;
				return i;
			}
		}
		return -1;
	}

	/**
	 * get any next available worker 
	 * @param workers
	 * @return
	 */
	public static int getWorker(boolean[] workers) {
		for (int i = 0; i < size; i++) {
			if (!workers[i]) {
				workers[i] = true;
				return i;
			}
		}
		return -1;
	}

	/**
	 * Usage
	 */
	private static void Usage() {
		System.err.println("java hw4q2seq <input-text-file-name>");
		System.exit(1);
	}
}

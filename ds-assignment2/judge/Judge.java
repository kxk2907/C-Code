import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;
import edu.rit.ds.space.Space;
import edu.rit.ds.space.TupleAndLease;

/**
 * @author Karthikeyan Karur Balu
 * 
 * <br>
 *         Judge evaluates a job . <br>
 *         Tuples used : <br>
 *         JUDGE_SEQ - to judge in the order of submission. <br>
 *         JUDGED - judging details(team, problem, time, yes/no) <br>
 *         PENDING - If already submitted <br>
 *         SUBMIT - to move the pending to submission if judgement is "no"
 * 
 */
public class Judge {

	/** tuple space varibale */
	protected final Space ts;

	/** Lease Time to be used in TakeWithLease */
	protected final Long leaseTime = 100000000L;

	/**
	 * To judge the submission, started from main
	 * 
	 * @param args
	 *            [0]: registry host, [1]: registry port, [2]: tuplespace
	 * @throws RemoteException
	 *             Occurs for errors with RegistryProxy
	 * @throws NotBoundException
	 *             Occurs for errors with RegistryProxy
	 */
	public Judge(String... args) throws RemoteException, NotBoundException {

		// verify command line arguments
		if (args.length != 3)
			throw new IllegalArgumentException(
					"Usage: java Judge <host> <port> <tuplespace>");

		// get and check command line arguments
		String host = args[0];
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Judge: Invalid port: \""
					+ args[1] + "\"");
		}
		String tuplespace = args[2];

		// setting the secrurityManager if not present already
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());

		// look up for the tuplespace and set a proxy.
		ts = (Space) (new RegistryProxy(host, port).lookup(tuplespace));

		// judging the submissions
		judge();
	}

	/**
	 * Input the judgment (true/false) from standard input
	 * 
	 * @throws RemoteException
	 *             Occurs for errors with tuplespace operations
	 */
	protected void judge() throws RemoteException {

		// Reader from standard input
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;

		try {

			// initialize the JUDGE_SEQ tuple to 0, one of the judge does this
			// operation. timeout 0, so it is non-blocking read
			if (ts.read(new Object[] { "JUDGE_SEQ", null }, 0) == null)
				ts.write(new Object[] { "JUDGE_SEQ", new Integer(0) });

			// get next line of input
			while ((line = in.readLine()) != null) {

				// set judgement
				Boolean ok = new Boolean(line.startsWith("t"));

				// get sequence of tuple last judges
				TupleAndLease judgeSeqTuple = ts.takeWithLease(new Object[] {
						"JUDGE_SEQ", null }, leaseTime);
				Integer jseq = (Integer) judgeSeqTuple.tuple[1];

				// increment the seq
				++jseq;

				//get the submission 
				TupleAndLease submitTuple = ts.takeWithLease(new Object[] {
						"SUBMIT", null, null, null, jseq }, leaseTime);

				//submission details
				String team = (String) submitTuple.tuple[1];
				String project = (String) submitTuple.tuple[2];
				String time = (String) submitTuple.tuple[3];

				//judging 
				ts.write(new Object[] { "JUDGED", team, project, time, ok, jseq });
				submitTuple.lease.cancel();

				//update the judge seq for next iteration
				ts.write(new Object[] { "JUDGE_SEQ", jseq });
				
				//cancel old seq
				judgeSeqTuple.lease.cancel();

				//checking for any pending for the item judged
				TupleAndLease pendingLease = ts.takeWithLease(new Object[] {
						"PENDING", team, project, null }, 0, leaseTime);

				//if (pending) 
				if (!(pendingLease.tuple == null)) {
					
					//"false" judgement 
					if (!ok) {
						
						//get the pending tuple 
						String subtime = (String) pendingLease.tuple[3];

						//move to the submission tuple to be judged next time
						TupleAndLease submitSeqTuple = ts.takeWithLease(
								new Object[] { "SUBMIT_SEQ", null }, leaseTime);
						Integer sseq = (Integer) submitSeqTuple.tuple[1];
						++sseq;
						
						//moved to submission 
						ts.write(new Object[] { "SUBMIT", team, project,
								subtime, sseq });
						ts.write(new Object[] { "SUBMIT_SEQ", sseq });
						
						//changing the submission seq
						submitSeqTuple.lease.cancel();
					}
					//"true" cancel pending lease (also "false" last case)
					pendingLease.lease.cancel();
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Judge(): Unexpected i/o issue [" + e + "]");
		} catch (InterruptedException e) {
			throw new IllegalArgumentException(
					"Judge(): Unexpected tuplespace operation issue [" + e
							+ "]");
		}
	}

	/**
	 * Main method : creates object for Judge() and passes the arguments
	 * 
	 * @param args
	 *            [0]: registry host, [1]: registry port, [2]:
	 *            tuplespace
	 * @throws RemoteException
	 *             Occurs for errors with RegistryProxy
	 * @throws NotBoundException
	 *             Occurs for errors with RegistryProxy
	 */

	public static void main(String... args) throws RemoteException,
			IOException, InterruptedException, NotBoundException {
		new Judge(args);
	}
}

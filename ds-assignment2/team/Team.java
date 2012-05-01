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
 *			<br> 
 * 		   Team submits team,problem,time to be evaluated. <br>Tuples
 *         used : <br>SUBMIT_SEQ - to retain the order of submission. <br>SUBMIT -
 *         submission details(team, problem, time) <br>PENDING - If already
 *         submitted
 * 
 */
public class Team {

	/** team's unique name. */
	protected final String name;

	/** tuple space varibale */
	protected final Space ts;

	/** Lease Time to be used in TakeWithLease */
	protected final Long leaseTime = 100000000L;

	/**
	 * To be started from main by creating object for Team()
	 * 
	 * @param args
	 *            [0]: registry host, [1]: registry port, [2]: name, [3]:
	 *            tuplespace
	 * @throws RemoteException
	 *             Occurs for errors with RegistryProxy
	 * @throws NotBoundException
	 *             Occurs for errors with RegistryProxy
	 */
	public Team(String... args) throws RemoteException, NotBoundException {

		// verify command line arguments
		if (args.length != 4)
			throw new IllegalArgumentException(
					"Usage: java Start Team <host> <port> <name> <tuplespace>");

		// get and check command line arguments
		String host = args[0];
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Team(): Invalid port: \""
					+ args[1] + "\"");
		}
		String tuplespace = args[2];
		name = args[3];

		// setting the secrurityManager if not present already
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());

		// look up for the tuplespace and set a proxy.
		ts = (Space) (new RegistryProxy(host, port).lookup(tuplespace));

		// create submissions
		submit();
	}

	/**
	 * Input for the submission is from standard input
	 * 
	 * @throws RemoteException
	 *             Occurs for errors with tuplespace operations
	 */
	protected void submit() throws RemoteException {

		// Reader from standard input
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;

		try {

			// initialize the SUBMIT_SEQ tuple to 0, one of the teams does this
			// operation. timeout 0, so it is non-blocking read
			if (ts.read(new Object[] { "SUBMIT_SEQ", null }, 0) == null)
				ts.write(new Object[] { "SUBMIT_SEQ", new Integer(0) });

			// get next line of input
			while ((line = in.readLine()) != null) {

				// parse : problem time
				String[] fields = line.split("\\s+");
				if (fields.length != 2)
					throw new IllegalArgumentException(
							"Team(): Invalid input: \"" + line + "\"");

				// Read if there is already a submit or pending tuple with the
				// same name, problem
				Object[] submit = ts.read(new Object[] { "SUBMIT", name,
						fields[0], null, null }, 0);
				Object[] pending = ts.read(new Object[] { "PENDING", name,
						fields[0], null }, 0);

				// if not present in submit and not present in pending tuple
				if (submit == null && pending == null) {

					// Look-up for the sequence for submission
					TupleAndLease submitSeqTuple = ts.takeWithLease(
							new Object[] { "SUBMIT_SEQ", null }, leaseTime);
					Integer sseq = (Integer) submitSeqTuple.tuple[1];

					// increment the sequence
					sseq++;

					// write SUBMIT with the new sequence number
					ts.write(new Object[] { "SUBMIT", name, fields[0],
							fields[1], sseq });

					// add the incremented sequence back to ts
					ts.write(new Object[] { "SUBMIT_SEQ", sseq });

					// cancel the lease of previous sequence
					submitSeqTuple.lease.cancel();

				}
				// if present in submit but not in pending
				else if (pending == null) {

					// add the tuple to pending
					ts.write(new Object[] { "PENDING", name, fields[0],
							fields[1] });
				}
			}
			return;
		} catch (IOException e) {
			throw new IllegalArgumentException("Team(): Unexpected i/o issue ["
					+ e + "]");
		} catch (InterruptedException e) {
			throw new IllegalArgumentException(
					"Team(): Unexpected tuplespace operation issue [" + e + "]");
		}
	}

	/**
	 * Main method : creates object for Team() and passes the arguments
	 * 
	 * @param args
	 *            [0]: registry host, [1]: registry port, [2]: name, [3]:
	 *            tuplespace
	 * @throws RemoteException
	 *             Occurs for errors with RegistryProxy
	 * @throws NotBoundException
	 *             Occurs for errors with RegistryProxy
	 */
	public static void main(String... args) throws RemoteException, Exception {
		new Team(args);
	}
}

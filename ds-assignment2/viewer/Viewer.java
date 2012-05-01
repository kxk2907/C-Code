import java.awt.BorderLayout;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;
import edu.rit.ds.space.Space;
import edu.rit.ds.space.SpaceIterator;

/**
 * @author Karthikeyan Karur Balu
 * 
 * <br>
 *         Viewer displays the views. <br>
 *         Tuples used : <br>
 *         SUBMIT - Every submit is displayed. <br>
 *         JUDGED - Every judged is displayed
 * 
 */
public class Viewer {

	/** time stamp formatter. */
	protected static final transient DateFormat fmt;
	static {
		fmt = new SimpleDateFormat("H:mm");
	}

	/** state of contest. */
	protected final Table table;

	/** tuple space variable */
	protected final Space ts;

	/** submission order */
	protected Integer submit;

	/** judgement order */
	protected Integer judge;

	/** textpane to show table */
	protected JTextPane tp;

	/**
	 * To be started from main by creating object for Team()
	 * 
	 * @param args
	 *            [0]: registry host, [1]: registry port, [2]: width, [3]:
	 *            height, [4]: tuplespace, [5]:title
	 * @throws RemoteException
	 *             Occurs for errors with RegistryProxy
	 * @throws NotBoundException
	 *             Occurs for errors with RegistryProxy
	 */
	public Viewer(String... args) throws RemoteException, NotBoundException {
		// verify command line arguments
		if (args.length != 6)
			throw new IllegalArgumentException(
					"Usage: java Viewer <host> <port> <tablespace> <width> <height> <title>");

		// get and check command line arguments
		String host = args[0];
		int port, width, height;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Viewer: Invalid port: \""
					+ args[1] + "\"");
		}
		try {
			width = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Viewer: Invalid width: \""
					+ args[2] + "\"");
		}
		try {
			height = Integer.parseInt(args[4]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Viewer: Invalid height: \""
					+ args[3] + "\"");
		}
		String tuplespace = args[2];
		String title = args[5];

		// create a table
		try {
			table = new Table();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Viewer(): Cannot read configuration [" + e + "]");
		} catch (ParseException e) {
			throw new IllegalArgumentException(
					"Viewer(): Cannot parse start time [" + e + "]");
		}

		// method to build table from JFrame
		buildTable(width, height, title);

		// setting the secrurityManager if not present already
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());

		// look up for the tuplespace and set a proxy.
		ts = (Space) (new RegistryProxy(host, port).lookup(tuplespace));

		// display table
		view();
	}

	/**
	 * Create a table in JFrame
	 * 
	 * @param width
	 *            - Jframe width
	 * @param height
	 *            - Jframe height
	 * @param title
	 *            - Jframe title
	 */
	protected void buildTable(int width, int height, String title) {
		JFrame jf = new JFrame(title);
		jf.setSize(width + 25, height + 25);
		jf.setLocation(1025, 300);
		tp = new JTextPane();
		tp.setEditable(false);
		tp.setContentType("text/html");
		tp.setText(table.toString());
		jf.setVisible(true);

		// adding text pane to JFrame
		jf.getContentPane().add(tp, BorderLayout.CENTER);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Creating and updating the view
	 * 
	 * @throws RemoteException
	 */
	protected void view() throws RemoteException {

		// Init : Check tuple space for any SUBMIT or JUDGED tuple
		try {

			// iterator for JUDGED or SUBMIT tuple
			SpaceIterator itSubmit = ts.iterator(new Object[] { "SUBMIT", null,
					null, null, null });
			SpaceIterator itJudged = ts.iterator(new Object[] { "JUDGED", null,
					null, null, null, null });

			// object respectively
			Object[] submitted;
			Object[] judged;

			// read for submit tuple with timeout = 0 (non-blocking)
			while ((submitted = itSubmit.read(0)) != null) {
				String team = (String) submitted[1];
				String problem = (String) submitted[2];
				Date time = fmt.parse((String) submitted[3]);
				// update table
				table.submit(team, problem, time);
			}

			// read for judged tuple with timeout = 0 (non-blocking)
			while ((judged = itJudged.read(0)) != null) {
				String team = (String) judged[1];
				String problem = (String) judged[2];
				Date time = fmt.parse((String) judged[3]);
				boolean ok = (Boolean) judged[4];
				// update table
				table.judged(team, problem, ok, time);
			}

		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Viewer(): Unexpected i/o issue [" + e + "]");
		} catch (InterruptedException e) {
			throw new IllegalArgumentException(
					"Viewer(): Unexpected tuplespace operation issue [" + e
							+ "]");
		} catch (ParseException e) {
			throw new IllegalArgumentException(
					"Viewer(): Cannot parse start time [" + e + "]");
		}

		// update the display, to help manage concurrent access of tp.SetText
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tp.setText(table.toString());
			}
		});

		//read the seq numbers, if crashes it will be the current seq numbers 
		Object[] submit_seq;
		Object[] judge_seq;

		try {
			submit_seq = ts.read(new Object[] { "SUBMIT_SEQ", null });
			judge_seq = ts.read(new Object[] { "JUDGE_SEQ", null });
		} catch (InterruptedException e) {
			throw new IllegalArgumentException(
					"Viewer(): Unexpected tuplespace operation issue [" + e
							+ "]");
		}

		//store the seq numbers of judged and submit 
		submit = (Integer) submit_seq[1];
		judge = (Integer) judge_seq[1];

		//thread to manage any further submission (uses seq number)
		new Thread(new Runnable() {
			public void run() {
				for (;;) {
					try {
						//wait for next submission 
						Integer sseq = ++submit;
						
						//read the next submission 
						Object[] submit = ts.read(new Object[] { "SUBMIT",
								null, null, null, new Integer(sseq) });
						String team = (String) submit[1];
						String problem = (String) submit[2];
						Date time = fmt.parse((String) submit[3]);
						
						//update table 
						table.submit(team, problem, time);
						
						//update the display
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tp.setText(table.toString());
							}
						});
					} catch (RemoteException e) {
						throw new IllegalArgumentException(
								"Viewer()::submitThread Unexpected Remote issue ["
										+ e + "]");
					} catch (InterruptedException e) {
						throw new IllegalArgumentException(
								"Viewer()::submitThread Unexpected i/o issue ["
										+ e + "]");
					} catch (ParseException e) {
						throw new IllegalArgumentException(
								"Viewer()::submitThread Cannot parse start time ["
										+ e + "]");
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				for (;;) {
					try {
						//wait for next judgement 
						Integer jseq = ++judge;
						
						//get the judgement 
						Object[] judged = ts.read(new Object[] { "JUDGED",
								null, null, null, null, jseq });
						String team = (String) judged[1];
						String problem = (String) judged[2];
						boolean ok = (Boolean) judged[4];
						
						//update table 
						table.judged(team, problem, ok, null);
						
						//update display 
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tp.setText(table.toString());
							}
						});
					} catch (RemoteException e) {
						throw new IllegalArgumentException(
								"Viewer()::judgeThread Unexpected Remote issue ["
										+ e + "]");
					} catch (InterruptedException e) {
						throw new IllegalArgumentException(
								"Viewer()::judgeThread Unexpected i/o issue ["
										+ e + "]");
					}

				}
			}
		}).start();
	}

	/**
	 * Creating object for Viewer()
	 * 
	 * @param args
	 *            [0]: registry host, [1]: registry port, [2]: width, [3]:
	 *            height, [4]: tuplespace, [5]:title
	 * @throws RemoteException
	 *             Occurs for errors with RegistryProxy
	 * @throws NotBoundException
	 *             Occurs for errors with RegistryProxy
	 */
	public static void main(String... args) throws RemoteException,
			NotBoundException {
		new Viewer(args);
	}
}
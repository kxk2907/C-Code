
package common;


import edu.rit.ds.RemoteEvent;

/** sent to contest listeners. */
public class ContestEvent extends RemoteEvent {
	private static final long serialVersionUID = -2558632359094248265L;

	/** current state, ready for display. */
	public final String table;

	public ContestEvent(String table) {
		this.table = table;
	}
}
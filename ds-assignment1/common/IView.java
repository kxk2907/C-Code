package common;

import java.rmi.RemoteException;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventListener;

/** what the contest administration must do for a viewer. */
public interface IView extends IContest {

	/** allow registration as a listener. */
	Lease addListener(RemoteEventListener<ContestEvent> listener)
			throws RemoteException;

	/** pull current contest state. */
	ContestEvent pull() throws RemoteException;
}

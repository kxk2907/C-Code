package common;

import java.rmi.RemoteException;

/** what the contest administration must do for a judge. */
public interface IJudge extends ICallback {

	/** dequeue submission for a judge. */
	Job fetch() throws RemoteException, InterruptedException;
}
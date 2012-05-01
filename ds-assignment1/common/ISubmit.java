package common;

import java.rmi.RemoteException;

/** what the contest administration must do for a team. */
public interface ISubmit extends IContest {

	/** enqueue submission from a team. */
	void submit(Job job) throws RemoteException;
}
package common;


import java.rmi.Remote;
import java.rmi.RemoteException;

/** how to deal with a judgement. */
public interface ICallback extends Remote {

	/** reply concerning a submission. */
	void judged(Job job, boolean ok) throws RemoteException;
	
}
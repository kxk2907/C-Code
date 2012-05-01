package common;
import java.rmi.Remote;

/** base for contest administration. */
public interface IContest extends Remote {

	/** name in registry. */
	public static final String name = "Contest";
}

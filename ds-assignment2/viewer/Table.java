import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author Axel-Tobias Schreiner
 * 
 * Taken the file from Professor Schreiner and modified judged, submit methods
 *
 */
public class Table {

	/** time stamp formatter. */
	protected static final DateFormat fmt;
	static {
		fmt = new SimpleDateFormat("H:mm");
	}

	/** display header and trailer from resource file. */
	protected static final String header, trailer;
	static {
		// locate table.properties
		ResourceBundle r = null;
		try {
			r = ResourceBundle.getBundle("table");
		} catch (MissingResourceException e) {
			System.err.println("table.properties: not on classpath");
			System.exit(1);
		}

		// retrieve header and trailer
		String h = null, t = null;
		try {
			h = r.getString("header");
			t = r.getString("trailer");
		} catch (Exception e) {
			System.err
					.println("table.properties: cannot find 'header' and 'trailer'");
			System.exit(1);
		}
		header = h;
		trailer = t;
	}

	/** from configuration. */
	protected final GregorianCalendar startTime = new GregorianCalendar();

	/** team names from configuration. */
	protected final String[] teams;

	/** maps team name to table index. */
	protected final HashMap<String, Integer> record = new HashMap<String, Integer>();

	/** problem names from configuration. */
	protected final String[] problems;

	/** maps problem name to record index. */
	protected final HashMap<String, Integer> item = new HashMap<String, Integer>();

	/** holds current state. */
	protected final Record[] table;

	/** sort order: table[sort[0]] is winning team. */
	protected final Integer[] sort;

	/**
	 * process three lines (start-time, team-name ..., problem-name ...) from
	 * standard input.
	 */
	public Table() throws ParseException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		// first line of input: start time
		startTime.setTime(fmt.parse(in.readLine()));

		// second line of input: teams
		teams = in.readLine().split("\\s+");

		// third line of input: problems
		problems = in.readLine().split("\\s+");

		// populate items, problem name to record index
		for (int i = 0; i < problems.length; ++i)
			item.put(problems[i], i);

		// create table
		table = new Record[teams.length];
		sort = new Integer[table.length];

		for (int r = 0; r < teams.length; ++r) {
			// team name to table index
			record.put(teams[r], r);

			// table row
			table[r] = new Record(teams[r], problems.length);

			// configuration order
			sort[r] = r;
		}
		sort();
	}

	/** sort the table. */
	protected synchronized void sort() {
		Arrays.sort(sort, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return table[a].compareTo(table[b]);
			}

		});
	}

	/** return HTML for the table. */
	@Override
	public synchronized String toString() {
		StringBuilder s = new StringBuilder();

		s.append(header);

		// header with problem names
		s.append("<tr><th class='topleft'>");
		for (int p = 0; p < problems.length; ++p)
			s.append("<th class='problem' colspan='2'>").append(problems[p]);
		s.append("<th class='topright' colspan='2'>");

		// header with column titles
		s.append("<tr><th class='team'>Team");
		for (int p = 0; p < problems.length; ++p)
			s.append("<th class='elapsed'>Elapsed<th class='n'>n");
		s.append("<th class='total'>Total<th class='n'>n");

		// sorted records
		for (int r = 0; r < table.length; ++r)
			table[sort[r]].toString(s);

		s.append(trailer);

		return s.toString();
	}

	protected static class Record implements Comparable<Record> {

		/** team name. */
		protected final String name;

		/** elapsed time per problem. */
		protected final GregorianCalendar[] time;

		/** total elapsed time. */
		protected GregorianCalendar total;

		/** attempts per problem. */
		protected final int[] attempt;

		/** total problems solved. */
		protected int solved;

		/** problem pending? */
		protected final boolean[] pending;

		public Record(String name, int length) {
			this.name = name;
			this.time = new GregorianCalendar[length];
			this.attempt = new int[length];
			this.pending = new boolean[length];
		}

		/** return HTML for one record. */
		public void toString(StringBuilder s) {

			// row header: team name
			s.append("<tr><th class='teams'>").append(name);

			// elapsed time and attempts, if any
			for (int i = 0; i < time.length; ++i) {
				// time if any
				s.append("<td class='elapsed");
				if (pending[i])
					s.append("pending");
				s.append("'>");
				s.append(time[i] != null ? fmt.format(time[i].getTime()) : "-");

				// attempts if any
				s.append("<td class='n");
				if (pending[i])
					s.append("pending");
				s.append("'>");
				s.append(attempt[i] > 0 ? "" + attempt[i] : "-");
			}

			// total elapsed time and problems solved, if any
			s.append("<td class='total'>").append(
					total != null ? fmt.format(total.getTime()) : "-");
			s.append("<td class='n'>").append(solved > 0 ? solved : "-");
		}

		/** record job submission. */
		public void submit(Integer problem, GregorianCalendar time) {
			this.time[problem] = time;
			pending[problem] = true;
			attempt[problem]++;
		}

		/** record job judgement. */
		public void judged(Integer problem, GregorianCalendar newtime,
				boolean ok) {
			pending[problem] = false;
			
			//if no time is submitted 
			if (newtime == null) {
				if (ok) {
					++solved;
					if (total != null) {
						total.add(GregorianCalendar.HOUR_OF_DAY, time[problem]
								.get(GregorianCalendar.HOUR_OF_DAY));
						total.add(GregorianCalendar.MINUTE,
								time[problem].get(GregorianCalendar.MINUTE));
					} else
						total = time[problem];
				} else
					time[problem] = null;
			} else {
				if (ok) {
					++solved;
					++attempt[problem];
					this.time[problem] = newtime;
					if (total != null) {
						total.add(GregorianCalendar.HOUR_OF_DAY,
								newtime.get(GregorianCalendar.HOUR_OF_DAY));
						total.add(GregorianCalendar.MINUTE,
								newtime.get(GregorianCalendar.MINUTE));
					} else
						total = newtime;
				} else {
					time[problem] = newtime;
					++attempt[problem];
				}
			}
		}

		/** return less than zero if this is better than the other. */
		@Override
		public int compareTo(Record r) {
			// this solved more or fewer?
			if (solved != r.solved)
				return r.solved - solved;

			// solved something?
			if (solved > 0) {

				// this total is less or more?
				int c = total.compareTo(r.total);
				if (c != 0)
					return c;

				// this attempts are less or more?
				int a = attempts(), b = r.attempts();
				if (a != b)
					return a - b;
			}
			// compare team names
			return name.compareTo(r.name);
		}

		/** sum of attempts. */
		protected int attempts() {
			int result = 0;
			for (int i = 0; i < attempt.length; ++i)
				result += attempt[i];
			return result;
		}
	}

	/** record job submission. */
	public synchronized void submit(String updateteam, String updateproblem,
			Date updatetime) {
		Integer team = record.get(updateteam);
		Integer problem = item.get(updateproblem);
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(updatetime);

		if (team == null || problem == null || now.compareTo(startTime) < 0)
			throw new IllegalArgumentException("Invalid inputs : " + updateteam
					+ " " + updateproblem + " ");

		now.add(GregorianCalendar.HOUR_OF_DAY,
				-startTime.get(GregorianCalendar.HOUR_OF_DAY));
		now.add(GregorianCalendar.MINUTE,
				-startTime.get(GregorianCalendar.MINUTE));

		table[team].submit(problem, now);
		sort();
	}

	/** record job judgement. */
	public synchronized void judged(String updateteam, String updateproblem,
			boolean ok, Date updatetime) {
		Integer team = record.get(updateteam);
		Integer problem = item.get(updateproblem);

		if (team == null || problem == null)
			throw new IllegalArgumentException("Invalid inputs : " + updateteam
					+ " " + updateproblem + " ");

		if (updatetime == null)
			table[team].judged(problem, null, ok);
		else {
			GregorianCalendar now = new GregorianCalendar();
			now.setTime(updatetime);
			now.add(GregorianCalendar.HOUR_OF_DAY,
					-startTime.get(GregorianCalendar.HOUR_OF_DAY));
			now.add(GregorianCalendar.MINUTE,
					-startTime.get(GregorianCalendar.MINUTE));
			table[team].judged(problem, now, ok);
		}
		sort();
	}
}
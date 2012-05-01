import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;



/** run a script and show the output in different windows. */
public class OS {
	/** maps name to {@link Runner}. */
	protected static final Map<String,Runner> runners = new HashMap<String,Runner>();
	/** read script lines from standard input and send them to the runners;
      wait after each line. Empty lines and text from # on are ignored.

      <p>{@code regex = replacement}
      <p>defines a substitution which is applied to each subsequent line.
        These lines can be continued as for property files.

      <p>{@code name [@x,y[,width,height]] command}
      <p>starts a runner for a new name or sends the command line to a runner;
        the coordinates dimension the window.

      @param arg [0] if specified it should be a properties file
      with regular expression keys and string replacement values to define
      substitutions<br>
      [1] if specified should be input for the command loop (default is standard input). */
	public static void main (String... arg) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// properties?
		Properties macros = null;
		if (arg != null && arg.length > 0) {
			macros = new Properties(); macros.load(new FileInputStream(arg[0]));
			if (arg.length > 1)
				in = new BufferedReader(new FileReader(arg[1]));
		}
		// setup console
		Console console = new Console();
		// run command loop
		String line;
		while ((line = in.readLine()) != null) {
			// clip trailing #...
			{ 
				int n = line.indexOf("#");
				if (n >= 0) line = line.substring(0, n);
			}
			// ignore blank lines
			if (line.trim().length() == 0) continue;
			// substitution?
			{ 
				int n = line.indexOf("=");
				if (n > 0) {
					String[] s = { line.substring(0, n), line.substring(n+1) };
					// continue?
					while (s[1].endsWith("\\")) {
						if ((line = in.readLine()) == null)
							throw new IOException("eof in substitution");
						line = line.trim();
						if (line.startsWith("\\")) line = line.substring(1);
						s[1] = s[1].substring(0, s[1].length()-1) + line;
					}
					if (macros == null) macros = new Properties();
					macros.setProperty(s[0].trim(), s[1].trim());
					continue;
				}
			}
			// substitute?
			if (macros != null) line = replace(macros, line);
			// split off leading name
			String[] field = line.split("[ \t]+");
			String name = field[0];
			line = line.substring(name.length()).trim();
			// split off geometry @x,y[,width,height], if any
			int[] geometry = null;
			if (line.startsWith("@")) {
				String[] f = line.split("[ \t]+");
				line = line.substring(f[0].length()).trim();
				f = f[0].split(",");
				try {
					switch (f.length) {
					case 2: // @x,y
						geometry = new int[]{
								Integer.parseInt(f[0].substring(1)),
								Integer.parseInt(f[1])
						};
						break;
					case 4: // @x,y,width,height
						geometry = new int[]{
								Integer.parseInt(f[0].substring(1)),
								Integer.parseInt(f[1]),
								Integer.parseInt(f[2]),
								Integer.parseInt(f[3])
						};
						break;
					}
				} catch (NumberFormatException nfe) { }
			}
			// create or tell runner
			Runner runner = runners.get(name);
			if (runner == null)
				runners.put(name, runner = new Runner(geometry, field));
			else
				runner.tell(line);
			// display on console and wait
			console.println(name+" "+line);
		}
		// forcibly terminate on end of file
		exit();
	}
	/** text replacement -- verbatim and as long as possible. */
	public static String replace (Properties macros, String line) {
		String result;
		do {
			result = line;
			for (@SuppressWarnings("rawtypes") Map.Entry entry : macros.entrySet())
				line = line.replaceAll((String)entry.getKey(), Matcher.quoteReplacement((String)entry.getValue()));
		} while (!result.equals(line));
		return result;
	}
	/** terminate all runners and exit. */
	public static void exit () {
		for (Runner runner : runners.values()) runner.destroy();
		System.exit(0);
	}
}
/** console, shows each script line and continues on click.
    Terminates the system if closed. */
@SuppressWarnings("serial")
class Console extends Frame {
	protected final TextArea in = new TextArea(8, 64);
	protected final Button next = new Button("next");

	public Console () {
		super("Console"); // name as title

		add(in, BorderLayout.CENTER);
		in.setFont(new Font("Monospaced", Font.PLAIN, 12));

		Panel panel = new Panel();
		panel.add(next);
		add(panel, BorderLayout.SOUTH);

		next.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				synchronized(Console.this) { Console.this.notify(); }
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e) {
				OS.exit();
			}
		});

		pack();
		setVisible(true);
	}
	/** displays a line and waits for a button click. */
	public synchronized void println (String line) {
		in.append(line);
		try { wait(); } catch (InterruptedException e) { }
		in.append("\n");
	}  
}
/** runner, runs a process, sends lines to standard input
    and shows standard and diagnostic output.
    Closing the frame terminates the process. */
@SuppressWarnings("serial")
class Runner extends Frame {
	protected final TextArea out = new TextArea(8, 64);
	protected final Process process;
	protected final PrintWriter stdin;

	public Runner (int[] geo, String... arg) throws IOException {
		super(arg[0]); // name as title

		add(out, BorderLayout.CENTER);
		out.setFont(new Font("Monospaced", Font.PLAIN, 12));

		addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e) {
				Runner.this.setVisible(false);
				Runner.this.destroy();
			}
		});

		pack();
		if (geo != null) {
			setLocationByPlatform(false);
			if (geo.length > 2)
				setBounds(geo[0], geo[1], geo[2], geo[3]);
			else
				setLocation(geo[0], geo[1]);
		}
		setVisible(true);

		int arg0 = geo == null ? 1 : 2;
		ProcessBuilder pb = new ProcessBuilder(
				Arrays.asList(arg).subList(arg0, arg.length).toArray(new String[0])
		);
		pb.redirectErrorStream(true);
		process = pb.start();

		stdin = new PrintWriter(process.getOutputStream(), true);
		final InputStream stdout = process.getInputStream();

		Thread reader = new Thread() {
			public void run () {
				byte[] buf = new byte[128];
				int n;
				try {
					while ((n = stdout.read(buf)) > 0)
						out.append(new String(buf, 0, n));
					out.append("[eof]");
				} catch (IOException e) {
					out.append("\n["+e+"]");
				}
			}
		};
		reader.start();
	}
	/** use a thread to send a line to standard input of the process.
      <tt>[eof]</tt> represents end of file, i.e., closes stdin. */
	public void tell (final String line) {
		new Thread() {
			public void run () {
				if (line.trim().equals("[eof]"))
					stdin.close();
				else {
					stdin.println(line); stdin.flush();
				}
			}
		}.start();
	}
	/** forcibly terminate the process. */
	public void destroy () { process.destroy(); }
}
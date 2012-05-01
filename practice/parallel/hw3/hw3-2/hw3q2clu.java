/**
 * hw3q2clu.java
 *
 *
 * @version   $Id: hw3q2clu.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.rit.image.PJGColorImage;
import edu.rit.image.PJGImage;
import edu.rit.mp.IntegerBuf;
import edu.rit.pj.Comm;
import edu.rit.util.Arrays;
import edu.rit.util.Range;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         It is the parallel code for tranforming the image.
 * 
 */
public class hw3q2clu {
	public static int deltax; // transfer x to a new location
	public static int deltay; // transfer y to a new location
	public static String inputfilename; // inbput file name
	public static String outputfilename; // output file name
	public static int height; // height of the image
	public static int width; // width of the image
	public static int[][] matrix; // input matrix
	public static int[][] resultmatrix; // result matrix after tranformation
	public static Range[] ranges; // splitting into range of slices
	public static Range myrange; // processor's range
	public static int mylb; // current proc's lb
	public static int myub; // current proc's u
	public static IntegerBuf[] slices;
	public static IntegerBuf myslice;

	// cluster details
	public static Comm world;
	public static int size;
	public static int rank;

	/**
	 * Main program
	 * 
	 * @param args
	 *            - deltax, deltaym output file, input file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		if (!(args.length == 4))
			usage();
		inputfilename = args[0];
		deltax = Integer.parseInt(args[1]);
		deltay = Integer.parseInt(args[2]);
		outputfilename = args[3];

		Comm.init(args); // run using scheduler
		world = Comm.world();
		size = world.size();
		rank = world.rank();

		// reading the image
		long t1 = System.currentTimeMillis();
		PJGColorImage image = (PJGColorImage) PJGImage
				.readFromStream(new BufferedInputStream(new FileInputStream(
						inputfilename)));
		// details about the image
		height = image.getHeight();
		width = image.getWidth();
		matrix = image.getMatrix();

		// result matrix after transformation
		resultmatrix = new int[height][];
		ranges = new Range(0, height - 1).subranges(size);
		myrange = ranges[rank];
		mylb = myrange.lb();
		myub = myrange.ub();

		if (rank == 0) {
			Arrays.allocate(resultmatrix, width);
		} else {
			Arrays.allocate(resultmatrix, myrange, width);
		}

		slices = IntegerBuf.rowSliceBuffers(resultmatrix, ranges);
		myslice = slices[rank];

		// tranforming the pixel data
		for (int y = mylb; y <= myub; y++) {
			int ynew = (y - deltay) % height;
			while (ynew < 0)
				ynew = ynew + height;
			int[] resultmatrix_r = resultmatrix[y];
			for (int x = 0; x < width; x++) {
				int xnew = (x - deltax) % width;
				while (xnew < 0)
					xnew = xnew + width;
				resultmatrix_r[x] = matrix[ynew][xnew];
			}
		}

		world.gather(0, myslice, slices);
		long t2 = System.currentTimeMillis();
		if (rank == 0) {
			System.out.println((t2 - t1) + " msec");
			// resulting image creation
			image = new PJGColorImage(height, width, resultmatrix);
			PJGImage.Writer writer = image
					.prepareToWrite(new BufferedOutputStream(
							new FileOutputStream(outputfilename)));
			writer.write();
			writer.close();
		}
	}

	/**
	 * Method to show the usage
	 */
	private static void usage() {
		System.err
				.println("java hw3q2seq <inputfile> <height> <width> <outputfile> ");
		System.exit(1);
	}
}

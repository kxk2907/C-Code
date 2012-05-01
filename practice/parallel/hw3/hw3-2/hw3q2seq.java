/**
 * hw3q2seq.java
 *
 *
 * @version   $Id: hw3q2seq.java,v 1.6 
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
import edu.rit.pj.Comm;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 * It is the sequential code for tranforming the image.
 *
 */
public class hw3q2seq {
	public static int deltax;					//transfer x to a new location
	public static int deltay;					//transfer y to a new location
	public static String inputfilename;			//input file name
	public static String outputfilename;		//output file name
	public static int height;					//height and wdith of the file
	public static int width;
	public static int[][] matrix;				//input and output matrix
	public static int[][] resultmatrix;

	/**
	 * input arguments
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		if (!(args.length == 4))	//check the usage
			usage();
		inputfilename = args[0];
		deltax = Integer.parseInt(args[1]);
		deltay = Integer.parseInt(args[2]);
		outputfilename = args[3];

		Comm.init(args);			//run using scheduler
		long t1 = System.currentTimeMillis();
		//reading the image
		PJGColorImage image = (PJGColorImage) PJGImage
				.readFromStream(new BufferedInputStream(new FileInputStream(
						inputfilename)));
		height = image.getHeight();		//details about the image
		width = image.getWidth();
		matrix = image.getMatrix();
		resultmatrix = new int[height][width]; //result matrix after transformation

		// tranforming the pixel data
		for (int y = 0; y < height; y++) {
			int ynew = (y + deltay) % height;
			for (int x = 0; x < width; x++) {
				int xnew = (x + deltax) % width;
				resultmatrix[ynew][xnew] = matrix[y][x];
			}
		}

		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
		//resulting image creation
		image = new PJGColorImage(height, width, resultmatrix);
		PJGImage.Writer writer = image.prepareToWrite(new BufferedOutputStream(
				new FileOutputStream(outputfilename)));
		writer.write();
		writer.close();
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

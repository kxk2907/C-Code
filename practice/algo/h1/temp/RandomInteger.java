import java.util.Random;

public class RandomInteger {
  public static void main(String args[]){
    System.out.println(args[0]);
    int n = Integer.parseInt(args[0]);
    int sqr = n*n;
    Random randomGenerator = new Random();
    for (int idx = 1; idx <= n; ++idx){
      int randomInt = randomGenerator.nextInt(sqr);
      System.out.print(randomInt + " ");
    }
     System.out.println();
  }
}

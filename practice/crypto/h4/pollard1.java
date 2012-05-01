import java.math.BigInteger;

public class pollard1 {
	public static void main(String[] args) {
		
		BigInteger n = new BigInteger(new String("618240007109027021"));
		Integer B = 1000;
		BigInteger[] vals = new BigInteger[2];   // 0 - p or d ; 1 - smallestB
		
		System.out.println("Factorization For n : " + n);
		primeFactor(B,vals,n);
		System.out.println("p  : " + vals[0]);
		System.out.println("B0 : " + vals[1]);
		
		System.out.println("Foctorization for (p-1) : " + vals[0].subtract(new BigInteger(new String("1"))));
		BigInteger[] vals2 = new BigInteger[2];
		primeFactor(B,vals2,vals[0].subtract(new BigInteger(new String("1"))));
		System.out.println("Factor of p-1 : " + vals2[0]);
		System.out.println("B0 of p1-1    : " + vals2[1]);
		
		BigInteger[] fact = new BigInteger[Integer.parseInt(vals[1].toString()) + 1];
		factorial(fact);
		System.out.println("Factorial B0! : " + fact[Integer.parseInt(vals[1].toString())]);
		System.out.println(" (p - 1) | B0! : " + fact[Integer.parseInt(vals[1].toString())].divide(vals[0].subtract(new BigInteger(new String("1")))));
	}
	
	public static void factorial(BigInteger[] fact) {
		fact[1] = new BigInteger(new String("1"));
		for(int i = 2; i< fact.length;i++) 
			fact[i] = fact[i-1].multiply(new BigInteger(""+i));
	}
	
	public static void primeFactor(Integer B,BigInteger[] ret,BigInteger n) {
		BigInteger b = new BigInteger(new String("2"));
		boolean flag = false;
	//	for (int j = 2; j <= 10000; j++) {
			double p = 2469135821;
			b = new BigInteger(new String("2"));
			for (double q = 2; q <= p; q++)
				b = b.pow(i).mod(n);
			b = b.subtract(new BigInteger(new String("1")));
			BigInteger d = b.gcd(n);
			if (d.compareTo(new BigInteger(new String("1"))) == 1
					&& d.compareTo(n) == -1) {
				if(!flag) {
					ret[0] =  d;//p = d;
					ret[1] = new BigInteger(new String(B.toString()));//smallestB = B;
					flag = true;
				}
				//System.out.println("B: " + B + " d : " + d);
			}
			else {
				System.out.println("B: " + B + " Failure!!");
			}
	//	}
	}
}

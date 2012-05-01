import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class BF<E> implements Serializable {
	private int totalElements;
	private int[] bitSetArray;
	private int bitSetSize;
	private final int expectedElements;
	private final double bitPerElement;
	private int numberOfHashes;
	private LocalHashTable<E>[] hashTable;
	
	static final String hashName = "MD5";
    static final MessageDigest digestFunction;
    static final Charset charset = Charset.forName("UTF-8");

    static { 
        MessageDigest tmp;
        try {
            tmp = java.security.MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException e) {	
            tmp = null;
        }
        digestFunction = tmp;
    }
    
	@SuppressWarnings("unchecked")
	public BF(double c, int n, int k) {
    	this.bitPerElement = c;
    	this.expectedElements = n;
    	this.numberOfHashes = k;
    	this.bitSetSize = (int)Math.ceil(this.bitPerElement * this.expectedElements);
    	this.bitSetArray = new int[this.bitSetSize];
    	this.hashTable = new LocalHashTable[this.bitSetSize];
    	this.totalElements = 0;
    }
    
    public void add(E element) {
        add(element.toString().getBytes(charset),element);
    }
    
    public boolean lookup(E element) {
    	return lookup(element.toString().getBytes(charset), element);
    }
    
	public void add(byte[] bytes, E element) {
    	int[] hashes = createHashes(bytes, this.numberOfHashes);
    	for (int hash : hashes) {
        	int location = Math.abs(hash % this.bitSetSize);
        	++bitSetArray[location];
    	}
    	int[] hash = createHashes(bytes, 1);	//To put into hashtable
    	int abs = Math.abs(hash[0] % this.bitSetSize);
    	if(hashTable[abs] == null) 
    		hashTable[abs] = new LocalHashTable<E>();
    	hashTable[abs].add(element);
    	++this.totalElements;
    }
    
	public boolean lookup(byte[] bytes, E element) {
		int[] hashes = createHashes(bytes, this.numberOfHashes);
		for(int hash:hashes) {
			int location = Math.abs(hash % this.bitSetSize);
			if(bitSetArray[location] == 0)
				return false;
		}
		
		//int[] hash = createHashes(bytes,1);
		int abs = Math.abs(hashes[0] % this.bitSetSize);
		if(hashTable[abs] == null) {
			//System.out.println("False positive !!");
			return false;
		}
		else 
			return hashTable[abs].lookup(element);
	}
	
    public static int[] createHashes(byte[] data, int hashes) {
        int[] result = new int[hashes];

        int k = 0;
        byte salt = 0;
        while (k < hashes) {
            byte[] digest;
            synchronized (digestFunction) {
                digestFunction.update(salt);
                salt++;
                digest = digestFunction.digest(data);                
            }
        
            for (int i = 0; i < digest.length/4 && k < hashes; i++) {
                int h = 0;
                for (int j = (i*4); j < (i*4)+4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[k] = h;
                k++;
            }
        }
        return result;
    }
}


class LocalHashTable<E> {
	private ArrayList<E> bucket;
	
	public LocalHashTable() {
		this.bucket = new ArrayList<E>();
	}
	
	public void add(E element) {
		this.bucket.add(element);
	}
	
	public boolean remove(E element) {
		return this.bucket.remove(element);
	}
	
	public boolean lookup(E element) {
		for(int i = 0;i<bucket.size();i++) {
			if(bucket.get(i).equals(element))
				return true;
		}
		return false;
	}
}

/**
 * Illustrate Abstract Class
 *
 *
 * @version   $Id: AnimalBehavior.java,v 1.1 2010/10/03 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) and Nipun Sud
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

/*
 * Main class to create objects for the class inherited from the abstract class 
 * Animals doesn't have a unique behavior, infact Dog and Lion which extends from Animal
 */
public class AnimalBehavior {
	public static void main(String args[]) {
		Animal d1 = new Dog("Bruno");
		Animal l1 = new Lion("Simba");
		d1.eat();
		d1.sleeps();
		((Dog) d1).barks();
		System.out.println();
		l1.eat();
		l1.sleeps();
		((Lion) l1).roars();
	}
}

/*
 * Abstract class where objects cannot be created, 
 * eat and sleep methods are normal behaviors of animal, it shud be defined so interface can't be used.
 */
abstract class Animal {
	String name;
	
	Animal(String name) {
		this.name = name;
	}
	void eat() {
		System.out.println(name + " eats !!");	
	}
	void sleeps() {
		System.out.println(name + " sleeps !!");	
	}
	String getName() {
		return name;
	}
}

/*
 * Dog barks .. that explains
 */
class Dog extends Animal{
	public Dog(String name) {
		super(name);
	}
	public void barks() {
		System.out.println(getName()+ " barks !! because it is a " + this.getClass().getName());
	}
}

/*
 * Lion Roars that explains
 */
class Lion extends Animal {
	public Lion(String name) {
		super(name);
	}
	public void roars() {
		System.out.println(getName()+ " roars !! because it is a " + this.getClass().getName());
	}
}



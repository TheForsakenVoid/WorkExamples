package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
        Node head = null;
        Node ptr1 = null;
        Node ptr2 = null;
        if(poly1 == null && poly2 == null){
            return null;
        }
        while (poly1 != null && poly2 != null){
            if(poly1.term.degree == poly2.term.degree){
                ptr2 = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree, null);
                if(ptr1 == null){
                    head = ptr2;
                }else{
                    ptr1.next = ptr2;
                }
                ptr1 = ptr2;
                poly1 = poly1.next;
                poly2 = poly2.next;
            }else if(poly2.term.degree < poly1.term.degree){
                ptr2 = new Node(poly2.term.coeff, poly2.term.degree, null);
                if(ptr1 == null){
                    head = ptr2;
                }else{
                    ptr1.next = ptr2;
                }
                ptr1 = ptr2;
                poly2 = poly2.next;
            }
            else if(poly1.term.degree < poly2.term.degree){
                ptr2 = new Node(poly1.term.coeff, poly1.term.degree, null);
                if(ptr1 == null){
                    head = ptr2;
                }else{
                    ptr1.next = ptr2;
                }
                ptr1 = ptr2;
                poly1 = poly1.next;
            }
        }
			if(poly1 != null){
			            while (poly1 != null){
			                ptr2 = new Node(poly1.term.coeff, poly1.term.degree, null);
			                if(ptr1 == null){
			                    head = ptr2;
			                }else{
			                    ptr1.next = ptr2;
			                }
			                ptr1 = ptr2;
			                poly1 = poly1.next;
			            }
			        }
			else if(poly2 != null){
			            while(poly2 != null){
			                ptr2 = new Node(poly2.term.coeff, poly2.term.degree, null);
			                if(ptr1 == null){
			                    head = ptr2;
			                }
			                else{
			                    ptr1.next = ptr2;
			                }
			                ptr1 = ptr2;
			                poly2 = poly2.next;
			            }
			        }
			        Node prev = null;
			        ptr2 = head;
			        while(ptr2 != null){
			            if(ptr2.term.coeff == 0){
			                if(ptr2 == head){
			                    head = ptr2.next;
			                }else{
			                    prev.next = ptr2.next;
			                }
			            }else{
			                prev = ptr2;
			            }
			            ptr2 = ptr2.next;
			        }
        return head;
    }

	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		float postCoeff;
		int postExponent;
		Node head=null;
		Node ptr1=null;
		Node product=null;
		if(poly1==null||poly2==null) {
			return head;
		}
			for(Node ptr2=poly2; ptr2 !=null; ptr2=ptr2.next) {
				for(Node ptr3=poly1; ptr3 !=null;ptr3= ptr3.next) {
					postCoeff = ptr3.term.coeff*ptr2.term.coeff;
					postExponent = ptr3.term.degree+ptr2.term.degree;
					Node ptr = new Node(postCoeff, postExponent, null);
					if(ptr1==null) {
						head=ptr;
					}
					else {
						ptr1.next = ptr;
					}
					ptr1=ptr;
				}
				product=add(product,head);
				head=null;
				ptr1=null;
			}
			return product;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		double sum=0;
		if(poly==null) {
			return(float)sum;
		}
		else {
			Node ptr=poly;
			while(ptr != null) {
				double var=ptr.term.coeff*Math.pow(x, ptr.term.degree);
				sum= sum+var;
				ptr=ptr.next;
			}
			return(float)sum;
		}
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}

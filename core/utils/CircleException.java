package BNlearning.core.utils;


public class CircleException extends Exception{
	public CircleException() {
		// TODO Auto-generated constructor stub
          super();	
	} 
	static void throwOne() throws CircleException{
		 throw new CircleException();
	 }
}
package server;

/**
 * Eccezione per gestire il caso di acquisizione di valore mancante o fuori range di un 
 * attributo di un nuovo esempio da classificare.
 */

@SuppressWarnings("serial")
public class UnknownValueException extends Exception{
	
	public UnknownValueException (String msgError){
		super (msgError);
	}

}

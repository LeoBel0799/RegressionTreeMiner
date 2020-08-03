package data;

/**
 * Eccezione definita per gestire il caso di acquisizione errata del Training set (file inesistente, schema mancante,
 * training set vuoto o training set privo di variabile target numerica).
 * 
 */
@SuppressWarnings("serial")
public class TrainingDataException extends Exception {
	
	public TrainingDataException (String msgError) {
		super (msgError);
	}
}

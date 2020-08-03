package tree;
import java.io.Serializable;

import data.Data;

/**
 * Modella l'entit� nodo fogliare.
 * 
 */

@SuppressWarnings("serial")
public class LeafNode extends Node implements Serializable{

	private Double predictedClassValue; // valore dell'attributo di classe espresso nella foglia corrente
	
	/**
	 * istanzia un oggetto invocando il costruttore della superclasse e avvalora l'attributo predictedClassValue 
	 * (come media dei valori dell�attributo di classe che ricadono nella partizione---ossia la porzione di trainingSet 
	 * compresa tra beginExampelIndex e endExampelIndex )
	 * 
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 */
	 LeafNode (Data trainingSet, int beginExampleIndex, int endExampleIndex){
		
		super (trainingSet,  beginExampleIndex,  endExampleIndex);
		Double sum = 0.0;
		Double mean = 0.0;
		
		for (int i=beginExampleIndex; i<=endExampleIndex; i++) {
			sum += trainingSet.getClassValue(i);
		}
		
		mean = sum/ (endExampleIndex - beginExampleIndex +1);
		predictedClassValue = mean;
	}
	
	
	 /** Restituisce il valore del membro predictedClassValue.
	 * 
	 * @return Media dei valori dell'attributo di classe che ricadono nella
	 *         partizione.
	 */
	public Double getPredictedClassValue() {		
		return predictedClassValue;
	}

	/**
	 * Restituisce il numero di split generati dal nodo foglia ,
	 * ovvero 0.
	 */
	int getNumberOfChildren() {	
		return 0;
	}
	
	/**
	 * Invoca il metodo della superclasse assegnando anche il valore di classe
	 * della foglia.
	 */
	public String toString() {		
		return "LEAF : class =" + predictedClassValue + "  " + super.toString();
	}

}




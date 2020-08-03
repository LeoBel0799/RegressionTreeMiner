package tree;

import data.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;

/**
 * Classe astratta SplitNode che modella l'astrazione dell'entit� nodo di split (continuo o discreto) che estende la superclasse Node
 * 
 */
@SuppressWarnings("serial")
public
abstract class SplitNode extends Node implements Comparable <SplitNode>, Serializable{


	/**
	 * Classe che aggrega tutte le informazioni riguardanti un nodo di split
	 */

	class SplitInfo implements Serializable{ 
		protected Object splitValue; 		// valore di tipo Object (di un attributo indipendente) che definisce uno split
		protected int beginIndex;
		protected int endIndex;
		int numberChild;					// numero di split (nodi figli) originanti dal nodo corrente
		String comparator="=";				// operatore matematico che definisce il test nel nodo corrente (�=� per valori discreti)

		/**
		 * Costruttore che avvalora gli attributi di classe per split a valori discreti
		 * @param splitValue
		 * @param beginIndex
		 * @param endIndex
		 * @param numberChild
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild){ 
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
		}

	
		/**
		 * Costruttore che avvalora gli attributi di classe per generici split (da usare per valori continui)
		 * @param splitValue
		 * @param beginIndex
		 * @param endIndex
		 * @param numberChild
		 * @param comparator
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild, String comparator){ 
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
			this.comparator=comparator;
		}


		/**
		 * Restituisce il valore del membro getbeginIndex 
		 * 
		 * @return 
		 * 		indice del primo esempio del sotto-insieme rispetto al training set complessivo
		 */
		 int getBeginindex(){
			return beginIndex;			
		}

		/**
		 * Restituisce il valore del membro getEndIndex 
		 *  
		 * @return 
		 * 		indice dell' ultimo esempio del sotto-insieme rispetto al training set complessivo
		 */

		 int getEndIndex(){
			return endIndex;
		}


		/**
		 * Restituisce il valore dello split
		 */
		 Object getSplitValue(){
			return splitValue;
		}



		/**
		 * Concatena in un oggetto String i valori di beginExampleIndex,endExampleIndex, variance e restituisce la stringa finale.
		 */
		public String toString(){		

			return "Child " + numberChild +" split value"+comparator+splitValue + " [Examples:"+beginIndex+"-"+endIndex+"]";
		}


		/*
		 * Restituisce il valore dell'operatore matematico che definisce il test
		 */
		public String getComparator(){ 
			return comparator;
		}
	}


	private Attribute attribute;			//oggetto Attribute che modella l'attributo indipendente sul quale lo split � generato
	protected List<SplitInfo> mapSplit=new ArrayList<SplitInfo> (); // Lista per memorizzare gli split candidati in una struttura dati di dimensione pari ai possibili valori di test
	protected double splitVariance;			//attributo che contiene il valore di varianza a seguuito del partizionamento indotto dallo split corrente

	/**
	 * Metodo abstract per generare le informazioni necessarie per ciascuno
	 * degli split candidati.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampelIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 * @param attribute
	 *            Attributo sul quale si definisce lo split.
	 */

	abstract void setSplitInfo(Data trainingSet,int beginExampelIndex, int endExampleIndex, Attribute attribute); 



	/**
	 * Metodo abstract per modellare la condizione di test (ad ogni valorfe di test c'� un ramo dallo split).
	 * 
	 * @param value
	 *            Valore dell'attributo che si vuole testare rispetto a tutti
	 *            gli split.
	 */

	abstract int testCondition (Object value);		


	
	/** Invoca il costruttore della super-classe , ordina i valori dell'attributo
	 * di input per gli esempi contenuti nel sotto-insieme di training compreso
	 * tra beginExampleIndex e endExampleIndex , e sfrutta questo ordinamento
	 * per determinare i possibili split e popolare la lista mapSplit. Computa
	 * la varianza per l'attributo usato nello split sulla base del
	 * partizionamento indotto dallo split.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param beginExampleIndex
	 *            Indice iniziale del sotto-insieme di training.
	 * @param endExampleIndex
	 *            Indice finale del sotto-insieme di training.
	 * @param attribute
	 *            Attributo indipendente sul quale definire lo split.
	 */
	SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute){

		super(trainingSet, beginExampleIndex,endExampleIndex);
		this.attribute=attribute;
		trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order by attribute
		setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);

		//compute variance
		splitVariance = 0;
		for (int i = 0; i < mapSplit.size(); i++) {
			double localVariance = new LeafNode(trainingSet, mapSplit.get(i).getBeginindex(), mapSplit.get(i).getEndIndex()).getVariance();
			splitVariance += (localVariance);
		}
	}

	
	/**
	 * Restituisce l'oggetto per l'attributo usato per lo split.
	 * 
	 * @return Attributo usato per lo split.
	 * @return 0 splitVariance uguali , -1 splitVariance minore , 1
	 *         splitVariance minore.
	 **/
	public int compareTo(SplitNode o) {
		if (o.getVariance() == this.splitVariance)
			return 0;
		else if (o.getVariance() < this.splitVariance)
			return -1;
		else
			return 1;
	}

	/**
	 * Restituisce l'oggetto per l'attributo usato per lo split
	 * 
	 * @return Attributo usato per lo split
	 */
	Attribute getAttribute(){		
		return attribute;
	}


	/**
	 * Restituisce l'information gain per lo split corrente.
	 **/
	 double getVariance(){		
		return splitVariance;
	}




	/**
	 * Restituisce il numero dei rami originati dal nodo corrente
	 * 
	 * @return 
	 * 			valore del numero di nodi sottostanti
	 **/
	public int getNumberOfChildren(){		
		return mapSplit.size();
	}


	/**
	 * Restituisce le informazioni per il ramo in mapSplit[] indicizzato da child.
	 * @param child
	 * @return
	 * 		Informazioni del ramo indicizzato
	 **/		
	SplitInfo getSplitInfo(int child){		
		return mapSplit.get(child); 
	}



	/**
	 * Concatena le informazioni di ciascuno test (attributo, operatore e valore) in una String finale. Necessario per la predizione di nuovi esempi
	 **/
	public String formulateQuery(){			
		String query = "";
		for (int i = 0; i < mapSplit.size(); i++)
			query += (i + ":" + attribute + mapSplit.get(i).getComparator() + mapSplit.get(i).getSplitValue())+ "\n";
		return query;
	}




	/**
	 * Concatena le informazioni di ciascuno test (attributo, esempi coperti, varianza, varianza di Split) in una String finale.
	 **/
	public String toString(){		
		String v= "SPLIT : attribute=" +attribute+ " "  + super.toString()+  " Split Variance: " + getVariance()+ "\n" ;

		for(int i=0;i<mapSplit.size();i++){
			v+= "\t"+mapSplit.get(i)+"\n";
		}

		return v;
	}
}
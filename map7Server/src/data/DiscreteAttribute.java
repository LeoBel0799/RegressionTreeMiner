package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 	Estende la classe Attribute e rappresenta un attributo discreto
 * 
 */

@SuppressWarnings("serial")
public class DiscreteAttribute extends Attribute implements Serializable, Iterable<String> { 

	private Set<String> values=new TreeSet<String>();

	
	/**Invoca il costruttore della super-classe e avvalora il Set con i valori ricevuti in input
	 * 
	 * @param name  Nome simbolico dell'attributo.
	 * @param index Identidicativo numerico dell'attributo.
	 * @param values Insieme dei valori discreti che l'attributo pu� assumere.
	 */
	public DiscreteAttribute (String name, int index, Set<String> values) {
		super(name, index);
		this.values=values;
	}


	/**
	 *  Restituisce la cardinalit� del Set values.
	 *  
	 * @return cardinalit� del'insieme
	 */
	public int getNumberOfDistinctValues () {
		return values.size();
	}


	/*public String getValue (int i)			/*Input: indice di un solo valore discreto rispetto all'array values[]
											Output: valore discreto con indice il parametro di input
											Comportamento: Restituisce il valore dell'elemento i dell'array values[]*/
	//{
	//return values[i];
	//Restituisce il valore dell'elemento i dell'array values[]
	//}

	public String toString() {
		String s;
		s=getName();
		return s;
	}

	/**
	 * Restituisce l'iteratore per l'insieme di valori.
	 */
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
}

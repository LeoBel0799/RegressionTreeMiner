package data;

import java.io.Serializable;

/**
 * Estende la classe Attribute e rappresenta un attributo Continuo.
 *
 */

@SuppressWarnings("serial")
public class ContinuosAttribute extends Attribute implements Serializable {

	/**
	 * Invoca il costruttore della super-classe per avvalorare gli attributi
	 * name ed index.
	 * 
	 * @param name Nome simbolico dell'attributo.
	 * @param index Identificativo numerico dell'attributo.
	 * 
	 */
	
	public ContinuosAttribute (String name, int index){
		super (name,index);
	}
	
	public String toString() {
		String s;
		s=getName();
		return s;
	}
}

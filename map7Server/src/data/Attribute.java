package data;

import java.io.Serializable;

/**
 * La classe modella un generico attributo discreto o continuo.
 * 
 */

@SuppressWarnings("serial")
public abstract class Attribute implements Serializable {
	
	protected String name;
	protected int index; 
	
	/**
	 * E' il costruttore di classe. Inizializza i valori dei membri name, index
	 * @param name
	 * @param index
	 */
	
	public Attribute (String name, int index) {
		this.name=name;
		this.index=index;
	}
	
	
	/**
	 * @return il valore del membro name;
	 */
	public String getName(){ 
		return name;
	}
	
	/**
	 * Restituisce il valore nel membro index
	 * @return identificativo numerico dell'attributo
	 */
	
	public int getIndex(){
		return index;
	}
}

package tree;


import java.io.FileInputStream;  
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeSet;
import data.Attribute;
import data.ContinuosAttribute;
import data.Data;  
import data.DiscreteAttribute;
import server.UnknownValueException;


/**
 * 
 * Modella l'entit� dell'intero albero di regressione come un insieme di sotto-alberi.
 *
 */
@SuppressWarnings("serial")
public class RegressionTree implements Serializable{

	private Node root; // radice del sotto-albero corrente
	private RegressionTree childTree[]; // array di sotto-alberi originanti nel nodo root:vi � un elemento nell�array per ogni figlio del nodo


	/**
	 * Istanzia un sotto-albero dell'intero albero
	 */
	RegressionTree (){
		
	}


	/**
	 * Istanzia un sotto-albero dell'intero albero e avvia l'induzione
	 * dell'albero dagli esempi di training in input.
	 * 
	 * @param trainingSet
	 * 	Training set complessivo.
	 * 
	 */

	public RegressionTree(Data trainingSet){

		learnTree(trainingSet,0,trainingSet.getNumberOfExamples()-1,trainingSet.getNumberOfExamples()*10/100);
	}



	/**
	 * Verifica se il sotto-insieme corrente pu� essere coperto da un nodo foglia controllando che il numero di esempi del
	 * training set compresi tra begin ed end sia minore uguale di numberOfExamplesPerLeaf.	
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param begin
	 *            Indice iniziale del sotto-insieme di training.
	 * @param end
	 *            Indice finale del sotto-insieme di training.
	 * @param numberOfExamplesPerLeaf
	 *            Numero minimo che una foglia deve contenere.
	 * @return False se la dimensione del sotto-insieme di training � maggiore
	 *         del numero minimo , True nel caso contrario.
	 */

	boolean isLeaf (Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf) {

		int dim = end - begin;
		if (dim <= numberOfExamplesPerLeaf) {
			return true;
		}else return false;
	}


	/**
	 * Per ciascun attributo indipendente istanzia il DiscreteNode associato e seleziona il nodo di split con minore varianza tra i DiscreteNode istanziati. Ordina la porzione di trainingSet 
	 * corrente (tra begin ed end) rispetto all�attributo indipendente del nodo di split selezionato. Restituisce il nodo selezionato.
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param begin
	 *            Indice iniziale del sotto-insieme di training.
	 * @param end
	 *            Indice finale del sotto-insieme di training.
	 * @return Nodo migliore di split per il sotto-insieme di training corrente.
	 */

	SplitNode determineBestSplitNode (Data trainingSet, int begin, int end) {

		TreeSet<SplitNode> ts = new TreeSet<SplitNode>();
		for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
			SplitNode current = null;
			Attribute a = trainingSet.getExplanatoryAttribute(i);
			if (a instanceof DiscreteAttribute)
				current = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) a);
			else if (a instanceof ContinuosAttribute)
				current = new ContinuosNode(trainingSet, begin, end, (ContinuosAttribute) a);
			ts.add(current);
		}
		trainingSet.sort(ts.last().getAttribute(), begin, end);
		return ts.last();
	}
	

	/**
	 * Scandisce ciascun ramo dell'albero completo dalla radice alla foglia
	 * concatenando le informazioni dei nodi di split fino al nodo foglia. Se il
	 * nodo root � di split discende ricorsivamente l'albero per ottenere le
	 * informazioni del nodo sottostante , di ogni ramo-regola , se � di foglia
	 * termina l'attraversamento visualizzando la regola.
	 */
	public String printRules(){
		Node currentNode = root;
		String current = ""; //
		String finalString = "\n********* RULES **********\n";

		//Inizializzo la stringa con l'intestazione

		if(currentNode instanceof LeafNode) {
			System.out.println(((LeafNode) currentNode).toString());
		} else {
			current += ((SplitNode) currentNode).getAttribute().getName();
			for(int i = 0; i < childTree.length; i++) { //PRIMA CHIAMATA RICORSIVA: current = "X=A" (esempio)
				finalString += childTree[i].printRules(current + ((SplitNode) currentNode).getSplitInfo(i).getComparator() + ((SplitNode) currentNode).getSplitInfo(i).getSplitValue());
			}
		}

		finalString+= "*************************\n"; //aggiungo alla stringa finale la coda
		System.out.println(finalString);
		return finalString;
	}


	/**
	 * Supporta il metodo printRules(). Concatena alle informazioni in current del precedente nodo quelle del nodo root del corrente sotto-albero: 
	 * se il nodo corrente � di split il metodo viene invocato ricorsivamente con current e le informazioni del nodo corrente, 
	 * se � di fogliare  visualizza tutte le informazioni concatenate.	
	 * 
	 * @param current
	 *  Informazioni del nodo di split del sotto-albero al livello superiore.
	 *  
	 */

	private String printRules(String current){
		Node currentNode = root;
		String finalString = "";

		if(currentNode instanceof LeafNode) { //Sono ad un nodo foglia. termino la riga e la ritorno
			return current + "  ==> Class = " + ((LeafNode) currentNode).getPredictedClassValue() + "\n";
		}  else {
			current += " AND " + ((SplitNode) currentNode).getAttribute().getName();
			for(int i = 0; i < childTree.length; i++) {
				finalString += childTree[i].printRules(current + ((SplitNode) currentNode).getSplitInfo(i).getComparator() + ((SplitNode) currentNode).getSplitInfo(i).getSplitValue());
			}
		}

		return finalString;

	}

	


	/**
	 * Genera un sotto-albero con il sotto-insieme di input istanziando un nodo
	 * fogliare o un nodo di split. In tal caso determina il miglior nodo
	 * rispetto al sotto-insieme di input, ed a tale nodo esso associa un
	 * sotto-albero avente radice il nodo medesimo e avente un numero di rami
	 * pari al numero dei figli determinati dallo split. Ricorsivamente ogni
	 * oggetto RegressionTree in childTree[] sar� re-invocato il metodo
	 * learnTree() per l'apprendimento su un insieme del ridotto del
	 * sotto-insieme attuale. Nella condizione in cui il nodo di split non
	 * origina figli , il nodo diventa fogliare.
	 * 
	 * 
	 * @param trainingSet
	 *            Training set complessivo.
	 * @param begin
	 *            Indice iniziale del sotto-insieme di training.
	 * @param end
	 *            Indice finale del sotto-insieme di training.
	 * @param numberOfExamplesPerLeaf
	 *            Numero massimo che una foglia deve contenere.
	 */

	private void learnTree(Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf){
	
		if( isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)){
			//determina la classe che compare pi� frequentemente nella partizione corrente
			root=new LeafNode(trainingSet,begin,end);
		}
		else //split node
		{
			root=determineBestSplitNode(trainingSet, begin, end);

			if(root.getNumberOfChildren()>1){
				childTree=new RegressionTree[root.getNumberOfChildren()];
				for(int i=0;i<root.getNumberOfChildren();i++){
					childTree[i]=new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode)root).getSplitInfo(i).beginIndex, ((SplitNode)root).getSplitInfo(i).endIndex, numberOfExamplesPerLeaf);
				}
			}
			else
				root=new LeafNode(trainingSet,begin,end);

		}
	}


	/**
	 * Invia al client le informazioni di ciascuno split dell'albero e per il
	 * corrispondente attributo acquisisce il valore da predire inviato dal
	 * client.Se il nodo root corrente � foglia termina l'acquisizione e invia
	 * al client la predizione per l'attributo di classe, altrimenti invoca
	 * ricorsivamente sul figlio di root in childTree[] individuato dal valore
	 * inviato dal client.
	 * 
	 * @param tree
	 *            Albero di regressione su cui effettuare la predizione.
	 * @return Oggetto Double contenente il valore di classe predetto per
	 *         l'esempio acquisito.
	 * @throws UnknownValueException
	 *             L'eccezione viene sollevata quando quando la risposta
	 *             ricevuta dal client non permetta di selezionare un ramo
	 *             valido del nodo di split.
	 */
	public void predictClass(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) throws IOException, UnknownValueException, ClassNotFoundException {

		if (root instanceof LeafNode) {
			objectOutputStream.writeObject("OK");
			objectOutputStream.writeObject(((LeafNode)root).getPredictedClassValue().toString());
		} else {
			int risp;
			objectOutputStream.writeObject("QUERY");
			objectOutputStream.writeObject(((SplitNode) root).formulateQuery());
			risp = Integer.parseInt(objectInputStream.readObject().toString());
			if (risp == -1 || risp >= root.getNumberOfChildren())
				throw new UnknownValueException("The answer should be an integer between 0 and " + (root.getNumberOfChildren() - 1) + "!");
			else
				childTree[risp].predictClass(objectOutputStream, objectInputStream);
		}
	}



	/**
	 * Concatena in una String tutte le informazioni di root e childTree[]
	 * correnti invocando i relativi metodi toString(). Nel caso il root
	 * corrente � di split vengono concatenate anche le informazioni dei rami.
	 */

	public String toString(){

		String tree=root.toString()+"\n";

		if( root instanceof LeafNode){

		}else { //split node
			for(int i=0;i<childTree.length;i++)
				tree +=childTree[i];
		}
		return tree;
	}


	/**
	 * Invoca il metodo toString() per la visualizzazione dell'albero di
	 * regressione.
	 */
	public void printTree(){
		System.out.println("********* TREE **********\n");
		System.out.println(toString());
		System.out.println("*************************\n");
	}	


	/**
	 * Serilizza l'albero in un file
	 * @param fileName
	 * 				Nome del File in cui salvare l'albero 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void salva (String fileName) throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (fileName));
		out.writeObject(this);
		out.writeObject(root);
		out.writeObject(childTree);
		out.close();
	}
	
	
	/**
	 * Carica un albero di regressione salvto in un file
	 * @param fileName
	 * 				Nome del file in cui � salvato l'albero
	 * @return
	 * 				L'albero contenuto nel file
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static RegressionTree carica (String fileName) throws FileNotFoundException, ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream (new FileInputStream (fileName));
		RegressionTree r = (RegressionTree) in.readObject();
		r.root = (Node) in.readObject();
		r.childTree = (RegressionTree[]) in.readObject();
		in.close();
		return r;
	}
}

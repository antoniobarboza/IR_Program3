package lucene;

import java.util.HashMap;
import java.util.Set;

/**
 * This class is a singleton that is used to keep track of document frequency
 * @author Bobby Chisholm
 *
 */
public class DocumentFreqTracker {
	private static DocumentFreqTracker instance = null;
	//					  DocumentID	   Term     count
	private static HashMap<String, HashMap<String, Integer>> termFreq;
	
	//                    DocumentID       Term     Weight
	private static HashMap<String, HashMap<String, Float>> documentVectors;
	
	//This is what determines what weight will be calculated and stored in the documentVectors, MUST BE lnc, bnn or anc
	private static String schema;
	
	private DocumentFreqTracker() {
		termFreq = new HashMap<String, HashMap<String, Integer>>();
		documentVectors = new HashMap<String, HashMap<String, Float>>();
	}

	/**
	 * Singleton get method to create new instance or get existing
	 * @return
	 */
	public static DocumentFreqTracker getInstance() {
		if( instance == null) {
			instance = new DocumentFreqTracker();
		}
		return instance;
	}
	
	
	/**
	 * If the document does not exist in map yet it's added, if term does not exist yet in 
	 * document term hash map it's added, and the term frequency is incremented
	 * @param docID documentID doc id
	 * @param term term to be added to documents term map
	 */
	protected void addDocTerm(String docID, String term) {
		HashMap<String, Integer> docMap = termFreq.get(docID);
		if(docMap == null) {
			HashMap<String, Integer> tmp = new HashMap<String, Integer>();
			termFreq.put(docID, tmp);
			docMap = termFreq.get(docID);
		}
		
		Integer count = docMap.get(term);
		if(count == null) {
			count = 0;
		}
		docMap.put(term, count + 1);
		
	}
	
	/**
	 * Must be lnc, bnn or anc. This determines how the weight will be calculated
	 */
	public static void setDocumentWeightingSchema(String newSchema) throws IllegalArgumentException{
		newSchema = newSchema.toLowerCase();
		if( !schema.equals("lnc") && !schema.equals("bnn") && !schema.equals("anc")) {
			schema = newSchema;
		}
		else throw new IllegalArgumentException();
	}
	
	/**
	 * This method MUST be called after all of the documents have been indexed and processed.
	 * This method goes through the document frequencies and calculates their document vectors for the given schema
	 */
	private static void calculateDocumentVectors() {
		Set<String> keys = termFreq.keySet();
		for(String docID: keys) {
			HashMap<String, Integer> tmp = termFreq.get(docID);
			HashMap<String, Float> documentVec;
			switch(schema) {
			case "lnc":
				documentVec = calculateLnc(tmp);
				break;
			case "bnn":
				documentVec = calculateBnn(tmp);
				break;
				//default is anc
			default:
				documentVec = calculateAnc(tmp);
				break;
			}
			documentVectors.put(docID,  documentVec);
		}
	}
	
	/**
	 * This method calculates the lnc of a given hashmap of terms and frequencies
	 * 
	 * @param termFreqs the hashmap of terms and their frequencies of a given document
	 * @return the calculated hashmap using the lnc weighting schema
	 */
	private static HashMap<String, Float> calculateLnc(HashMap<String, Integer> termFreqs){
		return null;
	}
	
	/**
	 * This method calculates the bnn of a given hashmap of terms and frequencies
	 * 
	 * @param termFreqs the hashmap of terms and their frequencies of a given document
	 * @return the calculated hashmap using the bnn weighting schema
	 */
	private static HashMap<String, Float> calculateBnn(HashMap<String, Integer> termFreqs){
		return null;
	}
	
	/**
	 * This method calculates the anc of a given hashmap of terms and frequencies
	 * 
	 * @param termFreqs the hashmap of terms and their frequencies of a given document
	 * @return the calculated hashmap using the anc weighting schema
	 */
	private static HashMap<String, Float> calculateAnc(HashMap<String, Integer> termFreqs){
		return null;
	}
	
	
	
	
	
	/**
	 * This method is used to get the term frequencies of a given document
	 * @param docID
	 * @return
	 */
	public HashMap<String, Integer> getDoc(String docID) {
		return termFreq.get(docID);
	}
	
	/**
	 * This method is used when the indexer is run so the new set of documents can be saved here
	 */
	public void resetDocMap() {
		instance = new DocumentFreqTracker();
	}
	
	/**
	 * Returns the constructed document term frequency map
	 * @return termFreq
	 */
	protected HashMap<String, HashMap<String, Integer>> gettermFreqs(){
		return termFreq;
	}
}

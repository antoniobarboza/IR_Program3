package lucene;

import java.util.HashMap;

/**
 * This class is a singleton that is used to keep track of document frequency
 * @author Bobby Chisholm
 *
 */
public class DocumentFreqTracker {
	private static DocumentFreqTracker instance = null;
	//					  DocumentID	   Term     count
	private static HashMap<String, HashMap<String, Integer>> docFreq;
	
	private DocumentFreqTracker() {
		docFreq = new HashMap<String, HashMap<String, Integer>>();
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
	 * @param str documentID
	 */
	protected static void addString(String docID, String term) {
		HashMap<String, Integer> docMap = docFreq.get(docID);
		if(docMap == null) {
			HashMap<String, Integer> tmp = new HashMap<String, Integer>();
			docFreq.put(docID, tmp);
			docMap = docFreq.get(docID);
		}
		
		Integer count = docMap.get(term);
		if(count == null) {
			docMap.put(term, 0);
			count = docMap.get(term);
		}
		count += 1;
		
	}
	
	/**
	 * Returns the constructed document term frequency map
	 * @return
	 */
	protected static HashMap<String, HashMap<String, Integer>> getDocFreqs(){
		return docFreq;
	}
}

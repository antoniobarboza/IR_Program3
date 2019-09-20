package lucene;

import java.util.HashMap;

/**
 * This class is a singleton that is used to keep track of document frequency
 * @author Bobby Chisholm
 *
 */
public class DocumentFreqTracker {
	private static DocumentFreqTracker instance = null;
	private static HashMap<String, Integer> docFreq;
	
	private DocumentFreqTracker() {
		docFreq = new HashMap<String, Integer>();
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
	 * If the key doesn't exist it adds a new one, if it does adds 1 to count
	 * @param str key to be checked/added
	 */
	protected static void addString(String str) {
		Integer count = docFreq.get(str);
		if(count == null) {
			docFreq.put(str,  1);
		} else count += 1;
	}
	
	protected static HashMap<String, Integer> getDocFreqs(){
		return docFreq;
	}
}

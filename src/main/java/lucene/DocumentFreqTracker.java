package lucene;

import java.util.HashMap;

/**
 * This class is a singleton that is used ti keep track of 
 * @author Bobby
 *
 */
public class DocumentFreqTracker {
	private static DocumentFreqTracker instance = null;
	private static HashMap<String, Integer> docFreq;
	
	private DocumentFreqTracker() {
		
	}

	public static DocumentFreqTracker getInstance() {
		if( instance == null) {
			instance = new DocumentFreqTracker();
			docFreq = new HashMap<String, Integer>();
		}
		return instance;
	}
	
	protected static void addString(String str) {
		
	}
}

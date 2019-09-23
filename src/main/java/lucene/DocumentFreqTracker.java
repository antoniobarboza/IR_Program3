package lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is a singleton that is used to keep track of document frequency
 * @author Bobby Chisholm
 *
 */
public class DocumentFreqTracker {
	private static String writeFilePath = "./src/main/java/lucene/FrequencyTrackerData.txt";
	private static DocumentFreqTracker instance;// = null;
	private static int maxTermFreq;
	private static String newDocString = "START_OF_NEW_DOC_DATA";
	//					  DocumentID	   Term     count
	private static HashMap<String, HashMap<String, Integer>> termFreq;
	
	//                    DocumentID       Term     Weight
	private static HashMap<String, HashMap<String, Float>> documentVectors;
	
	//This is what determines what weight will be calculated and stored in the documentVectors, MUST BE lnc, bnn or anc
	private static String schema;
	
	private DocumentFreqTracker() throws IOException {
		termFreq = new HashMap<String, HashMap<String, Integer>>();
		documentVectors = new HashMap<String, HashMap<String, Float>>();
		maxTermFreq = 0;
	}

	/**
	 * Singleton get method to create new instance or get existing
	 * @return
	 * @throws IOException 
	 */
	public static DocumentFreqTracker getInstance(){
		if( instance == null) {
			//System.out.println("New Instance created!");
			try {
				instance = new DocumentFreqTracker();
			} catch(Exception e) {
				
			}
		}
		return instance;
	}
	
	
	/**
	 * If the document does not exist in map yet it's added, if term does not exist yet in 
	 * document term hash map it's added, and the term frequency is incremented
	 * @param docID documentID doc id
	 * @param term term to be added to documents term map
	 */
	public void addDocTerm(String docID, String term) {
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
		if(count + 1 > maxTermFreq) maxTermFreq = count + 1;
		
	}
	
	/**
	 * Must be lnc, bnn or anc. This determines how the weight will be calculated
	 */
	public void setDocumentWeightingSchema(String newSchema) throws IllegalArgumentException{
		schema = newSchema.toLowerCase();
		if( schema.equals("lnc") || schema.equals("bnn") || schema.equals("anc")) {
			schema = newSchema;
			calculateDocumentVectors();
		}
		else throw new IllegalArgumentException();
	}
	
	/**
	 * This method MUST be called after all of the documents have been indexed and processed.
	 * This method goes through the document frequencies and calculates their document vectors for the given schema
	 */
	private void calculateDocumentVectors() {
		Set<String> keys = termFreq.keySet();
		//.out.println("KEY SET SIZE: " + keys.size());
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
			//System.out.println(docID + ": " + documentVec.toString());
		}
	}
	/**
	 * This returns the map of docId -> term -> TF-IDF
	 * @return
	 */
	public HashMap<String, HashMap<String, Float>> getDocumentVectors() {
		return documentVectors;
	}
	/**
	 * This method calculates the lnc of a given hashmap of terms and frequencies
	 * 
	 * @param termFreqs the hashmap of terms and their frequencies of a given document
	 * @return the calculated hashmap using the lnc weighting schema
	 */
	private HashMap<String, Float> calculateLnc(HashMap<String, Integer> termdocFreq){
		//Now we have built a hashmap termdocfreq: Term-> termfrequency
		HashMap<String, Float> simHash = new HashMap<String, Float>();
		for ( String term : termdocFreq.keySet() ) {
			float l = (float) ( 1 + Math.log(termdocFreq.get(term)));
			simHash.put(term, l);
		}
		//now I have to get the normalized vector length
		float sumOfSquares = 0; 
		for ( String term : simHash.keySet() ) {
			sumOfSquares += Math.pow( simHash.get(term), 2);
		}
		float vectorLength = (float) ( 1 / (Math.sqrt( sumOfSquares )));
		//now need to divide each value by vector length to normalize
		for ( String term : simHash.keySet() ) {
			float temp = simHash.get(term);
			temp = temp / vectorLength;
		}
		return simHash;
	}
	
	/**
	 * This method calculates the bnn of a given hashmap of terms and frequencies
	 * 
	 * @param termFreqs the hashmap of terms and their frequencies of a given document
	 * @return the calculated hashmap using the bnn weighting schema
	 */
	private HashMap<String, Float> calculateBnn(HashMap<String, Integer> termdocFreq){
		//If the term frequency is greater than 1 it's tf idf value is 1
		HashMap<String, Float> docVec = new HashMap<String, Float>();
		for ( String term : termdocFreq.keySet() ) {
			float l = 0;
			if(termdocFreq.get(term) > 0) l = 1;
			docVec.put(term, l);
		}
		return docVec;
	}
	
	/**
	 * This method calculates the anc of a given hashmap of terms and frequencies
	 * 
	 * @param termFreqs the hashmap of terms and their frequencies of a given document
	 * @return the calculated hashmap using the anc weighting schema
	 */
	private HashMap<String, Float> calculateAnc(HashMap<String, Integer> termdocFreq){
		//NEED TO CHANGE THIS TO BE MAX FREQUENCY FOR EACH DOC
		//Now we have built a hashmap termdocfreq: Term-> termfrequency
		HashMap<String, Float> simHash = new HashMap<String, Float>();
		for ( String term : termdocFreq.keySet() ) {
			float a = (float) ( 0.5 + (0.5*termdocFreq.get(term)/maxTermFreq) );
			simHash.put(term, a);
		}
		//now I have to get the normalized vector length
		float sumOfSquares = 0; 
		for ( String term : simHash.keySet() ) {
			sumOfSquares += Math.pow( simHash.get(term), 2);
		}
		float vectorLength = (float) ( 1 / (Math.sqrt( sumOfSquares )));
		//now need to divide each value by vector length to normalize
		for ( String term : simHash.keySet() ) {
			float temp = simHash.get(term);
			temp = temp / vectorLength;
		}
		return simHash;
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
	 * Returns the docID keyset so the set can be traversed
	 * @return docID keyset
	 */
	public Set<String> getDocKeySet(){
		return termFreq.keySet();
	}
	
	/**
	 * This method is used when the indexer is run so the new set of documents can be saved here
	 * @throws IOException 
	 */
	public void resetDocMap() {
		try {
			Files.deleteIfExists(Paths.get(writeFilePath));
			instance = new DocumentFreqTracker();
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Returns the constructed document term frequency map
	 * @return termFreq
	 */
	public HashMap<String, HashMap<String, Integer>> gettermFreqs(){
		return termFreq;
	}
	
	/**
	 * Must be called after indexer
	 * @throws IOException
	 */
	public void writeDataToFile() throws IOException {
		Files.deleteIfExists(Paths.get(writeFilePath));
		File lncltnRankOutputFile = new File(writeFilePath);
    	lncltnRankOutputFile.createNewFile();
       	BufferedWriter writer = new BufferedWriter(new FileWriter(writeFilePath));
       	
       	for(String docID: termFreq.keySet()) {
       		//used to determine a new Doument ID with it's data, means next line is document id
       		writer.write(newDocString + "\n");
       		writer.write(docID + "\n");
       		//following is all of the entries for this documentID
       		HashMap<String, Integer> freqs = termFreq.get(docID);
       		//Writes each term and it's count on a line seperated by a space
       		for(String term: freqs.keySet()) {
       			writer.write(term + " " + freqs.get(term) + "\n");
       		}
       	}
       	writer.close();
	}
	
	/**
	 * This method is used to parse the hashmap stored in the file written to by the method above
	 * Must be called at the beginning of searcher
	 * @throws IOException 
	 */
	public void loadDataFromFile() throws IOException {
		File file = new File(writeFilePath);
		if(!file.exists()) return;
		BufferedReader reader = new BufferedReader(new FileReader(writeFilePath));
    	String line = reader.readLine();
    	while( line != null ) {
    		if(line.equals(newDocString)) {
    			line = reader.readLine();
    			String docID = line;
    			line = reader.readLine();
    			while(line != null && line != newDocString && line.trim().split(" ").length > 1) {
    				String [] termAndFreq = line.trim().split(" ");
    				int freq = Integer.parseInt(termAndFreq[1]);;
    				for(int i = 0; i < freq; i++) {
    					addDocTerm(docID, termAndFreq[0]);
    				}
    				line = reader.readLine();
    			}
    		}
    	}
    	reader.close();
	}
}

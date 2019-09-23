package lucene.CustomSimilarities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;

import lucene.DocumentFreqTracker;

public class LncLtn extends Similarity{
	private IndexSearcher searcher;
	private Query query;
	private SimScorer scorer;
	//Constructor that takes in Query  
	//takes in Hashmap of document 
	public LncLtn () {
		
	}

	@Override
	public long computeNorm(FieldInvertState state) {
		// Normalization of query vector.
		return 0;
	}

	@Override
	public SimWeight computeWeight(float boost, CollectionStatistics collectionStats, TermStatistics... termStats) {

		// TF_IDF value for the query 
		return new DefaultSimWeight();

	}

	@Override
	public SimScorer simScorer(SimWeight weight, LeafReaderContext context) throws IOException {
		//This creates a custom scorer function that lets the index searcher use our custom scorer
		
		
		return new DefaultScorer(searcher, query);
	}
	
	public void setSearcherAndQuery(IndexSearcher indexSearcher, Query query) {
		searcher = indexSearcher;
		this.query = query;
		scorer = new DefaultScorer(searcher, query);
	}
	
	class DefaultSimWeight extends SimWeight{
		
	}
	
	class DefaultScorer extends SimScorer{
		private IndexSearcher searcher;
		private String query;
		private HashMap<String, Float> queryVec;
		private DocumentFreqTracker freqTracker = DocumentFreqTracker.getInstance();
		
		/**
		 * Uses the lnc ltn scoring to calculate the score of a given document
		 * @param searcher
		 * @param q
		 */
		public DefaultScorer(IndexSearcher searcher, Query q) {
			this.searcher = searcher;
			query = q.toString();
			queryVec = new HashMap<String, Float>();
			//get term frequency of querys
			for(String word: query.split(" ")) {
				Float count = queryVec.get(word);
				if(count == null) queryVec.put(word, (float) 0);
				else queryVec.put(word, count + 1);
			}
			
			for(String term: queryVec.keySet()) {
				float l = (float) ( 1 + Math.log(queryVec.get(term)));
				queryVec.put(term, l);
			}
			
			//get total term frequency in all docs and multiply by log(N/df)
			Set<String> docKeySet =  freqTracker.getDocKeySet();
			float bigN = docKeySet.size();
			float sumOfSquares = 0;
			for(String term: queryVec.keySet()) {
				float docFreq = 0;
				for(String docID: docKeySet) {
					if( freqTracker.getDoc(docID).containsKey(term)) docFreq += 1;
				}
				Float ltn = queryVec.get(term);
				ltn = (float) (ltn * Math.log(bigN / docFreq));
				sumOfSquares += Math.pow(ltn,  2);
			}
			//Get l2 norm of query vector
			for(String term: queryVec.keySet()) {
				float tmp = queryVec.get(term);
				tmp = tmp / sumOfSquares;
			}
		}
		
		/**
		 * Computes the score of the given doc with the given query
		 */
		@Override
		public float score(int doc, float freq) throws IOException {
			String docID = searcher.doc(doc).get("id");
			HashMap<String, Float> docVector = freqTracker.getDocumentVectors().get(docID);
	
			if(docVector == null) {
				//System.out.println("NO DOCUMENTS RETURNED FOR: " + docID);
				return 0;
			}
			
			float score = 0;
			for(String term: queryVec.keySet()) {
				//score for term in document term vector
				float docScore = 0;
				if(docVector.containsKey(term)) docScore = docVector.get(term);
				score = score + ( docScore * queryVec.get(term));
			}
			
			return score;
		}

		@Override
		public float computeSlopFactor(int distance) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}

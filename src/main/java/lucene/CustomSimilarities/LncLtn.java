package lucene.CustomSimilarities;

import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;

public class LncLtn extends Similarity{
	
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
		// To my understanding this is what takes the dot product between the Document vector and the query vector 
		
		return null;
	}
	
	class DefaultSimWeight extends SimWeight{
		
	}
	
	class DefaultScorer extends SimScorer{
		
		@Override
		public float score(int doc, float freq) throws IOException {
			// TODO Auto-generated method stub
			return 0;
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

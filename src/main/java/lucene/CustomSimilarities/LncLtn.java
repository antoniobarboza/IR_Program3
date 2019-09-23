package lucene.CustomSimilarities;

import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;

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
		// TF_IDF value for each query 
		return null;
	}

	@Override
	public SimScorer simScorer(SimWeight weight, LeafReaderContext context) throws IOException {
		// To my understanding this is what takes the dot product between the Document vector and the query vector 
		return null;
	}

}

package lucene.CustomSimilarities;

import java.io.IOException;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;

public class BnnBnn extends Similarity{

	@Override
	public long computeNorm(FieldInvertState state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SimWeight computeWeight(float boost, CollectionStatistics collectionStats, TermStatistics... termStats) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimScorer simScorer(SimWeight weight, LeafReaderContext context) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}

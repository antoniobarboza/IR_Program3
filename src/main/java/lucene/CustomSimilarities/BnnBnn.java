package lucene.CustomSimilarities;

import java.io.IOException;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.SmallFloat;

public class BnnBnn extends Similarity{

	@Override
	public long computeNorm(FieldInvertState state) {
		// No normalization applied.
		int numTerms = state.getLength();
		return SmallFloat.intToByte4(numTerms);
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

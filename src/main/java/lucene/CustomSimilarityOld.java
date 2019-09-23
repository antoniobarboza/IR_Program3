package lucene;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.SimilarityBase;

/**
 *
 * Creates a custom similarity/scoring function and returns it
 */
public class CustomSimilarityOld {
	/**
	 * Returns a custom scoring function
	 * @return a custom similarity
	 */
	public static Similarity getSimilarity() {
        SimilarityBase mySimilarity = new SimilarityBase() {
        	@Override
            protected float score(BasicStats basicStats, float v, float v1) {
                return v;
            }

            @Override
            public String toString() {
                return "CustomSimilarity";
            }
        };

        return mySimilarity;
    }
	
	/**
	 * Returns the name of the ranking function
	 */
	public static String getSimilarityName() {
		return "CustomSimilarity";
		
	}
}

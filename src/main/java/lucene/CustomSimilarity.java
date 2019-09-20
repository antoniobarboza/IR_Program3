package lucene;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.SimilarityBase;

/**
 *
 * Creates a custom similarity/scoring function and returns it
 */
public class CustomSimilarity {
	private static String tfidfModel = null;
	/**
	 * Returns a a TF-IDF lnc.ltn ranking model to be used 
	 * as a custom similarity when ranking documents
	 * @return an lnc.ltn similarity
	 */
	public static Similarity lncltn() {
		tfidfModel = "lnc.ltn";
        SimilarityBase mySimilarity = new SimilarityBase() {
        	@Override
            protected float score(BasicStats basicStats, float v, float v1) {
                return v;
            }

            @Override
            public String toString() {
                return "lnc.ltn";
            }
        };

        return mySimilarity;
    }
	
	/**
	 * Returns a a TF-IDF bnn.bnn ranking model to be used 
	 * as a custom similarity when ranking documents
	 * @return an bnn.bnn similarity
	 */
	public static Similarity bnnbnn() {
		tfidfModel = "bnn.bnn";
        SimilarityBase mySimilarity = new SimilarityBase() {
        	@Override
            protected float score(BasicStats basicStats, float v, float v1) {
                return v;
            }

            @Override
            public String toString() {
                return "bnn.bnn";
            }
        };

        return mySimilarity;
    }
	
	/**
	 * Returns a a TF-IDF anc.apc ranking model to be used 
	 * as a custom similarity when ranking documents
	 * @return an anc.apc similarity
	 */
	public static Similarity ancapc() {
		tfidfModel = "anc.apc";
        SimilarityBase mySimilarity = new SimilarityBase() {
        	@Override
            protected float score(BasicStats basicStats, float v, float v1) {
                return v;
            }

            @Override
            public String toString() {
                return "anc.apc";
            }
        };

        return mySimilarity;
    }
	
	/**
	 * Returns the name of the ranking function
	 * null if a similarit has not been selected yet
	 */
	public static String getSimilarityName() {
		return tfidfModel;
		
	}
}

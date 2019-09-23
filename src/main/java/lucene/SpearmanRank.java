package lucene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SpearmanRank {

	public static void main(String[] args) {
		// This is going to be the driver function 
		//this is hardcoded for 100 pairs.
		HashMap<String, Double> queryMap = new HashMap<>();
		
		int numPairs = 100; 
		//Step one is to sort both rankings by doc Ids
	    String defaultRankOutputPath = "./src/main/java/output/DefaultRankingOutput.txt";
	    
	    //Java take in the path as an arg
	    if ( args.length < 1 ) {
	    	System.out.println( "Give the path to the other Ranking file. That is being compared the BMS25Simularity ");
	    	throw new IllegalArgumentException("Quiting...");
	    }
	    String compareRankOutputPath = args[0];
	    try {
	    	BufferedReader reader = new BufferedReader(new FileReader(defaultRankOutputPath));
	    	BufferedReader reader1 = new BufferedReader(new FileReader(compareRankOutputPath));
	    	String line = reader.readLine();
	    	while ( line != null ) {
	    		calc(queryMap, defaultRankOutputPath, compareRankOutputPath, numPairs, reader, line, reader1);
	    		line = reader.readLine();
	    	}
	    	System.out.println(queryMap);
	    	//Print out the average for each query 
	    	double sum = 0;
	    	int numqueries = 0;
	    	for ( String key: queryMap.keySet() ) {
	    		sum += queryMap.get(key);
	    		numqueries++;
	    	}
	    	double average = sum/numqueries;
	    	System.out.println( "Spearman value: " + average );
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	private static void calc(HashMap<String, Double> queryMap,String defaultRankOutputPath, String compareRankOutputPath, int numPairs, BufferedReader reader, String line1, BufferedReader reader1) {
		int counter = 1;
	    String query = "";
	    TreeMap<String, ArrayList<Integer>> dMap = new TreeMap<>();
	    //loop through the defaultRank file and for every 100 lines store the documentID in a map 
	    try {
	    	String line = line1;

	    	while(line !=null ) {
	    		String[] arrayLine = line.split(" ");
		    	if ( query.contentEquals("") ) {
		    		query = arrayLine[0];
		    	}
	    		if ( !arrayLine[0].equals( query ) && !query.equals("") ) {
	    			//counter++; 
	    			//line = reader.readLine();
	    			break;
	    		}
	    		//query = arrayLine[0];
	    		ArrayList<Integer> ar = new ArrayList<>();
	    		ar.add(Integer.parseInt(arrayLine[3]));
	    		ar.add(0);
	    		dMap.put(arrayLine[2], ar);
	    		
	    		counter++;
	    		line = reader.readLine();
	    	}
	    	if ( line == null ) {
	    		return;
	    	}
	    	
	    	//System.out.println("Starting check");
	    	//now I need to check doc Id in other ranking and update the value with a d value 
	    	TreeMap<String, ArrayList<Integer>> out = checkCustom (compareRankOutputPath,numPairs,dMap, reader1);
	    	//System.out.println(out);
	    	//Loop through each d value and store 
	    	int dSquareSum = 0;
	    	int nonPairs = 0;
	    	for ( String value: out.keySet() ) {
	    		if( out.get(value).get(1) == 1 ) {
	    			dSquareSum += Math.pow( out.get(value).get(0), 2 );
	    		}
	    		else {
	    			nonPairs++; 
	    		}
	    	}
	    	
	    	int tempN = numPairs - nonPairs;
	    	if ( tempN != 0 ) {
	    		queryMap.put(query, 1 - ((6*dSquareSum)/(Math.pow(tempN, 3)-tempN)));
	    	}
	    	
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	private static TreeMap<String, ArrayList<Integer>> checkCustom( String path, int numPairs, TreeMap<String, ArrayList<Integer>> dMapc, BufferedReader reader1 ) {
		try {
	    	String line = reader1.readLine();
	    	int counter = 1;
	    	String query = "";
	    	while(line !=null ) {
	    		String[] arrayLine = line.split(" ");
	    		if ( query.contentEquals("")) {
	    			query = arrayLine[0];
	    		}
	    		if ( !arrayLine[0].equals( query ) && !query.equals("") ) {
	    			//counter++; 
	    			//line = reader.readLine();
	    			break;
	    		}
	    		if ( dMapc.get(arrayLine[2]) != null ) {
	    			//the Doc Id in here 
	    			ArrayList<Integer> temp = dMapc.get(arrayLine[2]); 
	    			int curVal = temp.get(0);
	    			int dif = curVal - Integer.parseInt(arrayLine[3]);
	    			ArrayList<Integer> ar = new ArrayList<>();
	    			ar.add(dif);
	    			ar.add(1); //indicates d value calculate
	    			dMapc.replace(arrayLine[2], ar); 
	    		}	    		
	    		counter++;
	    		line = reader1.readLine();
	    	}
	    	//return dMapc;
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		return dMapc;
	}
		
}

1. Git clone repository 
	git clone https://github.com/antoniobarboza/IR_Program3.git
2. cd IR_Program3
3. mvn clean install 
4. mvn clean compile assembly:single

*copy the test200 data into src/main/java/data

*How to run the indexer:
java -Xmx50g -cp target/IR_Program3-0.1-jar-with-dependencies.jar lucene.Indexer

*Search files: 
java -Xmx50g -cp target/IR_Program3-0.1-jar-with-dependencies.jar lucene.SearchFiles
-Args 0 - path to index 
-Args 1 - path to input file 
-IF no args are passed they both set to default 

*Add note of the status of search files - Stuck on similarities 

Part 2: 
*Even though we were unsuccessful in creating a working solution for part 1. We implemented part 2. 

java -Xmx50g -cp target/IR_Program3-0.1-jar-with-dependencies.jar lucene.SpearmanRank 
-Arg 0 - path to the comparison ranking. 

*This was tested with the comparison between lucene default and the custom similarity used last week! 
TO TEST: 

java -Xmx50g -cp target/IR_Program3-0.1-jar-with-dependencies.jar lucene.SearchFilesOld
*This produces the old CustomRanking from last week 

*This compars the default with the Custom Ranking produced above. 
java -Xmx50g -cp target/IR_Program3-0.1-jar-with-dependencies.jar lucene.SpearmanRank src/main/java/output/CustomRankingOutput.txt


package lucene;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.unh.cs.treccar_v2.Data.Paragraph;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

/** Index all text files under a directory.
 * It is currently hard-coded and does not take any input
 * 
 * @author Bobby Chisholm
 * 
 */
public class Indexer {
	
  private Indexer() {}
  //Used to add all documents and all terms in the document to a map that is used to track each terms frequency in each document
  private static DocumentFreqTracker docFreq = DocumentFreqTracker.getInstance();

  /** Index all text files under a directory.
   * 
   * @param args args[0] will be the path to the index directory, default value of: ./src/main/java/index
   */
  public static void main(String[] args) {
	  
	  //check if indexPath passed in, if not set to default value
    String indexPath;
    if(args.length != 0) indexPath = args[0];
    else indexPath = "./src/main/java/index";
    
    String docsPath = "./src/main/java/data/test200/test200-train/train.pages.cbor-paragraphs.cbor";
    
    File input = new File(docsPath);
    
    try {
        //try to open the index to be written to
    	Directory dir = FSDirectory.open(Paths.get(indexPath));
    	Analyzer analyzer = new StandardAnalyzer();
    	IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

    	//Creates a new index folder, deletes any old data
    	indexWriterConfig.setOpenMode(OpenMode.CREATE);

    	IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);
    	//clears index to avoid errors
    	indexWriter.deleteAll();
    	indexDoc(indexWriter, input, analyzer);
    	indexWriter.close();
    } catch(Exception e) {
    	e.printStackTrace();
    }

  }

  /**
   * Creates Lucene Documents from an input file with paragraphs, then indexes them
   * Adds all documents and terms to a document frequency tracker
   * 
   * @param writer Writes to the given index
   * @param file file to be parsed into Lucene Documents
   * @param analyzer to b e used to tokenize strings to add to document frequency map
   * 
   * @throws IOException If there is a low-level I/O error
   */
  static void indexDoc(final IndexWriter writer, File file, Analyzer analyzer) throws Exception {
	  docFreq.resetDocMap();
	  //System.out.println("PATH: " + file.getAbsolutePath());
	  FileInputStream fileStream = new FileInputStream(file);
	  
	  //convert all data into paragraphs
	  Iterable<Paragraph> paragraphs = null;
	  try {
	  paragraphs = DeserializeData.iterableParagraphs(fileStream);
	  } catch(Exception e) {
		  //conversion failed
		  throw e;
	  }
	  int commit = 0;
	  System.out.println("Indexing documents...");
	  for(Paragraph paragraph : paragraphs) {
          if (commit == 10000) {
              writer.commit();
              commit = 0;
          }
		  //Tokenize paragraphs text and add it to the document frequency tracker
          //*This was the most easily readable way to process a lucen token stream
          TokenStream tokens = analyzer.tokenStream("terms", paragraph.getTextOnly());
          CharTermAttribute term = tokens.addAttribute(CharTermAttribute.class);
          //Need to reset because it doesn't work if you don't
          tokens.reset();
          String docID = paragraph.getParaId();
          while (tokens.incrementToken()) {
            docFreq.addDocTerm(docID, term.toString());
          }
          //Debugging prints
          //HashMap<String, Integer> tmp = docFreq.getDoc(docID);
          //if(tmp != null) System.out.println(docID + ": " + tmp.toString());
          //end debugging prints
          tokens.end();
          tokens.close();
          
		  Document doc = new Document();
		  doc.add(new StringField("id", paragraph.getParaId(), Field.Store.YES));   //Correct this needs to be a stringfield
		  doc.add(new TextField("text", paragraph.getTextOnly(), Field.Store.YES)); //Correct this needs to be Textfield
		  writer.addDocument(doc);
		  commit++;
	  }
	  //System.out.println(docFreq.getDocFreqs().toString());
	  writer.commit();
	  docFreq.writeDataToFile();
	  //System.out.println(docFreq.getDocKeySet().toString());
	  System.out.println("All documents indexed!");
  }

  
}

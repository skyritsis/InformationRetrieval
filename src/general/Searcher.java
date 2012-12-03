package general;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
//import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class Searcher {
	
	private StandardAnalyzer analyzer;
	private int hitsPerPage;
	private IndexReader reader;
	private IndexSearcher searcher;
	private TopScoreDocCollector collector;
	
	public Searcher(StandardAnalyzer analyzer,Directory index) throws IOException
	{
		this.analyzer = analyzer;
		hitsPerPage = 20;
	    reader = DirectoryReader.open(index);
	}
	
	public void searchQueryVector(String querystr,int id) throws ParseException, IOException
	{
		searcher = new IndexSearcher(reader);
	    collector = TopScoreDocCollector.create(hitsPerPage, true);
//	    String[] fields = {"text"};
	    Query q = new QueryParser(Version.LUCENE_40, "text" , analyzer).parse(querystr);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    System.out.println("\nVector Space Results");
	    printResults(hits,querystr,id);
	}
	
	public void searchQueryBM25(String querystr,int id) throws ParseException, IOException
	{
		searcher = new IndexSearcher(reader);
	    collector = TopScoreDocCollector.create(hitsPerPage, true);
//	    String[] fields = {"text","title","author"};
	    searcher.setSimilarity(new BM25Similarity());
	    Query q = new QueryParser(Version.LUCENE_40, "text" , analyzer).parse(querystr);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    System.out.println("\nOKAPI BM25 Results");
	    printResults(hits,querystr,id);
	}
	
	public void searchQueryBoolean(String querystr,int id) throws ParseException, IOException
	{	
		searcher = new IndexSearcher(reader);
	    TokenStream stream = analyzer.tokenStream("text",new StringReader(querystr));
	    
	    
	    stream.reset();
	    int hitsPP = tokenCount(stream);
	    stream.end();
	    stream.close();
	    stream = analyzer.tokenStream("text",new StringReader(querystr));
		stream.reset();
//		System.out.println("Tokens: " + hitsPP);
		ScoreDoc[][] hits=new ScoreDoc[hitsPP][];
		int i=0;
		while (stream.incrementToken())
		{
			collector = TopScoreDocCollector.create((hitsPP>10 ? 2 : 3 ), true);
			Query q = new TermQuery(new Term("text", stream.getAttribute(CharTermAttribute.class).toString()));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(q, BooleanClause.Occur.SHOULD);
//			System.out.println("Query: " + booleanQuery.toString());
		    searcher.search(booleanQuery, collector);
		    hits[i] = collector.topDocs().scoreDocs;
		    i++;
		}
		stream.end();
		stream.close();
		 System.out.println("\nBoolean Results");
	    printResultsB(hits,querystr,id);
	}
	
	public int tokenCount(TokenStream s) throws IOException
	{
		int tokens=0;
		while (s.incrementToken())
		{
			tokens++;
		}
		return tokens;
	}
	
	public void printResultsB(ScoreDoc[][] hits,String q,int id) throws IOException
	{
		System.out.println("Found " + ((hits.length*hits[0].length > 20) ? 20 : hits.length) + " hits");
		int k=0;
	    for(int i=0;i<hits.length;++i) {
	    	for(int j=0;j<hits[i].length;j++){
	    		int docId = hits[i][j].doc;
	    		Document d = searcher.doc(docId);
	    		System.out.println(id + " " + d.get("id"));
	    		k++;
	    		if(k==20)
	    			return;
	    	}
	    }
	}

	public void printResults(ScoreDoc[] hits,String q,int id) throws IOException
	{
		System.out.println("Found " + hits.length + " hits");
	    for(int i=0;i<hits.length;++i) {
	    	int docId = hits[i].doc;
	    	Document d = searcher.doc(docId);
	    	System.out.println(id + " " + d.get("id"));
	    }
	}
}

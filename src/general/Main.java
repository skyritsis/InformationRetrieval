package general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.lucene.queryparser.classic.ParseException;




public class Main {

	/**
	 * @author skyritsis
	 */
	
	public static void main(String[] args) {
		try {
			Documenizer dmz = new Documenizer("cran/cran.all.1400");
			Querizer qrz = new Querizer("cran/cran.qry");
			Indexer idxer = new Indexer();
			
			dmz.createDocumentVector();
			for(int i=0;i<dmz.docs.size();i++)
				idxer.addDoc_to_Index(dmz.docs.elementAt(i));
			idxer.closeIndexWriter();
			//Searcher srch = new Searcher(idxer.analyzer,idxer.index);
			Searcher srch = new Searcher(idxer.analyzer,idxer.index);
			qrz.createQueryVector();
			srch.initializeWriters();
			for(int i=0;i<qrz.queries.size();i++)
			{
				String q = qrz.queries.elementAt(i).text;
				int id = qrz.queries.elementAt(i).id;
				System.out.println("\nQUERY ID : "+ id);
				srch.searchQueryVector(q,i+1);
				srch.searchQueryBM25(q,i+1);
				srch.searchQueryBoolean(q,i+1);
			}
			srch.closeWriters();
			//System.out.println(dmz.docs.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

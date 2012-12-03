package general;

import java.io.FileNotFoundException;
import java.io.IOException;

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
			for(int i=0;i<20;i++)
			{
				String q = qrz.queries.elementAt(i).text;
				int id = qrz.queries.elementAt(i).id;
				System.out.println("\nQUERY ID : "+ id);
				srch.searchQueryVector(q,id);
				srch.searchQueryBM25(q,id);
				srch.searchQueryBoolean(q,id);
			}
			//System.out.println(dmz.docs.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

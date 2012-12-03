package general;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	
	public Directory index;
	public IndexWriterConfig config;
	public StandardAnalyzer analyzer;
	public IndexWriter w;
	
	public Indexer() throws IOException{
		analyzer = new StandardAnalyzer(Version.LUCENE_40);

		index = new RAMDirectory();
		config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		w = new IndexWriter(index, config);
	}
	
	public void addDoc_to_Index(Mydoc cur_doc) throws IOException
	{
		Document doc = new Document();
		doc.add(new TextField("title", cur_doc.title, Field.Store.YES));
		doc.add(new TextField("text", cur_doc.text, Field.Store.YES));
		doc.add(new TextField("author", cur_doc.author, Field.Store.YES));
		doc.add(new StringField("id", cur_doc.id, Field.Store.YES));
		doc.add(new StringField("b", cur_doc.b, Field.Store.YES));
		w.addDocument(doc);
	}
	
	public void closeIndexWriter() throws IOException
	{
		w.close();
	}

}

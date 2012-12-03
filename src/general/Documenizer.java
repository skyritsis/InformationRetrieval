package general;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Documenizer {
	
	private BufferedReader br;
	public Vector<Mydoc> docs;
	
	public Documenizer(String file) throws FileNotFoundException
	{
		br = new BufferedReader(new FileReader(file));
		docs = new Vector<Mydoc>();
	}
	
	public void createDocumentVector() throws IOException
	{
		StringBuilder strb = new StringBuilder();
		String line = br.readLine();
		Mydoc temp=null;
//		String previous = "1";
		
		while(line!=null)
		{
			if(line.startsWith(".I"))
			{
				temp = new Mydoc();
				strb.setLength(0);
				temp.id = line.substring(3);
				line = br.readLine();
//				if(!temp.id.equalsIgnoreCase("1") && Integer.parseInt(previous)!=Integer.parseInt(temp.id)-1)
//					System.out.println("Wrong Parse at "+temp.id);
//				previous = temp.id;
			}
			if(line.startsWith(".T"))
			{
				line = br.readLine();
				while(!line.startsWith(".A")){
					strb.append(line);
					line = br.readLine();
				}
				temp.title = strb.toString();
				//System.out.println("Text Title :"+temp.title);
				strb.setLength(0);
			}
			if(line.startsWith(".A"))
			{
				line = br.readLine();
				while(!line.startsWith(".B")){
					strb.append(line);
					line = br.readLine();
				}
				temp.author = strb.toString();
				strb.setLength(0);
			}
			if(line.startsWith(".B"))
			{
				line = br.readLine();
				while(!line.startsWith(".W")){
					strb.append(line);
					line = br.readLine();
				}
				temp.b = strb.toString();
				strb.setLength(0);
			}
			if(line.startsWith(".W"))
			{
				line = br.readLine();
				while(!line.startsWith(".I")){
					strb.append(line);
					line = br.readLine();
					if(line==null)
					{
						temp.text = strb.toString();
						break;
					}
				}
				temp.text = strb.toString();
				strb.setLength(0);
			}
			docs.addElement(temp);
		}
	}

}

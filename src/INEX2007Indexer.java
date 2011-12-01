

import javax.xml.parsers.*; // JAXP
import org.xml.sax.*;
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;

public class INEX2007Indexer {
	
   public static StreamSource xsltFile;
   public static StreamSource xsltBodyOnlyFile;
   public static IndexWriter indexWriter;
   public static DocumentBuilderFactory factory;
   public static DocumentBuilder parser;
   public static TransformerFactory xsltFactory;

   
   public static void main (String args[]) {
	   try {
		   File file = new File(args[0]);
		   if (!file.exists() || !file.isDirectory()){
			   throw new Exception("Directory does not exist: " + args[0]);
		   }
		   
		   //Source XSLT Stylesheets
           xsltFile = new StreamSource(new File(args[1]));
           xsltBodyOnlyFile = new StreamSource(new File(args[2]));
           
		   indexWriter = new IndexWriter(args[3], new StandardAnalyzer(), true);
		   
		   factory = DocumentBuilderFactory.newInstance();
	       parser = factory.newDocumentBuilder();
	       xsltFactory = TransformerFactory.newInstance();
	       
	       recursiveIndexer(file);
	       
	       //TODO: do we need this?
	       //indexWriter.optimize();
	       
	       indexWriter.close();
	   }
	   catch (Exception e) {
	         System.err.println( "Error in initializing: " + e.getMessage() );
	   }
   }
   
   public static void recursiveIndexer (File file) {
      try {
    	  if (file.isDirectory()){
    		  for (File childFile : file.listFiles()){
    			  recursiveIndexer(childFile);
    		  }
    		  
    	  }
    	  //is a file
    	  else if (file.isFile() && file.getAbsolutePath().endsWith(".xml")){       	    
                //Source XML File
                StreamSource xmlFile = new StreamSource(file);
                Transformer transformer = xsltFactory.newTransformer(xsltFile);
                Transformer transformerBody = xsltFactory.newTransformer(xsltBodyOnlyFile);

                //Apply the transformation
                StringWriter sw = new StringWriter();
                StreamResult resultStream = new StreamResult( sw );
                transformer.transform(xmlFile, resultStream);
                
                StringWriter swBody = new StringWriter();
                StreamResult resultStreamBody = new StreamResult( swBody );
                transformerBody.transform(xmlFile, resultStreamBody);
                
                //get the article
                org.w3c.dom.Document document = parser.parse(new InputSource(new StringReader(sw.toString())));
                String gist  = parser.parse(new InputSource(new StringReader(swBody.toString()))).getElementsByTagName("body").item(0).getTextContent();
                if (gist.length() > 500)
                	gist = gist.substring(0, 500);
                System.out.print(file.getPath() + "\t");
                System.out.println( document.getElementsByTagName("title").item(0).getTextContent() );

                //create Lucene document and add it
                org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                doc.add(new Field("contents", new StringReader(document.getElementsByTagName("body").item(0).getTextContent())));
                doc.add(new Field("title", document.getElementsByTagName("title").item(0).getTextContent(), Field.Store.YES, Field.Index.TOKENIZED));
                doc.add(new Field("fileId", file.getPath(), Field.Store.YES, Field.Index.NO));
                doc.add(new Field("gist", gist, Field.Store.YES, Field.Index.NO));
                indexWriter.addDocument(doc);
         }
         
      }
      catch (IOException io) {
         System.err.println ( "Error while reading data files: " + io.getMessage() );
      }
      catch (Exception e) {
         System.err.println( "Error in parsing: " + e.getMessage() );
      }
   }
 }

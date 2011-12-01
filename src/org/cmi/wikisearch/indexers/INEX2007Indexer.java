package org.cmi.wikisearch.indexers;
/**
 * CleanIndexer.java
 *
 * This script runs the Wikipedia articles through an xsl file before inserting them
 * into a Lucene index.
 * 
 * Execution: 
 * java CleanIndexer <file containing list of article filenames> 
 * 					 <path to corpus> 
 * 					 <xsl file> 
 * 					 <lucene index>
 *
 * Sept 08 07 Updated to use Lucene instead of MySQL - Chris Jordan
 * July 17 07 Initial Created - Chris Jordan
 */

import javax.xml.parsers.*; // JAXP
import org.xml.sax.*;
import java.io.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;

public class INEX2007Indexer {
   public static void main (String args[]) {
	   
      try {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder parser = factory.newDocumentBuilder();
          
          //get list of article filenames 
          BufferedReader br = new BufferedReader(new FileReader( args[0]  ) );
          List<String> testFiles = new ArrayList<String>();
          String line;
          while ( (line = br.readLine()) != null ) {
                  testFiles.add( line.trim() );
          }

          //insert every document in Wikipedia into a Lucene index
          //after it has been filtered by a XSL file
          IndexWriter indexWriter = new IndexWriter(args[4], new StandardAnalyzer(), true);
          for ( int i = 0; i < testFiles.size(); i ++ ) {
        	    
                //Source XML File
                StreamSource xmlFile = new StreamSource(new File(args[1]+testFiles.get(i)));
                //Source XSLT Stylesheets
                StreamSource xsltFile = new StreamSource(new File(args[2]));
                StreamSource xsltBodyOnlyFile = new StreamSource(new File(args[3]));
                
                TransformerFactory xsltFactory = TransformerFactory.newInstance();
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
                System.out.print(testFiles.get(i) + "\t");
                System.out.println( document.getElementsByTagName("title").item(0).getTextContent() );

                //create Lucene document and add it
                org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                doc.add(new Field("contents", new StringReader(document.getElementsByTagName("body").item(0).getTextContent())));
                doc.add(new Field("title", document.getElementsByTagName("title").item(0).getTextContent(), Field.Store.YES, Field.Index.TOKENIZED));
                doc.add(new Field("fileId", testFiles.get(i), Field.Store.YES, Field.Index.NO));
                doc.add(new Field("gist", gist, Field.Store.YES, Field.Index.NO));
                indexWriter.addDocument(doc);
         }
         indexWriter.close();
         
      }
      catch (IOException io) {
         System.err.println ( "Error while reading data files: " + io.getMessage() );
      }
      catch (Exception e) {
         System.err.println( "Error in parsing: " + e.getMessage() );
      }
   }
 }

package org.cmi.wikisearch.services;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DocumentServiceXSLImpl implements DocumentService {
	private String corpusPath;
	private String xslPath;

	protected final Log logger = LogFactory.getLog(getClass());
	
	public String documentString (String id){
		try {
			//Source XML File
            StreamSource xmlFile = new StreamSource(new File(this.getCorpusPath()+File.separator+id));
            //Source XSLT Stylesheet
            StreamSource xsltFile = new StreamSource(new File(this.getXslPath()));

            TransformerFactory xsltFactory = TransformerFactory.newInstance();
            Transformer transformer = xsltFactory.newTransformer(xsltFile);

            //Apply the transformation
            StringWriter sw = new StringWriter();
            StreamResult resultStream = new StreamResult( sw );
            transformer.transform(xmlFile, resultStream);
            
			return sw.toString();
		}
		catch (Exception e) {
			logger.error("Error in parsing", e);
			throw new RuntimeException("Error in parsing", e);
	    }
	}

	public String getCorpusPath() {
		return corpusPath;
	}

	public void setCorpusPath(String corpusPath) {
		this.corpusPath = corpusPath;
	}

	public String getXslPath() {
		return xslPath;
	}

	public void setXslPath(String xslPath) {
		this.xslPath = xslPath;
	}
	
}

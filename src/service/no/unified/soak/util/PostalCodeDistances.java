package no.unified.soak.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

public class PostalCodeDistances {
    protected final static Log log = LogFactory.getLog("PostalCodeDistances");

	public static void loadKmlFileIfNecessary(String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			File file = new File(filename);
			if (!file.canRead() && !file.isFile()) {
				log.error("File "+file.getAbsolutePath()+" ("+file.getCanonicalPath()+") does not exist or is not readable with current security settings.");
				return;
			}
			db.parse(file);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

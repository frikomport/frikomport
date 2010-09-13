package no.unified.soak.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.unified.soak.model.PostalCodeCoordinate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

public class PostalCodeDistances {
    protected final static Log log = LogFactory.getLog("PostalCodeDistances");

	public static List<PostalCodeCoordinate> loadKmlFileIfNecessary(String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			File file = new File(filename);
			if (!file.canRead() && !file.isFile()) {
				log.error("File "+file.getAbsolutePath()+" ("+file.getCanonicalPath()+") does not exist or is not readable with current security settings.");
				return null;
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
		return null;
	}

	public static List<PostalCodeCoordinate> loadKmlFileIfNecessary_EmulatedTest(String filename) {
		List list = new ArrayList<PostalCodeCoordinate>();
		list.add(new PostalCodeCoordinate("1501", 10.666900, 59.437650));
		list.add(new PostalCodeCoordinate("0187", 10.7637606398,59.9144471445));
		list.add(new PostalCodeCoordinate("0581", 10.8165703454,59.9274831406));
		list.add(new PostalCodeCoordinate("0853", 10.7451984259,59.9426039666));
		list.add(new PostalCodeCoordinate("0854", 10.7382430161,59.9458229218));
		list.add(new PostalCodeCoordinate("3104", 10.4195367782,59.2697473505));
		list.add(new PostalCodeCoordinate("5147", 5.28072422154,60.3510818902));
		list.add(new PostalCodeCoordinate("5610", 6.20648923884,60.3898497536));
		list.add(new PostalCodeCoordinate("7457", 10.460804,63.416732));
		list.add(new PostalCodeCoordinate("9013", 18.901958,69.639866));
		list.add(new PostalCodeCoordinate("9173", 11.9286,78.9245));
		list.add(new PostalCodeCoordinate("9501", 23.234048,69.960702));
		list.add(new PostalCodeCoordinate("9950", 31.1047486293,70.3732314647));
		return list;
	}
	
}

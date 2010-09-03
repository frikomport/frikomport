package no.unified.soak.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class PostalCodesSuperduperLoader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PostalCodesSuperduperLoader().loadAndWriteJavaCode();
	}
	
	
	private void loadAndWriteJavaCode(){
		try {
			InputStream is = new FileInputStream("C:\\FriKomPort_SVV\\postnummer-koordinater\\postnummer.kml");
			if (is != null) {
				StringBuilder sb = new StringBuilder();
				String line;

				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

					System.out.println("public static List<PostalCodeCoordinate> loadPostalCodes() {");
					System.out.println("    List list = new ArrayList<PostalCodeCoordinate>();");
					
					int pos = 0;
					int start = 0;
					int end = 0;
					while ((line = reader.readLine()) != null) {
						// find next postalCode
						String postalCode = null;
						if((pos = line.indexOf("<Placemark><name>")) != -1){
							start = pos + "<Placemark><name>".length();
							end = start + 4;
							postalCode = line.substring(start, end);
						}

						// find next X,Y 
						String xy = null;
						String x = null;
						String y = null;
						if((pos = line.indexOf("<Point><coordinates>")) != -1){
							start = pos + "<Point><coordinates>".length();
							end = line.indexOf("</coordinates>");
							xy = line.substring(start, end);
							String[] xyValues = StringUtils.split(xy, ',');
							x = xyValues[0];
							y = xyValues[1];
						}
						
						if(postalCode != null && postalCode.length() == 4 && x != null && y != null){
							System.out.println("    list.add(new PostalCodeCoordinate(\"" + postalCode + "\"," + x + ", " + y + "));");
						}
					}
					System.out.println("   return list;\n}");
				} finally {
					is.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}

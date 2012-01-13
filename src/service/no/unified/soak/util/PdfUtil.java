/**
 * Util for 
 * @author sa
 */
package no.unified.soak.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

public class PdfUtil {
     
	public static final Log log = LogFactory.getLog(PdfUtil.class);

	public static final int A3 = 3;
	public static final int A4 = 4;
	public static final int A5 = 5;

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;

	public static final int PORTRAIT = 1;
	public static final int LANDSCAPE = 2;

	Document doc = null;
	OutputStream out = null;
	Hashtable<Integer, PdfUtilTable> tables = new Hashtable<Integer, PdfUtilTable>();
	int tableIdentifier = 0;
	
	
	/**
	 * Instance for creation of pdf-document 
	 * @param filename 
	 * @param pageformat A3, A4, A5
	 * @param mode PORTRAIT, LANDSCAPE
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	public PdfUtil(String filename, int format, int mode, String title, String author) throws DocumentException, FileNotFoundException {
		out = new FileOutputStream(filename);
		openDocument(out, format, mode, title, author);
	} 

	/**
	 * Instance for creation of pdf-document 
	 * @param out 
	 * @param pageformat A3, A4, A5
	 * @param mode PORTRAIT, LANDSCAPE
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	public PdfUtil(OutputStream out, int format, int mode, String title, String author) throws DocumentException {
		openDocument(out, format, mode, title, author);
	} 

	private void openDocument(OutputStream out, int format, int mode, String title, String author) throws DocumentException {
		switch(format) {
		case 3:
			if(mode == LANDSCAPE) doc = new Document(PageSize.A3.rotate());
			else doc = new Document(PageSize.A3);
			break;
		case 4:
			if(mode == LANDSCAPE) doc = new Document(PageSize.A4.rotate());
			else doc = new Document(PageSize.A4);
			break;
		case 5:
			if(mode == LANDSCAPE) doc = new Document(PageSize.A5.rotate());
			else doc = new Document(PageSize.A5);
			break;
		default:
			doc = new Document(PageSize.A4); // portrait
		}

		doc.setMargins(12, 12, 12, 12);
		
		PdfWriter.getInstance(doc, out);
		doc.addCreationDate();
		
		if(title != null) doc.addTitle(title); // must be set before doc.open() for itext-2.0.4
		if(author != null) doc.addAuthor(author); // must be set before doc.open() for itext-2.0.4

		doc.open();
	}
	
	public void emptyLine(int fontsize) throws DocumentException {
		Font font = new Font();
		font.setSize(fontsize);
		doc.add(new Paragraph(" ", font));
	}
	/**
	 * Adds text to pdf doument, implicit \n.
	 * \n can be used inside str for adding muliple lines at once.
	 * @param str
	 * @param size
	 * @param alignment inbuilt PdfUtil-constants
	 * @throws DocumentException
	 */
	public void addText(String str, float size, int alignment) throws DocumentException {
		Font font = new Font();
		font.setSize(size);
		Paragraph p = new Paragraph(str, font);
		
		switch(alignment) {
		case ALIGN_LEFT:
			p.setAlignment(Element.ALIGN_LEFT);
			break;
		case ALIGN_CENTER:
			p.setAlignment(Element.ALIGN_CENTER);
			break;
		case ALIGN_RIGHT:
			p.setAlignment(Element.ALIGN_RIGHT);
			break;
		default:
			p.setAlignment(Element.ALIGN_LEFT);
		}
		
		Element element = p;
		doc.add(element);
	}
	
	/**
	 * Creates a table header form coloumns in vector
	 * @param tableheader
	 * @param widths absolute values for column widths, can be null
	 * @return identifier needed for {@link #addTableRow(Vector, Integer)} and {@link #addTableToDocument(Integer)}
	 * @throws BadElementException
	 */
	public Integer addTableHeader(Vector<String> tableheader, float[] widths) throws BadElementException {
		tableIdentifier++;
		
		PdfUtilTable pdfut = new PdfUtilTable(tableheader.size());
		Table table = pdfut.getTable();
		table.setCellsFitPage(true);
		table.setWidth(100);
		table.setPadding(2);
		table.setSpacing(0);
		table.setAlignment(Table.ALIGN_LEFT);

		if(widths != null && widths.length == tableheader.size()) {
			table.setWidths(widths);
		}
		
		Font font = new Font();
		font.setSize(8); // bør kunne angis utenfra
		
		Iterator it = tableheader.iterator();
		while(it.hasNext()) {
			String t = (String)it.next();
			Element e = new Paragraph(StringUtils.capitalize(t), font);
			Cell c = new Cell(e);
			c.setGrayFill(0.9f);
			c.setHeader(true);
			table.addCell(c);
		}
		Integer identifier = new Integer(tableIdentifier);
		tables.put(identifier, pdfut);
		return identifier;
	}

	/**
	 * Adds a row to identified table.
	 * All columns must be present (or null)
	 * @param tablerow
	 * @param tableIdentifier see {@link #addTableHeader(Vector)}
	 * @throws BadElementException
	 */
	public void addTableRow(Vector<String> tablerow, Integer tableIdentifier) throws BadElementException {
		PdfUtilTable pdfut = tables.get(tableIdentifier);
		
		pdfut.checkNumberOfColumnsInRow(tablerow);
		
		Table table = pdfut.getTable();
		
		Font font = new Font();
		font.setSize(8); // bør kunne angis utenfra
		
		Iterator it = tablerow.iterator();
		while(it.hasNext()) {
			String t = (String)it.next();
			if(t == null) t = "";
			Element e = new Paragraph(StringUtils.capitalize(t), font);
			Cell c = new Cell(e);
			table.addCell(c);
		}
	}

	
	public void adjustTableColumnsWidths(Integer identifier) {
		// TODO: generic calculation of columns widths should be implemented
		//		table.setWidth(float[]);
	}
	
	/**
	 * Adds created table to pdf document
	 * @param identifier see {@link #addTableHeader(Vector)}
	 * @throws DocumentException
	 */
	public void addTableToDocument(Integer identifier) throws DocumentException {
		PdfUtilTable pdfut = tables.get(identifier);
		doc.add(pdfut.getTable());
	}
	
	/**
	 * Closes pdf file and outputstream
	 */
	public void close() {
		doc.close();
		try { if(out != null) out.close(); }
		catch (IOException e) { log.error("Error closing outputstream", e); }
	}
	
	
	/**
	 * Formats an export of registrations
	 * Should be moved to another class if PdfUtil is to be used for several purposes
	 * @param course
	 * @param registrations
	 */
	public void createRegistrationListExport(Course course, List<Registration> registrations) {
		try {
			// legg til kurs info
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			String start = formatter.format(course.getStartTime());
			String stop = formatter.format(course.getStopTime());
			String date = start.equals(stop)? start : start + " - " + stop;
			
			if("n/a".equalsIgnoreCase(course.getDuration())){
				// pga. SVV's mulighet for å ikke oppgi varighet
				course.setDuration(null);
			}
			String duration = course.getDuration()!=null ? (" (" + course.getDuration() + ")") : "";
			
			this.addText(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.name")) + ": " + course.getName() + " - " + date + duration, 13, PdfUtil.ALIGN_LEFT);
			this.addText(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.responsible")) + ": " + course.getResponsible().getFullName() + "  /  " + StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.instructor")) +  ": " + course.getInstructor().getName(), 9, PdfUtil.ALIGN_LEFT);
			this.addText(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.location")) + ": " + course.getLocation().getName() + ", " + course.getLocation().getAddress(), 9, PdfUtil.ALIGN_LEFT);
			
			TableHeader tableHeader = new TableHeader();
			tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.firstName")), 15);
			tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.lastName")), 15);
			tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.email")), 26);

			if(ApplicationResourcesUtil.isSVV()){
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.organization")), 11);
			}else{
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.municipality")), 11);
			}

			if(!ApplicationResourcesUtil.isSVV()){
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.jobTitle")), 13);
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.servicearea")), 13);
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.workplace")), 12);
			}
			
			tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.phoneNumber.short")), 10);
			tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("user.mobilePhone.short")), 10);

			if(!ApplicationResourcesUtil.isSVV()){
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registration.comment")), 20);
			}

			if(ApplicationResourcesUtil.isSVV()){
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registration.participants")), 8);
			}
			
			if(!ApplicationResourcesUtil.isSVV()){
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registration.invoiced")), 8);
				// tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registration.reserved")), 8);
				tableHeader.add(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registration.attended")), 8);
			}
			
			Integer reserved = this.addTableHeader(tableHeader.getColumnnames(), tableHeader.getWidths());
			Integer waiting = this.addTableHeader(tableHeader.getColumnnames(), tableHeader.getWidths());
			int rCount = 0;
			int wCount = 0;
				
			// legg til deltakere
			Iterator<Registration> it = registrations.iterator();
			while(it.hasNext()) {
				Registration r = it.next();
				Vector<String> row = new Vector<String>();
				row.add(r.getFirstName());
				row.add(r.getLastName());
				row.add(r.getEmail());
				try { row.add(r.getOrganization().getName()); }catch(Exception e) { row.add(""); } // if organization not set

				if(!ApplicationResourcesUtil.isSVV()){
					row.add(r.getJobTitle());
					try { row.add(r.getServiceArea().getName()); }catch(Exception e) { row.add(""); } // if servicearea not set
					row.add(r.getWorkplace());
				}
				
				row.add(r.getPhone());
				row.add(r.getMobilePhone());

				if(!ApplicationResourcesUtil.isSVV()){
					row.add(r.getComment());
				}
				
				if(ApplicationResourcesUtil.isSVV()){
					row.add(""+r.getParticipants());
				}
				
				if(!ApplicationResourcesUtil.isSVV()){
					row.add(r.getInvoiced()?StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("checkbox.checked")):StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("checkbox.unchecked")));
					// row.add(r.getReserved()?StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("checkbox.checked")):StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("checkbox.unchecked")));
					row.add(r.getAttended()?StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("checkbox.checked")):StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("checkbox.unchecked")));
				}

				if(r.getStatusAsEnum() == Status.RESERVED) {
					this.addTableRow(row, reserved);
					rCount += r.getParticipants();
				}
				else if(r.getStatusAsEnum() == Status.WAITING){
					this.addTableRow(row, waiting);
					wCount += r.getParticipants();
				}
			}

			if(rCount > 0 || wCount > 0) {
				String tmp = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationList.attendants")) + ": " + rCount;
				if(!ApplicationResourcesUtil.isSVV()){
					tmp += "  /  " + StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.waitlist")) + ": " + wCount; 
				}
				tmp += "  (" + StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationsSent.updated")) + " " + formatter.format(new Date()) + ")"; 
				this.addText(tmp, 9, PdfUtil.ALIGN_LEFT);
				this.emptyLine(10);
			}
			if(rCount > 0) {
				this.addText(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationList.attendants")), 11, PdfUtil.ALIGN_LEFT);
				this.addTableToDocument(reserved);
			}
			if(rCount > 0 && wCount > 0) this.emptyLine(10);
			if(wCount > 0) {
				this.addText(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.waitlist")), 11, PdfUtil.ALIGN_LEFT);
				this.addTableToDocument(waiting);
			}
			this.close();
			
		} catch (Exception e) {
			log.error("Error creating pdf document", e);
		}
	}
	
	
	/**
	 * For testing...
	 */
	public static void main(String args[]) {
		try {
			
			// File test
			
			File test = new File("C:\\Users\\sa\\PdfUtilTest.pdf");
			
			PdfUtil pu = new PdfUtil(test.getPath(), A4, PdfUtil.LANDSCAPE, "PdfTest", "Sindre Amundsen");
			
			pu.addText("Testing av PdfUtil.java", 12, PdfUtil.ALIGN_CENTER);
			
			Vector<String> header = new Vector<String>();
			header.add("Fornavn");
			header.add("Etternavn");
			header.add("Epost");
			header.add("Sted");
			header.add("Mobil");
			Integer identifier = pu.addTableHeader(header, null);

			Vector<String> row = new Vector<String>();
			row.add("Sindre");
			row.add("Amundsen");
			row.add("sindre.amundsen@knowit.no");
			row.add("Oslo");
			row.add(null); // hvis denne ikke er med vil BadElementException kastes
			pu.addTableRow(row, identifier);
			
			Vector<String> row2 = new Vector<String>();
			row2.add("Kjell");
			row2.add("Karlsen");
			row2.add("kjell.karlsen@norge.no");
			row2.add("Oslo");
			row2.add("905 97 725");
			pu.addTableRow(row2, identifier);
			
			pu.addTableToDocument(identifier);
			
			pu.addText("Litt mer tekst..!", 12, PdfUtil.ALIGN_LEFT);
			
			// en tabell til
			Vector<String> header2 = new Vector<String>();
			header2.add("Fornavn");
			header2.add("Etternavn");
			header2.add("Epost");
			header2.add("Sted");
			Integer identifier2 = pu.addTableHeader(header2, null);

			Vector<String> row3 = new Vector<String>();
			row3.add("Gunnar");
			row3.add("Karlsen");
			row3.add("gunnar.karlsen@norge.no");
			row3.add("Oslo");
			pu.addTableRow(row3, identifier2);

			Vector<String> row4 = new Vector<String>();
			row4.add("Henriette");
			row4.add("Amundsen");
			row4.add("henriette.amundsen@knowit.no");
			row4.add("Oslo");
			pu.addTableRow(row4, identifier2);
			
			pu.addTableToDocument(identifier2);

			pu.addText("Enda litt mer tekst..!", 5, PdfUtil.ALIGN_RIGHT);

			pu.close();
			
			System.out.println("Pdf created: " + test.getPath());
			
		} catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	/**
	 * Wrapper to verify that header / rows mathes
	 * @author sa
	 */
	private class PdfUtilTable {
		
		private Table table;
		private int columnsInHeader;
		
		protected PdfUtilTable(int numberOfCols) throws BadElementException {
			table = new Table(numberOfCols);
			columnsInHeader = numberOfCols;
		}
		
		protected Table getTable() {
			return table;
		}
		
		protected void checkNumberOfColumnsInRow(Vector<String> row)
				throws BadElementException {
			if (row.size() != columnsInHeader)
				throw new BadElementException("Number of columns in row ("
						+ row.size() + ") does not match columns in header ("
						+ columnsInHeader + ")");
		}
	}

	/**
	 * Klasse for å "dynamisk" kunne bygge opp width[] for tilpassede kolonnebredder i tabell
	 * @author extsam
	 */
	private class TableHeader {
		Vector<String> c = null;
		Vector<Float> w = null;
		
		public TableHeader(){
			c = new Vector<String>();
			w = new Vector<Float>();
		}

		public void add(String columnname, float approxWidth){
			c.add(columnname);
			w.add(approxWidth);
		}
		
		public Vector<String> getColumnnames(){
			return c;
		}
		
		public float[] getWidths(){
			int num = w.size();
			float[] widths = new float[num];
			for(int i=0; i<w.size(); i++){
				widths[i] = w.elementAt(i).floatValue();
			}
			return widths;
		}
	}
	
}

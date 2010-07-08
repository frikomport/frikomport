package no.unified.soak.webapp.export;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.displaytag.Messages;
import org.displaytag.exception.BaseNestableJspTagException;
import org.displaytag.exception.SeverityEnum;
import org.displaytag.export.BinaryExportView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;


/**
 * @author sa
 */
public class RegistrationListExcelExportView implements BinaryExportView
{

    /**
     * TableModel to render.
     */
    private TableModel model;

    /**
     * export full list?
     */
    private boolean exportFull;

    /**
     * include header in export?
     */
    private boolean header;

    /**
     * decorate export?
     */
    private boolean decorated;

    /**
     * Name of Excel Spreadsheet
     */
    private String sheetName;

    /**
     * Workbook
     */
    private HSSFWorkbook wb;

    /**
     * Worksheet
     */
    private HSSFSheet sheet;

    /*
     * Available already configured cell styles, as HSSF JavaDoc claims there are limits to cell styles.
     */
    private Map<CellFormatTypes, HSSFCellStyle> cellStyles = new HashMap<CellFormatTypes, HSSFCellStyle>();

    public RegistrationListExcelExportView()
    {
    }

    /**
     * @see org.displaytag.export.ExportView#setParameters(TableModel, boolean, boolean, boolean)
     */
    public void setParameters(TableModel tableModel, boolean exportFullList, boolean includeHeader,
        boolean decorateValues)
    {
        this.model = tableModel;
        this.exportFull = exportFullList;
        this.header = includeHeader;
        this.decorated = decorateValues;
        setWb(new HSSFWorkbook());
    }

    /**
     * @return "application/vnd.ms-excel"
     * @see org.displaytag.export.BaseExportView#getMimeType()
     */
    public String getMimeType()
    {
        return "application/vnd.ms-excel"; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BinaryExportView#doExport(OutputStream)
     */
    public void doExport(OutputStream out) throws JspException
    {
        try
        {
            String inputSheetName = "RegistrationList"; //this.model.getProperties().getProperty(EXCEL_SHEET_NAME);
            setSheetName(inputSheetName);
            setSheet(getWb().createSheet(getSheetName()));

            int rowNum = addCourseInfo();
            short colNum = 0;

            if (this.header)
            {
                // Create an header row
                HSSFRow xlsRow = sheet.createRow(rowNum++);

                Iterator iterator = this.model.getHeaderCellList().iterator();

                while (iterator.hasNext())
                {
                    HeaderCell headerCell = (HeaderCell) iterator.next();

                    HSSFCell cell = xlsRow.createCell( colNum++);
                    cell.setCellValue(new HSSFRichTextString(getHeaderCellValue(headerCell)));
                    cell.setCellStyle(createHeaderStyle(getWb(), headerCell));
                }
            }

            // get the correct iterator (full or partial list according to the exportFull field)
            RowIterator rowIterator = this.model.getRowIterator(this.exportFull);

            // iterator on rows
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                HSSFRow xlsRow = getSheet().createRow(rowNum++);
                colNum = 0;

                // iterator on columns
                ColumnIterator columnIterator = row.getColumnIterator(this.model.getHeaderCellList());

                while (columnIterator.hasNext())
                {
                    Column column = columnIterator.nextColumn();

                    // Get the value to be displayed for the column
                    Object value = column.getValue(this.decorated);

                    HSSFCell cell = xlsRow.createCell(colNum++);
                    writeCell(value, cell);
                }
            }

            createTotalsRow(getSheet(), rowNum, this.model);

            autosizeColumns();

            getWb().write(out);
        }
        catch (Exception e)
        {
            throw new ExcelGenerationException(e);
        }
    }

    /**
     * Adds information about course to sheet
     * @param rowNum
     */
    private int addCourseInfo() {
    	int rowNum = 0; // in sheet
    	int rCount = 0; // attendants
    	int wCount = 0; // attendants on waitlist
    	
    	// find course from first registration, then calculate registered attendands / attendants on waitlist
    	boolean firstRegistration = true;
    	DateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    	RowIterator rowIterator = this.model.getRowIterator(this.exportFull);
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            if(row.getObject() instanceof Registration) {
            	Registration registration = (Registration)row.getObject();
            	if(firstRegistration) {
	            	Course course = registration.getCourse();
	            	String start = f.format(course.getStartTime());
	            	String stop = f.format(course.getStopTime());
	            	String date = start.equals(stop)? start : start + " - " + stop;
	            	String duration = course.getDuration()!=null ? course.getDuration() : "";
	            	if(ApplicationResourcesUtil.isSVV()){
	            		duration = "";
	            	}
	            	
	            	short colNum = 0;
	            	// adds coursename, date, duration
	            	HSSFRow r = sheet.createRow(rowNum++);
	                writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.name")), r.createCell(colNum++));
	                writeCell(course.getName(), r.createCell(colNum++));
	                writeCell(date, r.createCell(colNum++));
	                writeCell(duration, r.createCell(colNum++));
	
	                colNum = 0;
	                // adds courseresponsible, instructor
	                r = sheet.createRow(rowNum++);
	                writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.responsible")), r.createCell(colNum++));
	                writeCell(course.getResponsible().getFullName(), r.createCell(colNum++));
	                writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.instructor")), r.createCell(colNum++));
	                writeCell(course.getInstructor().getName(), r.createCell(colNum++));
	
	                colNum = 0;
	                // adds location
	                r = sheet.createRow(rowNum++);
	                writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.location")), r.createCell(colNum++));
	                writeCell(course.getLocation().getName(), r.createCell(colNum++));
	                writeCell(course.getLocation().getAddress(), r.createCell(colNum++));
	                
	                firstRegistration = false;
            	}
				if(registration.getReserved()) {
					rCount++;
				}
				else {
					wCount++;
				}
            }
        }
		if(rCount > 0 || wCount > 0) {
			short colNum = 0;
            // adds attendant counts and time of update
			HSSFRow r = sheet.createRow(rowNum++);
            writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationList.attendants")) + ": " + rCount, r.createCell(colNum++));
            writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.waitlist")) + ": " + wCount, r.createCell(colNum++));
            writeCell(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationsSent.updated")) + " " + f.format(new Date()), r.createCell(colNum++));

            // empty row
            colNum = 0;
            r = sheet.createRow(rowNum++);
            writeCell("", r.createCell(colNum++));
		}
		return rowNum;
    }
    
    /**
     * Uses POI Autosizing.
     *
     * WARNING.  This has been known to cause performance problems and various exceptions.  use at your own risk!  Overriding this method is suggested.
     *
     * From POI HSSF documentation for autoSizeColumn:
     *  "To calculate column width HSSFSheet.autoSizeColumn uses Java2D classes that throw exception if graphical environment is not available.
     *  In case if graphical environment is not available, you must tell Java that you are running in headless mode and set the following system property:  java.awt.headless=true."
     */
    protected void autosizeColumns() {
        for (short i=0; i < getModel().getNumberOfColumns(); i++)
        {
            getSheet().autoSizeColumn((short) i);
            // since this usually creates column widths that are just too short, adjust here!
            // gives column width an extra character
            short width = getSheet().getColumnWidth(i);
            width += 256;
            getSheet().setColumnWidth(i, width);
        }
    }

    /**
     * Write the value to the cell.  Override this method if you have complex data types that may need to be exported.
     * @param value the value of the cell
     * @param cell the cell to write it to
     */
    protected void writeCell(Object value, HSSFCell cell)
    {
    	if (value == null) {
            cell.setCellValue(new HSSFRichTextString(""));
        }
//        else if (value instanceof Integer)
//        {
//            Integer integer = (Integer) value;
//            // due to a weird bug in HSSF where it uses shorts, we need to input this as a double value :(
//            cell.setCellValue(integer.doubleValue());
//            cell.setCellStyle(cellStyles.get(CellFormatTypes.INTEGER));
//        }
//        else if (value instanceof Number)
//        {
//            Number num = (Number) value;
//            if (num.equals(Double.NaN))
//            {
//                cell.setCellValue(new HSSFRichTextString(""));
//            }
//            else
//            {
//                cell.setCellValue(num.doubleValue());
//            }
//            cell.setCellStyle(cellStyles.get(CellFormatTypes.NUMBER));
//        }
//        else if (value instanceof Date)
//        {
//            cell.setCellValue((Date) value);
//            cell.setCellStyle(cellStyles.get(CellFormatTypes.DATE));
//        }
//        else if (value instanceof Calendar)
//        {
//            cell.setCellValue((Calendar) value);
//            cell.setCellStyle(cellStyles.get(CellFormatTypes.DATE));
//        }
        else
        {
            cell.setCellValue(new HSSFRichTextString(escapeColumnValue(value)));
        }
    }

    // patch from Karsten Voges
    /**
     * Escape certain values that are not permitted in excel cells.
     * @param rawValue the object value
     * @return the escaped value
     */
    protected String escapeColumnValue(Object rawValue)
    {
        if (rawValue == null)
        {
            return null;
        }
        String returnString = ObjectUtils.toString(rawValue);
        // escape the String to get the tabs, returns, newline explicit as \t \r \n
        returnString = StringEscapeUtils.escapeJava(StringUtils.trimToEmpty(returnString));
        // remove tabs, insert four whitespaces instead
        returnString = StringUtils.replace(StringUtils.trim(returnString), "\\t", "    ");
        // remove the return, only newline valid in excel
        returnString = StringUtils.replace(StringUtils.trim(returnString), "\\r", " ");
        // unescape so that \n gets back to newline
        returnString = StringEscapeUtils.unescapeJava(returnString);
        return returnString;
    }

    /**
     * Templated method that is called for all non-header & non-total cells.
     * @param wb
     * @param rowCtr
     * @param column
     * @return
     */
    public HSSFCellStyle createRowStyle(HSSFWorkbook wb, int rowCtr, Column column)
    {
        return wb.createCellStyle();
    }

    public String getHeaderCellValue(HeaderCell headerCell)
    {
        String columnHeader = headerCell.getTitle();

        if (columnHeader == null)
        {
            columnHeader = StringUtils.capitalize(headerCell.getBeanPropertyName());
        }

        return columnHeader;
    }

    /**
     * Templated method that is called for all header cells.
     * @param wb
     * @param headerCell
     * @return
     */
    public HSSFCellStyle createHeaderStyle(HSSFWorkbook wb, HeaderCell headerCell)
    {
        HSSFCellStyle headerStyle = getNewCellStyle();

        headerStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);
        headerStyle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);
        HSSFFont bold = wb.createFont();
        bold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        bold.setColor(HSSFColor.WHITE.index);
        headerStyle.setFont(bold);

        return headerStyle;
    }

    /**
     * Templated method that is used if a totals row is desired.
     * @param sheet
     * @param rowNum
     * @param tableModel
     */
    public void createTotalsRow(HSSFSheet sheet, int rowNum, TableModel tableModel)
    {
    }

    public TableModel getTableModel()
    {
        return model;
    }

    public boolean isExportFull()
    {
        return exportFull;
    }

    public boolean isIncludeHeaderInExport()
    {
        return header;
    }

    public boolean isDecorateExport()
    {
        return decorated;
    }

    public String getSheetName()
    {
        return sheetName;
    }

    public void setSheetName(String sheetName) throws JspException
    {
        // this is due to either the POI limitations or excel (I'm not sure). you get the following error if you don't do this:
        // Exception: [.ExcelHssfView] !ExcelView.errorexporting! Cause: Sheet name cannot be blank, greater than 31 chars, or contain any of /\*?[]
        if (StringUtils.isBlank(sheetName))
        {
            throw new JspException("The sheetname must not be blank.");
        }
        sheetName =  sheetName.replaceAll("/|\\\\|\\*|\\?|\\[|\\]","");
        this.sheetName = sheetName.length() <= 31 ? sheetName : sheetName.substring(0,31-3) + "...";
    }

    public HSSFCellStyle getNewCellStyle()
    {
        return getWb() == null ? null : getWb().createCellStyle();
    }

    public HSSFWorkbook getWb()
    {
        return wb;
    }

    public void setWb(HSSFWorkbook wb)
    {
        this.wb = wb;
    }

    public HSSFSheet getSheet()
    {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet)
    {
        this.sheet = sheet;
    }

    public TableModel getModel()
    {
        return model;
    }

    public void setModel(TableModel model)
    {
        this.model = model;
    }

    public enum CellFormatTypes
    {
        INTEGER,
        NUMBER,
        DATE
    }

    /**
     * Wraps IText-generated exceptions.
     */
    static class ExcelGenerationException extends BaseNestableJspTagException
    {

        /**
         * D1597A17A6.
         */
        private static final long serialVersionUID = 899149338534L;

        /**
         * Instantiate a new PdfGenerationException with a fixed message and the given cause.
         * @param cause Previous exception
         */
        public ExcelGenerationException(Throwable cause)
        {
            super(RegistrationListExcelExportView.class, Messages.getString("ExcelView.errorexporting"), cause); //$NON-NLS-1$
        }

        /**
         * @see org.displaytag.exception.BaseNestableJspTagException#getSeverity()
         */
        public SeverityEnum getSeverity()
        {
            return SeverityEnum.ERROR;
        }
    }

}

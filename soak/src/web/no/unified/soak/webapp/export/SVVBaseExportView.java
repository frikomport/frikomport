package no.unified.soak.webapp.export;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.export.TextExportView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;


/**
 * Base abstract class for simple exports customized for FriKomPort
 * @author sa
 */
public abstract class SVVBaseExportView implements TextExportView
{

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(SVVBaseExportView.class);

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
     * @see org.displaytag.export.ExportView#setParameters(org.displaytag.model.TableModel, boolean, boolean, boolean)
     */
    public void setParameters(TableModel tableModel, boolean exportFullList, boolean includeHeader,
        boolean decorateValues)
    {
        this.model = tableModel;
        this.exportFull = exportFullList;
        this.header = includeHeader;
        this.decorated = decorateValues;
    }

    /**
     * String to add before a row.
     * @return String
     */
    protected String getRowStart()
    {
        return null;
    }

    /**
     * String to add after a row.
     * @return String
     */
    protected String getRowEnd()
    {
        return null;
    }

    /**
     * String to add before a cell.
     * @return String
     */
    protected String getCellStart()
    {
        return null;
    }

    /**
     * String to add after a cell.
     * @return String
     */
    protected abstract String getCellEnd();

    /**
     * String to add to the top of document.
     * @return String
     */
    protected String getDocumentStart()
    {
        return null;
    }

    /**
     * String to add to the end of document.
     * @return String
     */
    protected String getDocumentEnd()
    {
        return null;
    }

    /**
     * always append cell end string?
     * @return boolean
     */
    protected abstract boolean getAlwaysAppendCellEnd();

    /**
     * always append row end string?
     * @return boolean
     */
    protected abstract boolean getAlwaysAppendRowEnd();

    /**
     * can be implemented to escape values for different output.
     * @param value original column value
     * @return escaped column value
     */
    protected abstract String escapeColumnValue(Object value);

    /**
     * Write table header.
     * @return String rendered header
     */
    protected String doHeaders()
    {
        final String ROW_START = getRowStart();
        final String ROW_END = getRowEnd();
        final String CELL_START = getCellStart();
        final String CELL_END = getCellEnd();
        final boolean ALWAYS_APPEND_CELL_END = getAlwaysAppendCellEnd();

        StringBuffer buffer = new StringBuffer(1000);

        Iterator<HeaderCell> iterator = this.model.getHeaderCellList().iterator();

        // start row
        if (ROW_START != null)
        {
            buffer.append(ROW_START);
        }

        while (iterator.hasNext())
        {
            HeaderCell headerCell = iterator.next();

            String columnHeader = headerCell.getTitle();

            if (columnHeader == null)
            {
                columnHeader = StringUtils.capitalize(headerCell.getBeanPropertyName());
            }

            columnHeader = escapeColumnValue(columnHeader);

            if (CELL_START != null)
            {
                buffer.append(CELL_START);
            }

            if (columnHeader != null)
            {
                buffer.append(columnHeader);
            }

            if (CELL_END != null && (ALWAYS_APPEND_CELL_END || iterator.hasNext()))
            {
                buffer.append(CELL_END);
            }
        }

        // end row
        if (ROW_END != null)
        {
            buffer.append(ROW_END);
        }

        return buffer.toString();

    }

    /**
     * @see org.displaytag.export.TextExportView#doExport(java.io.Writer)
     */
    public void doExport(Writer out) throws IOException, JspException
    {

    	if (log.isDebugEnabled())
        {
            log.debug(getClass().getName());
        }

        final String DOCUMENT_START = getDocumentStart();
        final String DOCUMENT_END = getDocumentEnd();
        final String ROW_START = getRowStart();
        final String ROW_END = getRowEnd();
        final String CELL_START = getCellStart();
        final String CELL_END = getCellEnd();
        final boolean ALWAYS_APPEND_CELL_END = getAlwaysAppendCellEnd();
        final boolean ALWAYS_APPEND_ROW_END = getAlwaysAppendRowEnd();

        // document start
        write(out, DOCUMENT_START);

        if (this.header)
        {
            write(out, doHeaders());
        }

        // get the correct iterator (full or partial list according to the exportFull field)
        RowIterator rowIterator = this.model.getRowIterator(this.exportFull);

        // iterator on rows
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();

            if (this.model.getTableDecorator() != null)
            {

                String stringStartRow = this.model.getTableDecorator().startRow();
                write(out, stringStartRow);
            }

            // iterator on columns
            ColumnIterator columnIterator = row.getColumnIterator(this.model.getHeaderCellList());

            write(out, ROW_START);

            while (columnIterator.hasNext())
            {
                Column column = columnIterator.nextColumn();

                // Get the value to be displayed for the column
                String value = escapeColumnValue(column.getValue(this.decorated));

                write(out, CELL_START);

                write(out, value);

                if (ALWAYS_APPEND_CELL_END || columnIterator.hasNext())
                {
                    write(out, CELL_END);
                }

            }
            if (ALWAYS_APPEND_ROW_END || rowIterator.hasNext())
            {
                write(out, ROW_END);
            }
        }
        
        if(this.model.getFooter() != null){
        	String footerStr = this.model.getFooter();
        	Vector columns = new Vector<String>();
        	String[] footerArr = footerStr.split("<td");
        	for(int c=1; c < footerArr.length; c++ ){
        		// finn value fra td'er
        		String td = footerArr[c];
        		int startIdx = td.indexOf(">")+1;
        		String value = td.substring(startIdx, td.indexOf("<", startIdx));
        		columns.addElement("" + value);
        		
        		int idx = 0;
        		if((idx = td.indexOf("colspan=\"")) != -1){
        			// legg til ekstra kolonner dersom colspan er benyttet p� td
        			idx += "colspan=\"".length();
        			int extraColumnsToAdd = Integer.parseInt(td.substring(idx, td.indexOf("\"", idx))) - 1;
        			for(int e=0; e < extraColumnsToAdd; e++){
        				columns.addElement("");
        			}
        		}
        	}
        	write(out, ROW_START);
        	for(int f=0; f<columns.size(); f++){
                String value = escapeColumnValue((String)columns.elementAt(f));
                write(out, CELL_START);
                write(out, value);
                write(out, CELL_END);
        	}
            write(out, ROW_END);
        }        

        // document end
        write(out, DOCUMENT_END);
    }

	/**
     * Write a String, checking for nulls value.
     * @param out output writer
     * @param string String to be written
     * @throws IOException thrown by out.write
     */
    private void write(Writer out, String string) throws IOException
    {
        if (string != null)
        {
            out.write(string);
        }
    }

    /**
     * @see org.displaytag.export.TextExportView#outputPage()
     */
    public boolean outputPage()
    {
        return false;
    }
}
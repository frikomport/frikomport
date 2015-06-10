package no.unified.soak.webapp.export;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import no.unified.soak.model.Course;
import no.unified.soak.model.Followup;
import no.unified.soak.model.Registration;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.lang.StringEscapeUtils;
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
public abstract class CustomBaseExportView implements TextExportView
{

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(CustomBaseExportView.class);

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

        addCourseInfoIfOk(out);

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

        // document end
        write(out, DOCUMENT_END);
    }

    /**
     * Adds information about course to csv sheet
     */
    private void addCourseInfoIfOk(Writer outWriter) throws IOException {
    	StringBuffer out = new StringBuffer(200);
        final String ROW_START = getRowStart();
        final String ROW_END = getRowEnd();
        final String CELL_START = getCellStart();
        final String CELL_END = getCellEnd();
    	
    	int rCount = 0; // attendants
    	int wCount = 0; // attendants on waitlist
    	
    	// find course from first registration, then calculate registered attendands / attendants on waitlist
    	boolean firstRegistration = true;
    	Long theCourseId = null;
    	
    	//Only add course info if all registrations belong to the same course.
    	boolean doAddCourseInfo = true; 
    	DateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    	RowIterator rowIterator = this.model.getRowIterator(this.exportFull);
		loop: while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (!(row.getObject() instanceof Registration)) {
				// An object is not a registration, so quit adding course info.
				doAddCourseInfo = false;
				break loop;
			} else {
				Registration registration = (Registration) row.getObject();
				if (theCourseId != null && theCourseId != registration.getCourse().getId()) {
					doAddCourseInfo = false;
					break loop;
				}
				theCourseId = registration.getCourse().getId();
				if (firstRegistration) {
					out = makeCourseInfo(registration);
					firstRegistration = false;

				}
				if (registration.getReserved()) {
					rCount += registration.getParticipants();
				} else {
					wCount += registration.getParticipants();
				}
			}
		}
		if (doAddCourseInfo) {
			if (rCount > 0 || wCount > 0) {
				// adds attendant counts and time of update
				write(out, ROW_START);
				write(out, CELL_START);
				write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil
						.getText("registrationList.attendants"))
						+ ": " + rCount));
				write(out, CELL_END);

	            if(!ApplicationResourcesUtil.isSVV()){
					write(out, CELL_START);
					write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.waitlist"))
							+ ": " + wCount));
					write(out, CELL_END);
	            }
	            
				write(out, CELL_START);
				write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil
						.getText("registrationsSent.updated"))
						+ " " + f.format(new Date())));
				write(out, CELL_END);
				write(out, ROW_END);

				// empty row
				write(out, ROW_START);
				write(out, CELL_START);
				write(out, escapeColumnValue(""));
				write(out, CELL_END);
				write(out, ROW_END);
			}
			write(outWriter, out.toString());
		}
    }

	private StringBuffer makeCourseInfo(Registration registration) {
        final String ROW_START = getRowStart();
        final String ROW_END = getRowEnd();
        final String CELL_START = getCellStart();
        final String CELL_END = getCellEnd();
    	DateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    	StringBuffer out = new StringBuffer(200);

		Course course = registration.getCourse();
		String start = f.format(course.getStartTime());
		String stop = f.format(course.getStopTime());
		String date = start.equals(stop) ? start : start + " - " + stop;

		if("n/a".equalsIgnoreCase(course.getDuration())){
			// pga. SVV's mulighet for å ikke oppgi varighet
			course.setDuration(null);
		}
		String duration = course.getDuration()!=null ? (" (" + course.getDuration() + ")") : "";

		// adds coursename, date, duration
		write(out , ROW_START);
		write(out, CELL_START);
		write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.name"))));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(course.getName()));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(date));
		write(out, CELL_END);

		if (!ApplicationResourcesUtil.isSVV()) {
			write(out, CELL_START);
			write(out, escapeColumnValue(duration));
			write(out, CELL_END);
		}
		write(out, ROW_END);

		// adds courseresponsible, instructor
		write(out, ROW_START);
		write(out, CELL_START);
		write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil
				.getText("course.responsible"))));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(course.getResponsible().getFullName()));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil
				.getText("course.instructor"))));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(course.getInstructor().getName()));
		write(out, CELL_END);
		write(out, ROW_END);

		// adds location
		write(out, ROW_START);
		write(out, CELL_START);
		write(out, escapeColumnValue(StringEscapeUtils
				.unescapeHtml(ApplicationResourcesUtil.getText("course.location"))));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(course.getLocation().getName()));
		write(out, CELL_END);

		write(out, CELL_START);
		write(out, escapeColumnValue(course.getLocation().getAddress()));
		write(out, CELL_END);
		write(out, ROW_END);

        if (course.hasFollowup()) {
            Followup followup = course.getFollowup();
            write(out, ROW_START);
            write(out, CELL_START);
            write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("followup.title"))));
            write(out, CELL_END);
            write(out, ROW_END);

            write(out, ROW_START);
            write(out, CELL_START);
            write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("followup.startTime"))));
            write(out, CELL_END);
            write(out, CELL_START);
            write(out, escapeColumnValue(f.format(followup.getStartTime()) + " - " + f.format(followup.getStopTime())));
            write(out, CELL_END);
            write(out, ROW_END);

            write(out, ROW_START);
            write(out, CELL_START);
            write(out, escapeColumnValue(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("followup.location"))));
            write(out, CELL_END);
            write(out, CELL_START);
            write(out, escapeColumnValue(followup.getLocation().getName()));
            write(out, CELL_END);
            write(out, CELL_START);
            write(out, escapeColumnValue(followup.getLocation().getAddress()));
            write(out, CELL_END);
            write(out, ROW_END);

            write(out, ROW_START);
            write(out, CELL_START);
            write(out, escapeColumnValue(""));
            write(out, CELL_END);
            write(out, ROW_END);
        }

		return out;
	}

    
	private void write(StringBuffer out, String string) {
		if (string != null) {
			out.append(string);
		}
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
package no.unified.soak.webapp.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.jsp.JspException;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import no.unified.soak.model.Course;
import no.unified.soak.util.MailUtil;

import org.displaytag.export.BinaryExportView;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;

public class CourseICalExportView implements BinaryExportView{
    private String mimeType = "text/calendar";
    private TableModel model;
    private boolean exportFull = true;
    
    public boolean outputPage() {
        return false;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setParameters(TableModel model, boolean exportFull, boolean includeHeader, boolean decorateValues) {
        this.model = model;
    }

    public void doExport(OutputStream outputstream) throws IOException,
            JspException {
        RowIterator rowIterator = this.model.getRowIterator(this.exportFull);
        Calendar calendar = MailUtil.createCalendar();
        CalendarOutputter output = new CalendarOutputter();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Kun gyldig for Course
            if (row.getObject() instanceof Course) {
                Course course = (Course)row.getObject();
                VEvent event = MailUtil.getVEvent(course);
                calendar.getComponents().add(event);
            }
        }
        try {
            output.output(calendar, outputstream);
        } catch (ValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}

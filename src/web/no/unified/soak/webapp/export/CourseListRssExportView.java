package no.unified.soak.webapp.export;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;

import no.unified.soak.model.Course;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.displaytag.export.XmlView;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;

public class CourseListRssExportView extends XmlView {

	private TableModel model = null;
	private boolean exportFull = true;
	
	private StringBuffer feed = null;
	private String baseURL = null;
	
	
	public String getMimeType() {
		return "application/rss+xml";
	}
	
    /**
     * @see org.displaytag.export.BaseExportView#setParameters(TableModel, boolean, boolean, boolean)
     */
    public void setParameters(TableModel tableModel, boolean exportFullList, boolean includeHeader,
        boolean decorateValues)
    {
    	this.model = tableModel;
    	this.exportFull = exportFullList;
    }
	
    /**
     * @see org.displaytag.export.TextExportView#doExport(java.io.Writer)
     */
    public void doExport(Writer out) throws IOException, JspException
    {

    	baseURL = ApplicationResourcesUtil.getText("javaapp.baseurl");
    	
        RowIterator rowIterator = this.model.getRowIterator(this.exportFull);

        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            if(row.getObject() instanceof Course) {
            	Course course = (Course)row.getObject();
            	addCourseToFeed(course);
            }
        }
        if(feed != null) {
        	finalizeFeed();
        	write(out, feed.toString());
        	out.close();
        }
    }
    
    private void addCourseToFeed(Course course) {
    	if(feed == null) feed = new StringBuffer();
    	
        DateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    	String start = " - " + ApplicationResourcesUtil.getText("export.startup") + " " + f.format(course.getStartTime());
    	feed.append("<item>");
    	feed.append("<title><![CDATA[" + course.getName() + "]]></title>");
    	feed.append("<link><![CDATA[" + baseURL + ApplicationResourcesUtil.getText("javaapp.courseurl") + course.getId() +"]]></link>");
    	feed.append("<description><![CDATA[" + course.getDescription() + start + "]]></description>");
    	feed.append("</item>");
    }
    
    private void finalizeFeed() {
    	String header = "<?xml version=\"1.0\"?><rss version=\"2.0\"><channel>";
    	header += "<title><![CDATA[" + ApplicationResourcesUtil.getText("webapp.prefix") + ApplicationResourcesUtil.getText("courseList.title") + "]]></title>";
    	header += "<description><![CDATA[" + ApplicationResourcesUtil.getText("courseList.descriprion") + "]]></description>";
    	header += "<link><![CDATA[" + baseURL + "listCourses.html" + "]]></link>";
    	
    	String footer = "</channel></rss>";

    	StringBuffer finalizedFeed = new StringBuffer();
    	finalizedFeed.append(header);
    	finalizedFeed.append(feed);
    	finalizedFeed.append(footer);
    	
    	feed = finalizedFeed;
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
	
}

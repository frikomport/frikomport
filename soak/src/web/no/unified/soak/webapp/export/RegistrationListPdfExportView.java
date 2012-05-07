package no.unified.soak.webapp.export;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.util.PdfUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.export.DefaultItextExportView;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;


/**
 * FriKomPort-modified PDF exporter using iText.
 */
public class RegistrationListPdfExportView extends DefaultItextExportView
{

    private final Log log = LogFactory.getLog(RegistrationListPdfExportView.class);

    private TableModel model;
    private boolean exportFull = true;
    
    /**
     * @see org.displaytag.export.BaseExportView#getMimeType()
     * @return "application/pdf"
     */
    public String getMimeType()
    {
        return "application/pdf"; //$NON-NLS-1$
    }

    public void setParameters(TableModel model, boolean exportFull, boolean includeHeader, boolean decorateValues) {
        this.model = model;
    }
    
    public void doExport(OutputStream outputstream) throws JspException {
    	try
    	{
            // Proprietor implementation for adding header info to registration list exports
			boolean firstRegistration = true;
			Course course = null;
			List<Registration> registrations = new ArrayList<Registration>();
	    	RowIterator rowIterator = this.model.getRowIterator(this.exportFull);
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            if (row.getObject() instanceof Registration) {
	            	
	            	Registration registration = (Registration)row.getObject();
	            	registrations.add(registration);
	            	if(firstRegistration) {
		                course = registration.getCourse();		                
						firstRegistration = false;
	            	}
	            }
	        }
	        createPdf(outputstream, course, registrations);
    	}
        catch (Exception e)
        {
        	/* must do nothing in order to complete export via PdfUtil */
        }
    }
    
	private void createPdf(OutputStream out, Course course, List<Registration> registrations) {

		PdfUtil pdf = null;
		try {
			pdf = new PdfUtil(out, PdfUtil.A4, PdfUtil.LANDSCAPE, "Registrationlist: " + course.getName(), "FriKomPort");
			pdf.createRegistrationListExport(course, registrations);
		} catch (Exception e) {
			log.error("Error creating pdf document", e);
		} finally {
			if(pdf != null)
				pdf.close();
		}
	}

	
	protected void initItextWriter(Document document, OutputStream out) throws DocumentException {}

}


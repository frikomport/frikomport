/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created January 21th 2006
 */
package no.unified.soak.webapp.util;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class contains helpful functions for dealing with files in the web layer
 *
 * @author hrj
 */
public class FileUtil {
    /**
     * Streams a file to the user for download
     *
     * @param request -
     *            the HTTP Request object
     * @param response -
     *            the HTTP response object
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void downloadFile(HttpServletRequest request,
        HttpServletResponse response, String contentType,
        String storedFilename, String outFilename)
        throws IOException, FileNotFoundException {
        response.setContentType(contentType);

        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + outFilename + "\"");

        ServletOutputStream out = response.getOutputStream();

        // print the file
        InputStream in = null;

        try {
            in = new BufferedInputStream(new FileInputStream(storedFilename));

            int ch;

            while ((ch = in.read()) != -1) {
                out.print((char) ch);
            }
        } finally {
            if (in != null) {
                in.close(); // very important
            }
        }

        out.close();
    }

    /**
     * Recieves a file from the client
     *
     * @param file
     *            The file
     * @param uploadDir
     *            Where to put it on the server
     * @param storedname
     *            What to name it on the server
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void recieveFile(CommonsMultipartFile file, String uploadDir,
        String storedname) throws IOException, FileNotFoundException {
        // retrieve the file data
        InputStream stream = file.getInputStream();

        // write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + storedname);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();
        // close the stream
        stream.close();
    }
}

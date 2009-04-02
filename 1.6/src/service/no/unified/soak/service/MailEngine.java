/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import org.springframework.core.io.ClassPathResource;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Class for sending e-mail messages based on Velocity templates
 * or with attachments.
 *
 * <p><a href="MailEngine.java.html"><i>View Source</i></a></p>
 *
 * @author Matt Raible
 */
public class MailEngine {
    protected static final Log log = LogFactory.getLog(MailEngine.class);
    private MailSender mailSender;
    private VelocityEngine velocityEngine;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * Send a simple message based on a Velocity template.
     * @param msg
     * @param templateName
     * @param model
     */
    public void sendMessage(SimpleMailMessage msg, String templateName, Map model) {
        String result = null;

        try {
            result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
        }

        msg.setText(result);
        send(msg);
    }

    /**
     * Send a simple message with pre-populated values.
     * @param msg
     */
    public void send(SimpleMailMessage msg) {
        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            //log it and go on
            log.error(ex.getMessage());
        }
    }

    /**
     * Sends a mime message with pre populated values
     * @param message
     */
    public void send(MimeMessage message){
        try {
            ((JavaMailSenderImpl) mailSender).send(message);
        } catch (MailException ex) {
            //log it and go on
            log.error(ex.getMessage());
        }
    }

    /**
     * Convenience method for sending messages with attachments.
     *
     * @param emailAddresses
     * @param resource
     * @param bodyText
     * @param subject
     * @param attachmentName
     * @throws MessagingException
     * @author Ben Gill
     */
    public void sendMessage(String[] emailAddresses,
        ClassPathResource resource, String bodyText, String subject,
        String attachmentName) throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailAddresses);
        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, resource);

        ((JavaMailSenderImpl) mailSender).send(message);
    }
}

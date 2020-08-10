package com.flotta.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends ServiceWithMsg {
    private final Log log = LogFactory.getLog(this.getClass());
    
    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;
	
    @Autowired
    public JavaMailSender emailSender;
    
    public static String SUCCESS_PASSWORD_CHANGE =
        "Dear $name$!\n\n"
        + "Password change has succeeded!";
    
    public static String FAILED_PASSWORD_CHANGE =
        "Dear $name$!\n\n"
        + "Password change has failed!";
    
    public static String ACTIVATION_AND_INITIAL_PASSWORD = 
        "Dear $name$!\n\n"
        + "Please activate your profile <a href=\"localhost:8080/activation/$key$\">here</a>!\n" 
        + "Your initial password: $initialPassword$";

	
	public boolean sendMessage(String email, String subject, String messageText) {
	  
	  MimeMessage message = emailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(messageText, true);
      emailSender.send(message);
    } catch (MessagingException e) {
      log.error("Multipart creation failed " + e);
    } catch (MailSendException e) {
      log.error("Failure when sending the message " + e);
      return false;
    }
    return true;
	}
	
	public String createMessageText(String template, String[] data) {
	  System.out.println(template);
	  System.out.println("-------------------");
	  StringBuilder sb = new StringBuilder();
	  
	  int from = 0;
	  for(int i = 0; i < data.length;) {
	    boolean copyFromTemplate = !template.isEmpty() && template.charAt(from) != '$';
	    int to = template.indexOf("$", from + 1);
	    if(to == -1) {
        break;
      }
	    if(copyFromTemplate) {
	      sb.append(template.substring(from, to));
	      from = to;
	    } else {
	      sb.append(data[i]);
	      i++;
	      from = to + 1;
	      if(to == template.length()) {
	        break;
	      }
	    }
	  }
	  if(from < template.length()) {
	    sb.append(template.substring(from, template.length()));
	  }
	  System.out.println(sb.toString());
	  return sb.toString();
	}
}

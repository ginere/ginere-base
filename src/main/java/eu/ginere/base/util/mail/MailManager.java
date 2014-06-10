package eu.ginere.base.util.mail;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.manager.AbstractManager;
import eu.ginere.base.util.properties.FileProperties;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;


public class MailManager extends AbstractManager {
	static Logger log = Logger.getLogger(MailManager.class);
	   
	private static final String PROPERTIES_FILE_NAME = "MailManager.properties";
	
	private static FileProperties fileManagerProperties = null;

	private static Session session = null;

	public static void init() throws DaoManagerException{
		if (fileManagerProperties == null) {
			fileManagerProperties = AbstractManager.getFileProperties(PROPERTIES_FILE_NAME);

			if (fileManagerProperties==null){
				throw new DaoManagerException("Properties object is null for file name:'"+PROPERTIES_FILE_NAME+"'");
			} else {
				session = initSession();
			}			
		}
	}

	private static Session initSession(){
		Properties props=new Properties();
		// Properties props = System.getProperties();
		props.put("mail.smtp.host", fileManagerProperties.getStringValue(MailManager.class,"smtpHost"));
		props.put("mail.smtp.user", fileManagerProperties.getStringValue(MailManager.class,"smtpUser"));
		props.put("mail.smtp.port", fileManagerProperties.getStringValue(MailManager.class,"smtpPort"));
		props.put("mail.smtp.auth", fileManagerProperties.getStringValue(MailManager.class,"smtpAuth"));
		props.put("mail.transport.protocol", fileManagerProperties.getStringValue(MailManager.class,"smtpProtocol"));
		
		Session session = Session.getDefaultInstance(props, new Authenticator());
		session.setDebug(fileManagerProperties.getBooleanValue(MailManager.class,"sessionDebug",true));

		log.debug("Session"+session);
		
		return session;
	}

	private static class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;
		
		public Authenticator() {
			String username = fileManagerProperties.getStringValue(MailManager.class,"smtpUser");
			String password = fileManagerProperties.getStringValue(MailManager.class,"smtpPassword");
			authentication = new PasswordAuthentication(username, password);
		}
		
		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
	
//	private static Address getFrom() throws AddressException{
////		return InternetAddress.getLocalAddress(session);
//		String from=fileManagerProperties.getStringValue(MailManager.class,"from");
//		
//		return new InternetAddress(from);
//	}

	private static String getSmtpUser(){
		return fileManagerProperties.getStringValue(MailManager.class,"smtpUser");
	}

	private static String getSmtpPassword(){
		return fileManagerProperties.getStringValue(MailManager.class,"smtpPassword");
	}
	
	public static void sendEmail(String fromString,String to,String body,String subject) throws DaoManagerException{
		if (fileManagerProperties == null){
			init();
		}
		try {
			Address from=new InternetAddress(fromString);
			Address to_array[] = InternetAddress.parse(to);
			
			Message msg = new MimeMessage(session);
			msg.setFrom(from);
			msg.setRecipients(Message.RecipientType.TO, to_array);
			msg.setSubject(subject);
			msg.setText(body);
	
			
			Transport transport = session.getTransport();
	
			transport.connect(getSmtpUser(), 
							  getSmtpPassword());
			transport.sendMessage(msg, msg.getAllRecipients());
		}catch (Exception e) {
			throw new DaoManagerException("To:"+to+"' Subject:'"+subject+ "' Body:'"+body+"'",e);
		}
	}
}	   

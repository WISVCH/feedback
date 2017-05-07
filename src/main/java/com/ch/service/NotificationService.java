package com.ch.service;

import com.ch.domain.AssociationFeedback;
import com.ch.domain.EducationFeedback;
import com.ch.domain.Feedback;
import com.ch.domain.Program;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@ConfigurationProperties(prefix = "mailNotifications")
public class NotificationService {
	@Getter @Setter
	private String from;
	@Getter @Setter
	private String toEducationCS;
	@Getter @Setter
	private String toEducationAM;
	@Getter @Setter
	private String toAssociation;

	private JavaMailSender javaMailSender;
	private MailContentBuilder mailContentBuilder;
	
	@Autowired
	public NotificationService(JavaMailSender javaMailSender, MailContentBuilder mailContentBuilder){
		this.javaMailSender = javaMailSender;
		this.mailContentBuilder = mailContentBuilder;
	}

	public void sendNotifications(Feedback feedback) {
		this.sendAdminNotification(feedback);
		this.sendSenderNotification(feedback);
	}
	
	private void sendSenderNotification(Feedback feedback) {
		if (!feedback.getSenderMail().equals("")) {
			try {
				MimeMessage mail = javaMailSender.createMimeMessage();
				mail.addRecipients(Message.RecipientType.TO, feedback.getSenderMail());
				mail.setFrom(from);
				mail.setSubject("[CH FeedbackTool] Copy of your feedback: " + feedback.getSubject());
				if (feedback instanceof EducationFeedback)
					mail.setContent(copyEducationFeedback((EducationFeedback) feedback), "text/html");
				else if (feedback instanceof AssociationFeedback)
					mail.setContent(copyAssociationFeedback((AssociationFeedback) feedback),  "text/html");
				javaMailSender.send(mail);
			}
			catch (MessagingException e) {

			}
		}
	}

	public String copyEducationFeedback(EducationFeedback educationFeedback) {
		return "<h1>Education Mail</h1>";
	}

	public String copyAssociationFeedback(AssociationFeedback associationFeedback) {
		return "INSERT ASSOCIATION COPY HERE";
	}

	private void  sendAdminNotification(Feedback feedback) {
		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			mail.setFrom(from);
			mail.setSubject("[CH FeedbackTool] New feedback available");
			if (feedback instanceof EducationFeedback) {
				Program program = ((EducationFeedback) feedback).getCourse().getProgram();
				if (program.equals(Program.BScTW) || program.equals(Program.MScAM)) {
					mail.addRecipients(Message.RecipientType.TO, toEducationAM);
				} else {
					mail.addRecipients(Message.RecipientType.TO, toEducationCS);
				}
				mail.setContent(mailContentBuilder.buildEducationAdmin((EducationFeedback) feedback), "text/html");
			} else if (feedback instanceof AssociationFeedback) {
				mail.addRecipients(Message.RecipientType.TO, toAssociation);
				mail.setContent(mailContentBuilder.buildAssociationAdmin((AssociationFeedback) feedback),"text/html");
			}
			javaMailSender.send(mail);
		}
		catch (MessagingException e) {

		}
	}
	
}
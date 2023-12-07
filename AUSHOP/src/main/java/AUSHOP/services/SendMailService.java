package AUSHOP.services;

import AUSHOP.Model.MailInfoModel;

import javax.mail.MessagingException;
import java.io.IOException;

public interface SendMailService {

	void run();

	void queue(String to, String subject, String body);

	void queue(MailInfoModel mail);

	void send(MailInfoModel mail) throws MessagingException, IOException;

}

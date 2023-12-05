package AUSHOP.services;

import java.io.IOException;

import javax.mail.MessagingException;



import AUSHOP.Model.MailInfoModel;

public interface ISendMailService {

	void run();

	void queue(String to, String subject, String body);

	void queue(MailInfoModel mail);

	void send(MailInfoModel mail) throws MessagingException, IOException;

}

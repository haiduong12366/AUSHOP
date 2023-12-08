package AUSHOP.services;

import AUSHOP.Model.MailInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SendMailServiceImpl implements SendMailService {
	@Autowired
	JavaMailSender sender;

	List<MailInfoModel> list = new ArrayList<>();

	@Override
	public void send(MailInfoModel mail) throws MessagingException, IOException {
		// Tạo message
		MimeMessage message = sender.createMimeMessage();
		// Sử dụng Helper để thiết lập các thông tin cần thiết cho message
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(mail.getFrom());

		if (mail.getAttachments() != null) {
			FileSystemResource file = new FileSystemResource(new File(mail.getAttachments()));
			helper.addAttachment(mail.getAttachments(), file);
		}

		// Gửi message đến SMTP server
		sender.send(message);

	}

	@Override
	public void queue(MailInfoModel mail) {
		System.out.println("sendmail,add");
		list.add(mail);
		System.out.println(list);
	}

	@Override
	public void queue(String to, String subject, String body) {
		System.out.println("sendmail,queue");
		queue(new MailInfoModel(to, subject, body));
	}

	@Override
	@Scheduled(fixedDelay = 5000)
	public void run() {
		System.out.println("tới run r");
		while (!list.isEmpty()) {
			System.out.println("chuẩn bị send và remove add");
			MailInfoModel mail = list.remove(0);
			try {
				System.out.println("sendmail1");
				this.send(mail);
				System.out.println("sendmail2");
			} catch (Exception e) {
				System.out.println("ko sendmail");
				e.printStackTrace();
			}
		}
	}
}
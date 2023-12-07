package AUSHOP.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailInfoModel {
	String from;
	String to;
	String subject;
	String body;
	String attachments;

	public MailInfoModel(String to, String subject, String body) {
		this.from = "AUSHOP <letanhuy19122003@gmail.com>";
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
}

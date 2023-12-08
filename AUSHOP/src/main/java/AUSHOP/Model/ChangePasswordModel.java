package AUSHOP.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordModel {
	@NotEmpty
	@Length(min = 6)
	private String newPasswd;
	@NotEmpty
	@Length(min = 6)
	private String confirmPasswd;
}

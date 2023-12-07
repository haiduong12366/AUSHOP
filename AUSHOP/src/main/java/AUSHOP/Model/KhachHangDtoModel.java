package AUSHOP.Model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangDtoModel {

	private Long id;
	@NotEmpty
	private String hoTen;
	@NotEmpty
	@Email
	private String email;
	@NotEmpty
	@Length(min = 6)
	private String passwd;
	@NotEmpty
	@Pattern(regexp = "^0\\d{9}$")
	private String sdt;
	@NotEmpty
	private String diaChi;
	private boolean gioiTinh;
	private String hinhanhKH;
	private Date registerDate;
	private Boolean status;
	private boolean isEdit = false;
	
}

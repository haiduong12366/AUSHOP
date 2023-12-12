package AUSHOP.Model;

import AUSHOP.entity.DanhGia;
import AUSHOP.entity.DonHang;
import AUSHOP.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

@Data

@AllArgsConstructor

@NoArgsConstructor

public class KhachHangModel{
	private int maKhachHang;
	private String username;
	private String  email;
	private String  hoTen;
	@Pattern(regexp = "^0\\d{9}$")
	private String  sdt;
	private String  diaChi;
	private boolean gioiTinh;
	private String  hinhanhKH;

	@Length(min = 6)
	private String passwd;
	public int tongChiTieu;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	private Date ngayDangKy;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	private boolean is_admin;
	
	private boolean isDelete = false;
	private boolean isEdit = false;

}

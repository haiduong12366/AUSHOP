package AUSHOP.Model;

import java.util.Date;
import java.util.Set;


import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import AUSHOP.entity.DanhGia;
import AUSHOP.entity.DonHang;
import AUSHOP.entity.KhachHang;
import AUSHOP.entity.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor

@NoArgsConstructor
public class KhachHangModel{
	private int maKhachHang;
	private String  email;
	private String  hoTen;
	@Pattern(regexp = "^0\\d{9}$")
	private String  sdt;
	private String  diaChi;
	private Boolean gioiTinh;
	private String  hinhanhKH;
	@Length(min = 6)
	private String  passwd;
	public int tongChiTieu;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	private Date ngayDangKy;
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	private Date last_login;
	private Boolean is_admin;
	private Set<DonHang> DonHang;
	private Set<DanhGia> DanhGia;
	private Set<UserRole> UserRole;
	private boolean isEdit;
	

}

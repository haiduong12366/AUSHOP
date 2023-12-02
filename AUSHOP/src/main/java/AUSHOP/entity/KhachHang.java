package AUSHOP.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KhachHang")
@Data

@AllArgsConstructor

@NoArgsConstructor

public class KhachHang implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int maKhachHang;
	
	@Column(name = "email" , columnDefinition = "varchar(50)")
	private String  email;
	
	@Column(name = "hoTen" , columnDefinition = "nvarchar(50)")
	private String  hoTen;
	
	@Column(name = "sdt" , columnDefinition = "varchar(10)")
	private String  sdt;
	
	@Column(name = "diaChi" , columnDefinition = "nvarchar(100)")
	private String  diaChi;
	
	@Column(columnDefinition = "bit")
	private Boolean gioiTinh;
	
	@Column(name = "hinhanhKH" , columnDefinition = "varchar(1000)")
	private String  hinhanhKH;
	
	@Column(name = "passwd" , columnDefinition = "varchar(32)")
	private String  passwd;
	
	public int tongChiTieu;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	@Column(columnDefinition = "Datetime")
    private Date ngayDangKy;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	@Column(columnDefinition = "Datetime")
    private Date last_login;
	
	@Column(name = "is_admin" , columnDefinition = "bit")
	private Boolean is_admin;
	
	@JsonIgnore
	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL)
	private Set<DonHang> DonHang;
	
	@JsonIgnore
	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL)
	private Set<DanhGia> DanhGia;
	
	@JsonIgnore
	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL)
	private Set<UserRole> UserRole;

}

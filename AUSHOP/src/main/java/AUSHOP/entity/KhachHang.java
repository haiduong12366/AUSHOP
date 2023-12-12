package AUSHOP.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KhachHang")

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
	private boolean gioiTinh;

	@Column(name = "hinhanhKH" , columnDefinition = "varchar(1000)")
	private String  hinhanhKH;


	@Column(name = "passwd" , columnDefinition = "varchar(1000)")
	private String  passwd;

	public int tongChiTieu;

	@Column(columnDefinition = "Date")
	private Date ngayDangKy;

//	@Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
//	@Column(columnDefinition = "Datetime")
//    private Date last_login;

	@Column(name = "is_admin" , columnDefinition = "bit")
	private Boolean is_admin;
	
	@Column(name = "isDelete" , columnDefinition = "bit")
	private boolean isDelete = false;

//	@JsonIgnore
//	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private Set<DonHang> DonHang;
//
//	@JsonIgnore
//	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private Set<DanhGia> DanhGia;
//
//	@JsonIgnore
//	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private Set<UserRole> UserRole;


}

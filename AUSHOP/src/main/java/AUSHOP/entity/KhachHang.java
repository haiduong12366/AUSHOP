package AUSHOP.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KhachHang")
public class KhachHang {

	@Id
	@Column(name = "maKhachHang")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "username" , columnDefinition = "varchar(50)")
	private String username;
	@Email
	private String email;
	@Column(length = 60, columnDefinition = "nvarchar(50) not null")
	private String hoTen;
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
	private boolean enabled;

	@JsonIgnore
	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL)
	private Set<DonHang> DonHang;

	@JsonIgnore
	@OneToMany(mappedBy = "maKhachHang", cascade = CascadeType.ALL)
	private Set<DanhGia> DanhGia;


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "UserRole",
			joinColumns = @JoinColumn(name = "maKhachHang"),
			inverseJoinColumns = @JoinColumn(name = "roleId")
	)
	private Set<AppRole> UserRole = new HashSet<>();

}

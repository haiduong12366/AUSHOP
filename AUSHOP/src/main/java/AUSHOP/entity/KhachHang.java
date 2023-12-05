package AUSHOP.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private String username;
	@Email
	private String email;
	@Column(length = 60, columnDefinition = "nvarchar(50) not null")
	private String hoTen;
	private String passwd;
	private boolean enabled;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<AppRole> UserRole = new HashSet<>();
	
}

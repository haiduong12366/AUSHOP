package AUSHOP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UserRole")

public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "roleId")
	private AppRole roleId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "maKhachHang")
	private KhachHang maKhachHang;

}

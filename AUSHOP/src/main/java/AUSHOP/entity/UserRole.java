package AUSHOP.entity;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserRole")
@Data

@AllArgsConstructor

@NoArgsConstructor
public class UserRole implements Serializable{

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

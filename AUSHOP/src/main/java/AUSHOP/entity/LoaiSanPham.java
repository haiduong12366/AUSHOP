package AUSHOP.entity;
import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LoaiSanPham")


public class LoaiSanPham implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int maLoaiSP;
	
	@Column(name = "tenLoaiSP" , columnDefinition = "nvarchar(100)")
	private String tenLoaiSP;

	@JsonIgnore
	@OneToMany(mappedBy = "maLoaiSP", cascade = CascadeType.ALL)
	private Set<NhaCungCap> NhaCungCap;
	
	
	@OneToMany(mappedBy = "maLoaiSP", cascade = CascadeType.ALL)
	private Set<SanPham> SanPham;
}

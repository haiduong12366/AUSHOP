package AUSHOP.entity;

import java.io.Serializable;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NhaCungCap")


public class NhaCungCap implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int  maNhaCC;
	
	@Column(name = "tenNhaCC" , columnDefinition = "nvarchar(100)")
	private String  tenNhaCC;
	
	@Column(name = "emailNhaCC" , columnDefinition = "varchar(100)")
	private String  emailNhaCC;
	
	@Column(name = "sdtNhaCC" , columnDefinition = "varchar(10)")
	private String  sdtNhaCC;
	
	@Column(name = "diaChiNhaCC" , columnDefinition = "nvarchar(100)")
	private String  diaChiNhaCC;
	
	
	@ManyToOne
	@JoinColumn(name = "maLoaiSP")
	private LoaiSanPham maLoaiSP;



}

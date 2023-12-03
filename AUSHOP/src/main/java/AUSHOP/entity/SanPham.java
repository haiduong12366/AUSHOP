package AUSHOP.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "SanPham")
@Data

@AllArgsConstructor

@NoArgsConstructor


public class SanPham implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int  maSP;
	
	@ManyToOne
	@JoinColumn(name = "maLoaiSP")
	private LoaiSanPham maLoaiSP;
	
	@ManyToOne
	@JoinColumn(name = "maNhaCC")
	private NhaCungCap maNhaCC;
	
	@Column(name = "tenSP" , columnDefinition = "nvarchar(100) not null")
	private String  tenSP;
	
	@Column(name = "moTa" , columnDefinition = "nvarchar(500)")
	private String  moTa;
	
	@Column(columnDefinition = "date")
	private Date ngaynhaphang;
	
	@Column(columnDefinition = "float")
	private float donGia;
	
	@Column(columnDefinition = "float")
	private float discount;
	
	@Column(name = "hinhAnh" , columnDefinition = "varchar(300)")
	private String  hinhAnh;	

	private int  slTonKho;
	
	@Column(name = "tinhTrang" , columnDefinition = "bit")
	private String  tinhTrang;	
	
	
	@OneToMany(mappedBy = "maSP", cascade = CascadeType.ALL)
	private Set<DanhGia> DanhGia;
	

	@OneToMany(mappedBy = "maSP", cascade = CascadeType.ALL)
	private Set<ChiTietDonHang> ChiTietDonHang;
	

	
}

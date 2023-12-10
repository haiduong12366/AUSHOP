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
@Table(name = "DonHang")
@Data

@AllArgsConstructor

@NoArgsConstructor

public class DonHang implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maDH;

	
    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang maKhachHang;

	@OneToOne
    @JoinColumn(name = "maPT")
    private ThanhToan maPT;

    @Column(columnDefinition = "Date")
    private Date ngayDatHang;
    
    @Column(name = "diaChiGiaoHang", columnDefinition = "nvarchar(200)")
    private String diaChiGiaoHang;
    
    @Column(name = "tinhTrang", columnDefinition = "int")
    private int tinhTrang;
    
    private double tongTien;

    @JsonIgnore
	@OneToMany(mappedBy = "maDH", cascade = CascadeType.ALL)
	private Set<ChiTietDonHang> ChiTietDonHang;
}

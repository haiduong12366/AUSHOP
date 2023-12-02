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

	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang maKhachHang;

    @ManyToOne
    @JoinColumn(name = "maPT")
    private ThanhToan thanhToan;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
    @Column(columnDefinition = "Datetime")
    private Date ngayDatHang;
    
    @Column(name = "diaChiGiaoHang", columnDefinition = "nvarchar(200)")
    private String diaChiGiaoHang;
    
    @Column(name = "tinhTrang", columnDefinition = "nvarchar(100)")
    private String tinhTrang;
    
    @Column(columnDefinition = "float")
    private Float tongTien;

    @JsonIgnore
	@OneToMany(mappedBy = "maDH", cascade = CascadeType.ALL)
	private Set<ChiTietDonHang> ChiTietDonHang;
}

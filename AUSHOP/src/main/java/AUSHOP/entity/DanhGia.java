package AUSHOP.entity;

import java.io.Serializable;
import java.util.Date;



import org.springframework.format.annotation.DateTimeFormat;



import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DanhGia")

@Data

@AllArgsConstructor

@NoArgsConstructor
public class DanhGia implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maDanhGia;
 
    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang maKhachHang;

  
    @ManyToOne
    @JoinColumn(name = "maSP")
    private SanPham maSP;

    @Column(name = "binhLuan", columnDefinition = "nvarchar(200)")
    private String binhLuan;

    @Column(name = "rating")
    private int rating;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
    @Column(columnDefinition = "Datetime")
    private Date ngayDanhGia;




}

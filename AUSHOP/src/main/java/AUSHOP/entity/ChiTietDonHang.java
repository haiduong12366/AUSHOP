package AUSHOP.entity;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietDonHang")

@Data

@AllArgsConstructor

@NoArgsConstructor
public class ChiTietDonHang implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maChiTietDH;

	@OneToOne
	@JoinColumn(name = "maDH")
	private DonHang maDH;
	
    @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "maSP")
	private SanPham maSP;
    
    private double donGia;

    private int soLuong;

    private double tong;
}


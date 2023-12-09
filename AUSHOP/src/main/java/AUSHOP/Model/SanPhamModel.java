package AUSHOP.Model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.NhaCungCap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamModel {
	private int  maSP;
	private String  hinhAnh;
	@NotEmpty
	@Length(min = 2)
	private String  tenSP;
	@NotEmpty
	private String  moTa;
	private Date ngaynhaphang;
	@NotNull
	@Min(value = 10000)
	private double donGia;
	@NotNull
	@Min(value = 0)
	@Max(value = 1)
	private double discount;
		
	@NotNull
	@Min(value = 1)
	private int  slTonKho;
	private boolean tinhTrang;	
	
	private int maLoaiSP;
	
	private int maNhaCC;
	private boolean isEdit = false; 
}

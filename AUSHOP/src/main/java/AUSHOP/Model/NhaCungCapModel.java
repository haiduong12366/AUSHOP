package AUSHOP.Model;


import AUSHOP.entity.LoaiSanPham;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor

@NoArgsConstructor
public class NhaCungCapModel {
	private int  maNhaCC;

	private String  tenNhaCC;
	
	private String  emailNhaCC;
	
	private String  sdtNhaCC;
	
	private String  diaChiNhaCC;
	private boolean isDelete = false;
	private boolean isEdit;
}

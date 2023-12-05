package AUSHOP.Model;

import org.hibernate.validator.constraints.Length;

import AUSHOP.entity.SanPham;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoaiSanPhamModel {

	private int maLoaiSP;
	@NotEmpty
	private String tenLoaiSP;
	private Set<SanPham> SanPham;

	private boolean isEdit = false; 
}

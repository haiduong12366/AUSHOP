package AUSHOP.Model;

import org.hibernate.validator.constraints.Length;



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
	@Length(min=5)
	private String tenLoaiSP;
	
	private Boolean isEdit = false; //true: update //false: insert
}

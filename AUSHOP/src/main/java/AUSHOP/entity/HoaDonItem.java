package AUSHOP.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private int  maSP;
	private String  tenSP;
	private int  soluong;
	private double donGia;
}

package AUSHOP.Model;

import java.io.Serializable;

import AUSHOP.Model.HangTonKhoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HangTonKhoModel implements Serializable{
	private Serializable group;
	private Double sum;
	private Long count;
}

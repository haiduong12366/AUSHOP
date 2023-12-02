package AUSHOP.entity;

import java.io.Serializable;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ThanhToan")
@Data

@AllArgsConstructor

@NoArgsConstructor

public class ThanhToan implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int maPT;
	
    @Column(name = "tenPT", columnDefinition = "nvarchar(100)")
	private String tenPT;
	
	@OneToOne
	@JoinColumn(name="maPT")
	private DonHang DonHang;
}

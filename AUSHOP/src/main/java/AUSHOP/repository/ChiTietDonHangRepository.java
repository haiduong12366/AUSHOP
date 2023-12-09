package AUSHOP.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import AUSHOP.entity.ChiTietDonHang;


public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer>{

	@Query(value = "select * from chi_tiet_don_hang where madh = ?", nativeQuery = true)
	List<ChiTietDonHang> findByMaDH(int id);
	
	List<ChiTietDonHang> findBymaSP(Integer id);
	
	@Query(value="delete from chi_tiet_don_hang where ma_chi_tietdh = ?",nativeQuery = true)
	List<ChiTietDonHang> deleteByMaCTTDH(int id);


}

package AUSHOP.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import AUSHOP.entity.ChiTietDonHang;


public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer>{

	@Query(value = "select * from chi_tiet_don_hang where madh = ?", nativeQuery = true)
	List<ChiTietDonHang> findByMaDH(int id);
}

package AUSHOP.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.DonHang;


@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

	@Query(value = "select * from don_hang where tinh_trang = ?", nativeQuery = true)
	Page<DonHang> findByStatus(int status, Pageable pageable);
	
	Page<DonHang> findByMaDH (int id, Pageable pageable);
}

package AUSHOP.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.DonHang;


@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

	@Query(value = "select * from don_hang where tinh_trang = ?", nativeQuery = true)
	Page<DonHang> findByStatus(int status, Pageable pageable);
	
	
	@Query(value="select * from don_hang where ma_khach_hang = ?1 and tinh_trang = ?2", nativeQuery = true)
	Page<DonHang> findByMaKhachHang (int id, int trangthai, Pageable pageble);

	@Query(value = "select top 1 * from don_hang where ma_khach_hang = ?", nativeQuery = true)
	Optional<DonHang> findByMaKhachHang(int maKhachHang);


	Page<DonHang> findByMaDH(Integer valueOf, PageRequest of);

}

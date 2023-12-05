package AUSHOP.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import AUSHOP.entity.SanPham;

public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {

	Page<SanPham> findBytenSPContaining(String tenSP, Pageable pageable);

	@Query(value = "select * from san_pham where ma_nhacc = ?", nativeQuery = true)
	Page<SanPham> findSanPhamByMaNhaCCContaining(Long brand, Pageable pageable);

	@Query(value = "select * from san_pham where tensp like %?% AND ma_loaisp = ?", nativeQuery = true)
	Page<SanPham> findByTenSPAndMaLoaiSPContaining(String tenSP, Long maloaisp, Pageable pageable);
	
	@Query(value = "select * from san_pham where tensp like %?% and ma_nhacc = ? and ma_loaisp = ? ", nativeQuery = true)
	Page<SanPham> findSanPhamByTenSPAndMaNhaCCAndMaLoaiSPContaining(String tenSP, Long ma_nhacc, Long ma_loaisp, Pageable pageable);

	@Query(value = "select * from san_pham where tensp like %?% and ma_nhacc = ?", nativeQuery = true)
	Page<SanPham> findSanPhamByTenSPAndMaNhaCCContaining(String tenSP, Long ma_nhacc, Pageable pageable);

}

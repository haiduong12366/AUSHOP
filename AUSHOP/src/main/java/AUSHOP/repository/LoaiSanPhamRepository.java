package AUSHOP.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.NhaCungCap;

@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham,Integer> {

	@Query(value = "select * from loai_san_pham where is_delete = 0 and ten_loaisp like %?%", nativeQuery = true)
	Page<LoaiSanPham> findBytenLoaiSPContaining(String tenLoaiSP, Pageable pageable);

	@Modifying
	@Transactional
	@Query(value = "update loai_san_pham set is_delete = 1 where ma_loaisp = ?", nativeQuery = true)
	void isDelete(int loaiSanPham);

	@Query(value = "select * from loai_san_pham where is_delete = 0", nativeQuery = true)
	Page<LoaiSanPham> findAllisDeleteContainning(Pageable pageable);

	
	// khai báo thêm những hàm không có trong repository
	
}

package AUSHOP.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.NhaCungCap;

@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham,Integer> {

	Page<LoaiSanPham> findBytenLoaiSPContaining(String tenLoaiSP, Pageable pageable);

	
	// khai báo thêm những hàm không có trong repository
	
}

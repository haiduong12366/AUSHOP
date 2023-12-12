package AUSHOP.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.NhaCungCap;


@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap,Integer> {

	@Query(value = "select * from nha_cung_cap where is_delete = 0 and ten_nhacc like %?%", nativeQuery = true)
	Page<NhaCungCap> findBytenNhaCCContaining(String tenNhaCC, Pageable pageable);

	@Modifying
	@Transactional
	@Query(value = "update nha_cung_cap set is_delete = 1 where ma_nhacc = ?", nativeQuery = true)
	void isDelete(int maNhaCC);

	@Query(value = "select * from nha_cung_cap where is_delete = 0", nativeQuery = true)
	Page<NhaCungCap> findWithIsDelete(Pageable pageable);
	
	// khai báo thêm những hàm không có trong repository
	
}
package AUSHOP.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import AUSHOP.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {

	@Query(value = "select * from khach_hang where is_delete = 0 and ho_ten like %?%", nativeQuery = true)
	public Page<KhachHang> findByHoTenContaining(String HoTen, Pageable pageable);

	@Query(value = "select * from khach_hang where is_delete = 0 and email = ?", nativeQuery = true)
	public Optional<KhachHang> findByEmail(String trim);

	@Modifying
	@Transactional
	@Query(value = "update khach_hang set is_delete = 1 where ma_khach_hang = ?", nativeQuery = true)
	public void isDelete(int maKhachHang);

	
	@Query(value = "select * from khach_hang where is_delete = 0", nativeQuery = true)
	public Page<KhachHang> findAllwithIsdelete(int i, Pageable pageable);


	
}



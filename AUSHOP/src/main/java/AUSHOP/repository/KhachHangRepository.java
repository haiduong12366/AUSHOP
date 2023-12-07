package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import AUSHOP.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {

	Optional<KhachHang> findByEmail(String name);
	
	@Query(value = "select top 1 * from khach_hang where email = ? and enabled = 1 ", nativeQuery = true)
	Optional<KhachHang> findInfoByEmail(String email);
}



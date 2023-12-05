package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import AUSHOP.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {

	Optional<KhachHang> findByEmail(String name);
	
}



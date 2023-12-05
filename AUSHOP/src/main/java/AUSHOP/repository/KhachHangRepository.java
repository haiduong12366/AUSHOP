package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.KhachHang;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {

	Optional<KhachHang> findByEmail(String name);
	
}



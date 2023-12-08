package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import AUSHOP.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {


	public Page<KhachHang> findByHoTenContaining(String HoTen, Pageable pageable);

	public Optional<KhachHang> findByEmail(String trim);


	
}



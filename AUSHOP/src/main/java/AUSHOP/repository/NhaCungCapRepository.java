package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.NhaCungCap;
import AUSHOP.entity.SanPham;


@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap,Integer> {


	Page<NhaCungCap> findBytenNhaCCContaining(String tenNhaCC, Pageable pageable);

	
}
package AUSHOP.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.NhaCungCap;


@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap,Integer> {


	Page<NhaCungCap> findBytenNhaCCContaining(String tenNhaCC, Pageable pageable);
	
	// khai báo thêm những hàm không có trong repository
	
}
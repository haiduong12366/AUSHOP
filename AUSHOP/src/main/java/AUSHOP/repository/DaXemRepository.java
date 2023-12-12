package AUSHOP.repository;

import AUSHOP.entity.DaXem;
import AUSHOP.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DaXemRepository extends JpaRepository <DaXem, Integer> {
	
}

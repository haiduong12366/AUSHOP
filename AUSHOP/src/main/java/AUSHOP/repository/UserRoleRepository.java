package AUSHOP.repository;

import AUSHOP.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{

	@Query(value = "select top 1 * from user_role where ma_khach_hang = ?", nativeQuery = true)
	Optional<UserRole> findByMaKhachHang(int maKhachHang);


}

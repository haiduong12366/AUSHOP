package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.KhachHang;

@Repository
public interface UserRepository extends JpaRepository<KhachHang, Long>{

	@Query("SELECT u FROM KhachHang u WHERE u.email = :email")
	public KhachHang getUserByUsername(@Param("email") String email);
	
	Optional<KhachHang> findByEmail(String email);
	Optional<KhachHang> findByUsernameOrEmail(String username, String email);
	Optional<KhachHang> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);

	public Page<KhachHang> findByHoTenContaining(String HoTen, Pageable pageable);
}

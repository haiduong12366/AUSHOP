package AUSHOP.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.AppRole;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, Integer> {

}

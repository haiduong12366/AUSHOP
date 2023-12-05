package AUSHOP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AUSHOP.entity.ChiTietDonHang;


@Repository
public interface ChiTietDonHangRepository  extends JpaRepository<ChiTietDonHang,Integer>{


	List<ChiTietDonHang> findBymaSP(Integer id);

}

package AUSHOP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import AUSHOP.entity.ChiTietDonHang;

public interface ChiTietDonHangRepository  extends JpaRepository<ChiTietDonHang,Integer>{


	List<ChiTietDonHang> findBymaSP(Integer id);

}

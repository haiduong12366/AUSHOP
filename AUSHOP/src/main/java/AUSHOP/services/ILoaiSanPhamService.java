package AUSHOP.services;

import java.util.List;
import java.util.Optional;

import AUSHOP.entity.LoaiSanPham;

public interface ILoaiSanPhamService {

	void deleteById(Integer id);

	long count();

	Optional<LoaiSanPham> findById(Integer id);

	List<LoaiSanPham> findAllById(Iterable<Integer> ids);

	List<LoaiSanPham> findAll();

	<S extends LoaiSanPham> S save(S entity);

}

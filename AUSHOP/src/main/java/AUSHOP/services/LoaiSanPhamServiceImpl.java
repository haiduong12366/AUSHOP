package AUSHOP.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AUSHOP.entity.LoaiSanPham;
import AUSHOP.repository.LoaiSanPhamRepository;

@Service
public class LoaiSanPhamServiceImpl implements ILoaiSanPhamService{
	
	@Autowired
	LoaiSanPhamRepository loaispRepository;

	public LoaiSanPhamServiceImpl(LoaiSanPhamRepository loaispRepository) {
		this.loaispRepository = loaispRepository;
	}

	@Override
	public <S extends LoaiSanPham> S save(S entity) {
		return loaispRepository.save(entity);
	}

	@Override
	public List<LoaiSanPham> findAll() {
		return loaispRepository.findAll();
	}

	@Override
	public List<LoaiSanPham> findAllById(Iterable<Integer> ids) {
		return loaispRepository.findAllById(ids);
	}

	@Override
	public Optional<LoaiSanPham> findById(Integer id) {
		return loaispRepository.findById(id);
	}

	@Override
	public long count() {
		return loaispRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		loaispRepository.deleteById(id);
	}
	
	
	
}

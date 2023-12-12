package AUSHOP.services;

import AUSHOP.entity.DaXem;
import AUSHOP.repository.DaXemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaXemServiceImpl implements IDaXemService {

	@Autowired
	DaXemRepository daXemRepository;

	@Override
	public void add(DaXem seen) {
		if (daXemRepository != null) {
		daXemRepository.save(seen);
		}

	}

}

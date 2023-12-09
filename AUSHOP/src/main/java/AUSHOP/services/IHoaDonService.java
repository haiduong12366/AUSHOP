package AUSHOP.services;

import java.util.Collection;

import AUSHOP.entity.HoaDonItem;

public interface IHoaDonService {

	void update(Integer id, int quantity);
	void remove(Integer id);
	int getCount();
	Collection<HoaDonItem> getHoaDonItems();
	double getAmount();
}

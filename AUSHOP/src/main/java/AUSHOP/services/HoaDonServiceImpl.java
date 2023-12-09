package AUSHOP.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import AUSHOP.entity.HoaDonItem;

@Service
@SessionScope
public class HoaDonServiceImpl implements IHoaDonService{

	private Map<Integer,HoaDonItem> map = new HashMap<Integer,HoaDonItem>();
	
	@Override
	public void remove(Integer id) {
		map.remove(id);
	}
	
	@Override
	public void update (Integer id, int soluong) {
		HoaDonItem item = map.get(id);
		item.setSoluong(soluong);
		if (item.getSoluong()<=0) {
			map.remove(id);
		}
	}
	
	@Override
	public int getCount() {
		if(map.isEmpty()) {
			return 0;
		}
		return map.values().size();
	}
	
	@Override
	public Collection<HoaDonItem> getHoaDonItems() {
		return map.values();
	}
	
	@Override
	public double getAmount() {
		double amount = 0;
		Set<Integer> listKey = map.keySet();
		for(Integer key : listKey) {
			amount += map.get(key).getDonGia() * map.get(key).getSoluong();
		}
		
		return amount;
	}
	
}

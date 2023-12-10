package AUSHOP.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import AUSHOP.Model.CartItem;




@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService{
    private Map<Integer, CartItem> map = new HashMap<Integer, CartItem>();

    @Override
    public void add(CartItem item) {
        CartItem existedItem = map.get(item.getProductId());
        if (existedItem != null) {
            existedItem.setQuantity(item.getQuantity()+existedItem.getQuantity());
        } else {
            map.put(item.getProductId(), item);
        }
    }

    @Override
    public void remove(Integer id) {
        map.remove(id);
    }

    @Override
    public Collection<CartItem> getCartItems() {
        return map.values();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void update(Integer id, int quantity) {
        CartItem item = map.get(id);
        item.setQuantity(quantity);
        if(item.getQuantity()<=0) {
            map.remove(id);
        }
    }

    @Override
    public double getAmount() {
        double amount = 0;
        Set<Integer> listKey = map.keySet();
        for(Integer key : listKey) {
            amount += map.get(key).getPrice() * map.get(key).getQuantity();
        }

        return amount;
    }

    @Override
    public int getCount() {
        if(map.isEmpty()) {
            return 0;
        }
        return map.values().size();
    }

    @Override
    public int getMountById(int item) {
        CartItem existedItem = map.get(item);
        if (existedItem != null) {
        	return existedItem.getQuantity();
        } else {
            return -1;
        }
    }


}

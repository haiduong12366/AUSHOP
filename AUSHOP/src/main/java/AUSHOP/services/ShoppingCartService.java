package AUSHOP.services;

import java.util.Collection;

import AUSHOP.Model.CartItem;


public interface ShoppingCartService{

	int getCount();

	double getAmount();

	void clear();

	Collection<CartItem> getCartItems();

	void remove(Integer id);

	void add(CartItem item);

	public void update(Integer id, int quantity);

	int getMountById(int i);

}

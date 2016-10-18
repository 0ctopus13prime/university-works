package fakeecommerce.dao;

import fakeecommerce.domain.Order;

import java.util.List;

public interface OrderDao {
	long nextOrderId();
	void insertOrder(Order order);
	List<Order> getOrdersByproductId(String productId);
	Order getOrderById(long orderId);
}

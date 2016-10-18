package fakeecommerce.dao.impl;

import fakeecommerce.dao.LogisticsDao;
import fakeecommerce.dao.OrderDao;
import fakeecommerce.dao.ProductDao;
import fakeecommerce.domain.Order;
import fakeecommerce.domain.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Just for Test.
 * Make fake order, lookup, logistics process
 */
public final class TestOrderDaoImpl implements ProductDao, LogisticsDao, OrderDao {

	private static long orderId = 0;

	@Override
	public void updateStatus(long orderId, Order.DeliverStatus deliverStatus) {
		System.out.println("update status order-id : " + orderId + " status : " + deliverStatus);
	}

	@Override
	public void insertOrder(Order order) {
		System.out.println("insert order : " + order);
	}

	@Override public long nextOrderId() {
		return orderId;
	}

	@Override
	public List<Order> getOrdersByproductId(String productId) {
		return makeFakeOrderList();
	}

	private List<Order> makeFakeOrderList() {
		List<Order> orders = new ArrayList<Order>();

		for(int i = 0 ; i<10 ; i++ ) {
			orders.add(makeFakeOrder(i));
		}

		return orders;
	}

	public static Order makeFakeOrder(int hint) {
		Product product = new Product();
		product.setCompany("company" + hint);
		product.setCost(10000*hint);
		product.setDeliverCost(100*hint);
		product.setDescription("description" + hint);
		product.setMakeDate(new Date());
		product.setName("name" + hint);

		Order order = new Order();
		order.addProduct(product);

		order.setproductId("product_id" + hint);
		order.setOrderId(orderId);
		order.setOrdererName("orderer_name" + orderId);
		order.setDeliverStatus(Order.DeliverStatus.NONE);
		orderId++;
		return order;
	}

	@Override
	public Order getOrderById(long orderId) {
		return makeFakeOrder(0);
	}
}

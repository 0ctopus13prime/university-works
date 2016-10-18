package fakeecommerce.dao;

import fakeecommerce.domain.Order;

public interface LogisticsDao {
	void updateStatus(long orderId, Order.DeliverStatus deliverStatus);
}

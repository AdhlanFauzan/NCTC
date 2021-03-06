package com.netcracker.facade.local_int;



import com.netcracker.classes.Point;
import com.netcracker.entity.OrderEntity;
import com.netcracker.entity.OrderStateEntity;

import javax.ejb.Local;
import java.util.List;

@Local
public interface Order {
	void create(OrderEntity entity);

	OrderEntity read(Object id);

	void update(OrderEntity entity);

	void delete(OrderEntity entity);

	List<OrderEntity> findAll();

	List<OrderEntity> findRange(int[] range);

	int count();

	List<OrderEntity> getOrdersByStateAndCustomerUuid(OrderStateEntity orderStateEntity, String customerUuid);

	List<OrderEntity> getOrdersByStateAndDriverUuid(OrderStateEntity orderStateEntity, String driverUuid);

	List<OrderEntity> getOrdersByState(OrderStateEntity orderStateEntity);

	List<Point> getFirstAndLastPoints(OrderEntity orderEntity);

	OrderEntity getByUUIDAndId(String orderId, String uuid);

	List<OrderEntity> sortByDateAndUUIDAndState(String uuid, OrderStateEntity orderStateEntity);

	List<OrderEntity> sortByPriceAndUUIDAndState(String uuid, OrderStateEntity orderStateEntity);

	List<OrderEntity> sortByLengthAndUUIDAndState(String uuid, OrderStateEntity orderStateEntity);

	OrderEntity getByDriverUUIDAndID(String orderId, String driverUuid);

	OrderEntity getOrderByPublicToken(String publicToken);
}

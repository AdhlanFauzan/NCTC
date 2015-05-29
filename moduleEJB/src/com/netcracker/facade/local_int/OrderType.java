package com.netcracker.facade.local_int;



import com.netcracker.entity.OrderTypeEntity;

import javax.ejb.Local;
import java.util.List;

@Local
public interface OrderType {
	void create(OrderTypeEntity entity);

	OrderTypeEntity read(Object id);

	void update(OrderTypeEntity entity);

	void delete(OrderTypeEntity entity);

	List<OrderTypeEntity> findAll();

	List<OrderTypeEntity> findRange(int[] range);

	int count();

	OrderTypeEntity findByName(String name);
}

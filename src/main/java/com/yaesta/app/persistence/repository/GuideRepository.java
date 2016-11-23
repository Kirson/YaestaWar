package com.yaesta.app.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yaesta.app.persistence.entity.Guide;
import com.yaesta.app.persistence.entity.Order;

public interface GuideRepository extends JpaRepository<Guide, Long>{

	public Guide findByVitexId(String vitexId);
	public List<Guide> findByDeliveryName(String deliveryName);
	public List<Guide> findByStatus(String status);
	public List<Guide> findByOrder(Order order);
	@Query("Select gui from Guide gui " +
	         "where gui.createDate between ?1 and ?2")
	public List<Guide> findByCreateDateBetween(Date start, Date finish);
	@Query("Select gui from Guide gui " +
	         "where gui.orderDate between ?1 and ?2")
	public List<Guide> findByOrderDateBetween(Date start, Date finish);

	public List<Guide> findByVitexDispatcherId(String vitexDispatcherId);
	public List<Guide> findByOrderVitexId(String orderVitexId);
	
	public List<Guide> findByStatusAndStrDeliveryDate(String status, String strDeliveryDate);
	public List<Guide> findByStatusAndStrDeliveryDateAndDeliveryName(String status, String strDeliveryDate, String deliveryName);
	
	@Query("Select gui from Guide gui " +
	         "where gui.orderDate between ?1 and ?2 and gui.status = ?3")
	public List<Guide> findByOrderDateBetweenAndStatus(Date start, Date finish, String status);
	
	public List<Guide> findByGuideNumber(String guideNumber);
	
	@Query("Select gui from Guide gui " +
	         "where gui.deliveryName = ?1 and gui.status =?2 and gui.paymentMethod = ?3")
	public List<Guide> findByDeliveryNameStatusPaymentMethod(String deliveryName, String status, String paymentMethod);
}

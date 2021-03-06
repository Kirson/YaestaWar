package com.yaesta.app.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yaesta.app.persistence.entity.Supplier;
import com.yaesta.app.persistence.entity.SupplierDeliveryCalendar;

public interface SupplierDeliveryCalendarRepository extends JpaRepository<SupplierDeliveryCalendar, Long>{

	public List<SupplierDeliveryCalendar> findBySupplier(Supplier supplier);
}

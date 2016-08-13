package com.yaesta.app.persistence.vo;

import java.io.Serializable;
import java.util.List;

import com.yaesta.app.persistence.entity.Supplier;
import com.yaesta.app.persistence.entity.SupplierDeliveryCalendar;

public class SupplierVO implements Serializable{

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -5790748112162698017L;
	
	private Supplier supplier;
	private List<SupplierDeliveryCalendar> deliveryCalendar;

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public List<SupplierDeliveryCalendar> getDeliveryCalendar() {
		return deliveryCalendar;
	}

	public void setDeliveryCalendar(List<SupplierDeliveryCalendar> deliveryCalendar) {
		this.deliveryCalendar = deliveryCalendar;
	}
	
	
	
	
}

package com.yaesta.app.persistence.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yaesta.app.persistence.entity.Supplier;
import com.yaesta.app.persistence.service.AddressService;
import com.yaesta.app.persistence.service.SupplierService;
import com.yaesta.app.persistence.vo.SupplierVO;

@RestController
@RequestMapping(value = "/supplier")
public class SupplierController {

	@Autowired
	SupplierService supplierService;
	
	@Autowired
	AddressService addressService;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public  ResponseEntity<List<Supplier>> getAll(){
		
		List<Supplier> found = supplierService.getSuppliers();
	
	    if(found!=null && !found.isEmpty()){
	    	return new ResponseEntity<List<Supplier>>(found, HttpStatus.OK);
	    }else{
	    	return new ResponseEntity<List<Supplier>>(new ArrayList<Supplier>(),HttpStatus.OK);
	    }
	}
	
	@RequestMapping(value = "/updateInfo", method = RequestMethod.GET)
	public ResponseEntity<String> updateInfo(){
		
		String response= supplierService.updateSupplierFromAddress();
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateSupplier", method = RequestMethod.GET)
	public ResponseEntity<String> updateSupplier(){
		
		String response= supplierService.updateSupplier();
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Supplier> create(@RequestBody SupplierVO supplierVO){
		
		Supplier response = supplierService.save(supplierVO.getSupplier(), null);
		
		return new ResponseEntity<Supplier>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<Supplier> update(@RequestBody SupplierVO supplierVO){
		
		Supplier response = supplierService.save(supplierVO.getSupplier(), null);
		
		return new ResponseEntity<Supplier>(response,HttpStatus.OK);
	}

}

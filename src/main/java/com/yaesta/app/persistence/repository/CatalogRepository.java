package com.yaesta.app.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yaesta.app.persistence.entity.Catalog;


public interface CatalogRepository extends JpaRepository<Catalog, Long> {

	public Catalog findByVitexId(String vitexId);
	public List<Catalog> findByMainCatalog(Catalog mainCatalog);
	public List<Catalog> findByMainCatalogIsNull();
	public List<Catalog> findByNemonic(String nemonic);
}

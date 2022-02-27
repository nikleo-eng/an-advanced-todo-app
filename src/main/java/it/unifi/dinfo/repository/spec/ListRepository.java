package it.unifi.dinfo.repository.spec;

import java.util.Set;

import it.unifi.dinfo.model.List;

public interface ListRepository {

	Set<List> findAllByUserId(Long userId);
	
	List findByNameAndUserId(String name, Long userId);
	
	List create(List list);
	
	List save(List list);
	
	void delete(List list);
	
}

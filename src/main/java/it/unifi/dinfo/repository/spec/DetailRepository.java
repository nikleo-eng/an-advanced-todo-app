package it.unifi.dinfo.repository.spec;

import java.util.Set;

import it.unifi.dinfo.model.Detail;

public interface DetailRepository {

	Set<Detail> findAllByListId(Long listId);
	
	Detail findByTodoAndListId(String todo, Long listId);
	
	Detail create(Detail detail);
	
	Detail save(Detail detail);
	
	void delete(Detail detail);
	
}

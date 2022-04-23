package it.unifi.dinfo.repository.spec;

import java.util.Set;

import it.unifi.dinfo.model.Log;

public interface LogRepository {

	Log create(Log log);
	
	Log save(Log log);
	
	Log findLastBeforeIdAndByUserId(Long id, Long userId);
	
	/* Only for tests */
	void delete(Log log);
	
	/* Only for tests */
	Set<Log> findAllByUserId(Long userId);
	
}

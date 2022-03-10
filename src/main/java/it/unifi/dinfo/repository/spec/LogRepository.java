package it.unifi.dinfo.repository.spec;

import it.unifi.dinfo.model.Log;

public interface LogRepository {

	Log create(Log log);
	
	Log save(Log log);
	
	Log findLastBeforeIdAndByUserId(Long id, Long userId);
	
}

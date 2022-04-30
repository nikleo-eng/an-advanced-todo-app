package it.unifi.dinfo.repository.spec;

import it.unifi.dinfo.model.User;

public interface UserRepository {
	
	User findByEmail(String email);
	
	User create(User user);
	
}

package it.unifi.dinfo.repository.spec;

import java.util.Set;

import it.unifi.dinfo.model.User;

public interface UserRepository {
	
	/* Only for tests */
	Set<User> findAll();

	User findByEmail(String email);
	
	User create(User user);
	
	/* Only for tests */
	void delete(User user);
	
}

package it.unifi.dinfo.repository.mysql.spec;

import javax.persistence.NoResultException;

import org.hibernate.Session;

import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.spec.base.BaseMySqlRepository;
import it.unifi.dinfo.repository.spec.UserRepository;

public class UserMySqlRepository extends BaseMySqlRepository implements UserRepository {

	public UserMySqlRepository(Session hibernateSession) {
		super(hibernateSession);
	}

	@Override
	public User findByEmail(String email) {
		try {
			return getHibernateSession()
					.createQuery("select u from User u where u.email = ?0", User.class)
					.setParameter(0, email)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	public User create(User user) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().persist(user);
		getHibernateSession().getTransaction().commit();
		
		return user;
	}
	
	/* Only for tests */
	public void delete(User user) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession()
			.createQuery("delete from User u where u.id = ?0")
			.setParameter(0, user.getId())
			.executeUpdate();
		getHibernateSession().getTransaction().commit();
	}

}

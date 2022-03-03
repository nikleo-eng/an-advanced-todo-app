package it.unifi.dinfo.repository.mysql.spec.base;

import org.hibernate.Session;

public abstract class BaseMySqlRepository {

	private Session hibernateSession;

	protected BaseMySqlRepository(Session hibernateSession) {
		this.hibernateSession = hibernateSession;
	}

	protected Session getHibernateSession() {
		return hibernateSession;
	}
	
}

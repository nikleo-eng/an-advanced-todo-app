package it.unifi.dinfo.repository.mysql.spec;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;

import com.google.inject.Inject;

import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.repository.mysql.spec.base.BaseMySqlRepository;
import it.unifi.dinfo.repository.spec.LogRepository;

public class LogMySqlRepository extends BaseMySqlRepository implements LogRepository {

	@Inject
	public LogMySqlRepository(Session hibernateSession) {
		super(hibernateSession);
	}

	@Override
	public Log findLastBeforeIdAndByUserId(Long id, Long userId) {
		return getHibernateSession()
				.createQuery("select l from Log l where l.user.id = ?0 and l.id < ?1 order by l.id desc", 
						Log.class)
				.setParameter(0, userId).setParameter(1, id)
				.getResultStream().findFirst().orElse(null);
	}

	@Override
	public Log create(Log log) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().persist(log);
		getHibernateSession().getTransaction().commit();
		
		return log;
	}

	@Override
	public Log save(Log log) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().update(log);
		getHibernateSession().getTransaction().commit();
		
		return log;
	}
	
	@Override
	public Set<Log> findAllByUserId(Long userId) {
		return getHibernateSession()
				.createQuery("select l from Log l where l.user.id = ?0 order by l.id desc", Log.class)
				.setParameter(0, userId)
				.getResultStream().collect(Collectors.toCollection(LinkedHashSet::new));
	}

}

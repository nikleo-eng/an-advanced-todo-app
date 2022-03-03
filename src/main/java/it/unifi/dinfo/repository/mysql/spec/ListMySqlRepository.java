package it.unifi.dinfo.repository.mysql.spec;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.hibernate.Session;

import it.unifi.dinfo.model.List;
import it.unifi.dinfo.repository.mysql.spec.base.BaseMySqlRepository;
import it.unifi.dinfo.repository.spec.ListRepository;

public class ListMySqlRepository extends BaseMySqlRepository implements ListRepository {

	public ListMySqlRepository(Session hibernateSession) {
		super(hibernateSession);
	}

	@Override
	public Set<List> findAllByUserId(Long userId) {
		return getHibernateSession()
				.createQuery("select l from List l where l.user.id = ?0 order by l.id", List.class)
				.setParameter(0, userId)
				.getResultStream().collect(Collectors.toSet());
	}

	@Override
	public List create(List list) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().persist(list);
		getHibernateSession().getTransaction().commit();
		
		return list;
	}

	@Override
	public List save(List list) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().update(list);
		getHibernateSession().getTransaction().commit();
		
		return list;
	}

	@Override
	public void delete(List list) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession()
			.createQuery("delete from List l where l.id = ?0")
			.setParameter(0, list.getId())
			.executeUpdate();
		getHibernateSession().getTransaction().commit();
	}

	@Override
	public List findByNameAndUserId(String name, Long userId) {
		try {
			return getHibernateSession()
					.createQuery("select l from List l where l.user.id = ?0 and l.name = ?1", 
							List.class)
					.setParameter(0, userId).setParameter(1, name)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}

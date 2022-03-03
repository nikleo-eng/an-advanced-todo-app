package it.unifi.dinfo.repository.mysql.spec;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.hibernate.Session;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.repository.mysql.spec.base.BaseMySqlRepository;
import it.unifi.dinfo.repository.spec.DetailRepository;

public class DetailMySqlRepository extends BaseMySqlRepository implements DetailRepository {

	public DetailMySqlRepository(Session hibernateSession) {
		super(hibernateSession);
	}

	@Override
	public Set<Detail> findAllByListId(Long listId) {
		return getHibernateSession()
				.createQuery("select d from Detail d where d.list.id = ?0 order by d.id", Detail.class)
				.setParameter(0, listId)
				.getResultStream().collect(Collectors.toSet());
	}

	@Override
	public Detail create(Detail detail) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().persist(detail);
		getHibernateSession().getTransaction().commit();
		
		return detail;
	}

	@Override
	public Detail save(Detail detail) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession().update(detail);
		getHibernateSession().getTransaction().commit();
		
		return detail;
	}

	@Override
	public void delete(Detail detail) {
		getHibernateSession().getTransaction().begin();
		getHibernateSession()
			.createQuery("delete from Detail d where d.id = ?0")
			.setParameter(0, detail.getId())
			.executeUpdate();
		getHibernateSession().getTransaction().commit();
	}

	@Override
	public Detail findByTodoAndListId(String todo, Long listId) {
		try {
			return getHibernateSession()
					.createQuery("select d from Detail d where d.list.id = ?0 and d.todo = ?1", 
							Detail.class)
					.setParameter(0, listId).setParameter(1, todo)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}

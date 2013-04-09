package com.ineatconseil.yougo.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ineatconseil.yougo.dao.GenericDao;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * 
 * @param <T>
 *            The type variable
 * @param <PK>
 *            The primary key for that type
 */
@Transactional
public class GenericDaoHibernate<T, PK extends Serializable> implements
		GenericDao<T, PK> {

	/**
	 * Logger SLF4J.
	 */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private Class<T> persistentClass;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;

	/**
	 * Constructor that takes in a class to see which type of entity to persist.
	 * Use this constructor when sub-classing.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 */
	public GenericDaoHibernate(final Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * Constructor that takes in a class and sessionFactory for easy creation of
	 * DAO.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 * @param sessionFactory
	 *            the pre-configured Hibernate SessionFactory
	 */
	public GenericDaoHibernate(final Class<T> persistentClass,
			SessionFactory sessionFactory) {
		this.persistentClass = persistentClass;
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	@Autowired
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return hibernateTemplate.loadAll(this.persistentClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<T> getAllDistinct() {
		Collection<T> result = new LinkedHashSet<T>(findAll());
		return new ArrayList<T>(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public T get(PK id) {
		T entity = (T) hibernateTemplate.get(this.persistentClass, id);

		if (entity == null) {
			log.warn(this.persistentClass + "' object with id '" + id
					+ "' not found...");
			throw new ObjectRetrievalFailureException(this.persistentClass, id);
		}

		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public boolean exists(PK id) {
		T entity = (T) hibernateTemplate.get(this.persistentClass, id);
		return entity != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public T save(T object) {
		return (T) hibernateTemplate.merge(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(PK id) {
		hibernateTemplate.delete(this.get(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> findByNamedQuery(String queryName,
			Map<String, Object> queryParams) {
		String[] params = new String[queryParams.size()];
		Object[] values = new Object[queryParams.size()];

		int index = 0;
		for (String s : queryParams.keySet()) {
			params[index] = s;
			values[index++] = queryParams.get(s);
		}

		return hibernateTemplate.findByNamedQueryAndNamedParam(queryName,
				params, values);
	}
}

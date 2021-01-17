package co.aurasphere.aura.common.interfaces.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base DAO interface with CRUD operations for an object T.
 * 
 * @author Donato Rimenti
 * @date 04/mag/2016
 * @param <T> the entity to manage.
 */
@Repository
@Transactional
public abstract class AuraBaseDao<T>{
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	/**
	 * Creates an object.
	 * @param object to create.
	 */
	public abstract void create(T object);
	
	/**
	 * Reads all objects.
	 * @return a list of objects.
	 */
	public abstract List<T> readAll();
	
	/**
	 * Updates an object.
	 * @param object to update.
	 */
	public abstract void update(T object);
	
	/**
	 * Deletes an object.
	 * @param object to delete.
	 */
	public abstract void delete(T object);

}

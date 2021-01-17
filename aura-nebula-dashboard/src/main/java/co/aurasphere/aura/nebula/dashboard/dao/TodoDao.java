package co.aurasphere.aura.nebula.dashboard.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import co.aurasphere.aura.common.interfaces.dao.AuraBaseDao;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;

@Repository
public class TodoDao extends AuraBaseDao<Todo>{
	
	private Logger log = LoggerFactory.getLogger(TodoDao.class);

	@Override
	public void create(Todo object) {
		sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public List<Todo> readAll() {
		return sessionFactory.getCurrentSession().createQuery("from Todo t where t.project is null").list();
//		return sessionFactory.getCurrentSession().createCriteria(Todo.class).list();
	}

	@Override
	public void update(Todo object) {
		sessionFactory.getCurrentSession().update(object);	
	}

	@Override
	public void delete(Todo object) {
		sessionFactory.getCurrentSession().delete(object);
	}
	
}

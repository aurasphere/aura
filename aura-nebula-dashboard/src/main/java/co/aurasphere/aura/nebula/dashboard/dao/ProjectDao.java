package co.aurasphere.aura.nebula.dashboard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import co.aurasphere.aura.common.interfaces.dao.AuraBaseDao;
import co.aurasphere.aura.nebula.dashboard.common.entities.Project;

@Repository
public class ProjectDao extends AuraBaseDao<Project>{

	@Override
	public void create(Project object) {
		sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public List<Project> readAll() {
		return sessionFactory.getCurrentSession().createCriteria(Project.class).list();
	}

	@Override
	public void update(Project object) {
		sessionFactory.getCurrentSession().update(object);
	}

	@Override
	public void delete(Project object) {
		sessionFactory.getCurrentSession().delete(object);
	}
}

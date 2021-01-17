package co.aurasphere.aura.nebula.dashboard.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import co.aurasphere.aura.common.interfaces.dao.AuraBaseDao;
import co.aurasphere.aura.nebula.dashboard.common.enumeration.ProjectCategory;

@Repository
public class ProjectCategoryDao extends AuraBaseDao<ProjectCategory>{
	
	private static final Logger log = LoggerFactory.getLogger(ProjectCategoryDao.class);

	@Override
	public void create(ProjectCategory object) {
		sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public List<ProjectCategory> readAll() {
		return sessionFactory.getCurrentSession().createCriteria(ProjectCategory.class).list();
	}

	@Override
	public void update(ProjectCategory object) {
		sessionFactory.getCurrentSession().update(object);
	}

	@Override
	public void delete(ProjectCategory object) {
		log.error("Operazione delete non consentita su classe " + object.getClass().getName());
		return;
	}
}

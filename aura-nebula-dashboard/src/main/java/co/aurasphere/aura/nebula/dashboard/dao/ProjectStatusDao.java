package co.aurasphere.aura.nebula.dashboard.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import co.aurasphere.aura.common.interfaces.dao.AuraBaseDao;
import co.aurasphere.aura.nebula.dashboard.common.enumeration.ProjectStatus;

@Repository
public class ProjectStatusDao extends AuraBaseDao<ProjectStatus>{
	
	private static final Logger log = LoggerFactory.getLogger(ProjectStatusDao.class);

	@Override
	public void create(ProjectStatus object) {
		sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public List<ProjectStatus> readAll() {
		return sessionFactory.getCurrentSession().createCriteria(ProjectStatus.class).list();
	}

	@Override
	public void update(ProjectStatus object) {
		sessionFactory.getCurrentSession().update(object);
	}

	@Override
	public void delete(ProjectStatus object) {
		log.error("Operazione delete non consentita su classe " + object.getClass().getName());
		return;
	}
}

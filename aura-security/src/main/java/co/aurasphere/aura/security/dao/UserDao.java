package co.aurasphere.aura.security.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import co.aurasphere.aura.common.interfaces.dao.AuraBaseDao;
import co.aurasphere.aura.security.model.User;

@Repository
public class UserDao extends AuraBaseDao<User>{
	
	private static final String Q_GET_USER_BY_NAME_PASS = "from User u where u.username = :user and u.password = :pass";
	private Logger log = LoggerFactory.getLogger(UserDao.class);

	@Override
	public void create(User object) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<User> readAll() {
		return (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class).list();
	}

	@Override
	public void update(User object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(User object) {
		// TODO Auto-generated method stub
	}
	
	public User getUser(String username, String password){
		return (User) sessionFactory.getCurrentSession().createQuery(Q_GET_USER_BY_NAME_PASS).setString("user", username).setString("pass", password).uniqueResult();
	}

}

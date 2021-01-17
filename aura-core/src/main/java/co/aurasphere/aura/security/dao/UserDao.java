package co.aurasphere.aura.security.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import co.aurasphere.aura.common.interfaces.dao.AuraBaseDao;
import co.aurasphere.aura.security.model.User;

/**
 * Dao which retrieves the users. Used for authentication.
 * @author Donato Rimenti
 * @date 13/mag/2016
 */

@Repository
public class UserDao extends AuraBaseDao<User>{
	
	private static final String Q_GET_USER_BY_NAME_PASS = "from User u where u.username = :user and u.password = :pass";

	@Override
	public void create(User object) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<User> readAll() {
		// TODO Auto-generated method stub
		return null;
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

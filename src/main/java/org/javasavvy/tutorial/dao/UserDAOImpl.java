package org.javasavvy.tutorial.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.javasavvy.tutorial.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("userDAO")
@Transactional(propagation = Propagation.REQUIRED)
public class UserDAOImpl implements UserDAO {

	@PersistenceContext
	public EntityManager entityManager;

	@Transactional(readOnly = false)
	public User addUser(User user) {
		System.out.println("1.Persisting User in UserDAOImpl:" + user);
		entityManager.persist(user);
		System.out.println("2.Persisting User in UserDAOImpl:" + user);
		return user;
	}

	@Transactional(readOnly = true)
	public User getUser(long id) {
		Query query = entityManager.createQuery("select user from User user where user.id=:id");
		query.setParameter("id", id);
		return (User) query.getSingleResult();
	}

	@Transactional(readOnly = true)
	public User getUser(String email) {
		return (User) entityManager.createQuery("select user from User user where" + " user.email=:email")
				.setParameter("email", email).getSingleResult();
	}

	@Transactional(readOnly = true)
	public User getUserById(long id) {

		Query query = entityManager.createQuery("select user from User user where user.id=:id");
		query.setParameter("id", id);
		return (User) query.getSingleResult();
	}

	@Override
	@Transactional(readOnly = false)
	public User updateUser(User user) {
		Query query = entityManager.createQuery("select user from User user where user.id=:id");
		query.setParameter("id", user.getId());
		User updateableUser = (User) query.getSingleResult();
		updateableUser.setEmail(user.getEmail());
		updateableUser.setFirstName(user.getFirstName());
		updateableUser.setLastName(user.getLastName());
		updateableUser.setSex(user.getSex());
		updateableUser.setPassword(user.getPassword());
		updateableUser.setUpdatedDate(new Date(System.currentTimeMillis()));
		entityManager.merge(updateableUser);
		return updateableUser;
	}

	@Override
	@Transactional(readOnly = false)
	public User deleteUser(long id) {
		Query query = entityManager.createQuery("select user from User user where user.id=:id");
		query.setParameter("id", id);
		User deletableUser = (User) query.getSingleResult();
		entityManager.remove(deletableUser);
		return deletableUser;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isUserPresent(long id) {
		Query query = entityManager.createQuery("select user from User user where user.id=:id");
		query.setParameter("id", id);
		boolean isUserPresent = query.getResultList().isEmpty();	
		return !isUserPresent;
	}
}

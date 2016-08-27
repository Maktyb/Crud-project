package com.dao;

import com.model.User;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class.getName());

    private SessionFactory sessionFactory;

       public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.merge(user);
        LOGGER.info("User successfully saved. User details:" + user);
    }

    @Override
    public void updateUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        LOGGER.info(user.toString());
        session.update(user);
        LOGGER.info("User successfully update. User details:" + user);
    }

    @Override
    public void removeUser(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class, new Integer(id));

        if(user!=null){
            session.delete(user);
        }

        LOGGER.info("User successfully removed. User details:" + user);
    }



    @Override
    public User getUserById(int id) {
        Session session= this.sessionFactory.getCurrentSession();
        User user = (User)session.load(User.class, new Integer(id));
        LOGGER.info("User successfully loaded. User details: " + user);

        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        Session session = this.sessionFactory.getCurrentSession();
        List<User> userList = session.createQuery("From User").list();

        for (User user: userList){
            LOGGER.info("User list: " + user);
        }
        return userList;
    }

    @Override
    public List<User> searchUser(String name) {
        Session session= this.sessionFactory.getCurrentSession();

        Criteria criteria= session.createCriteria(User.class);
        criteria.add(Restrictions.like("name", name + "%"));

        LOGGER.info("User successfully loaded. User details: " + criteria.list());

        return criteria.list();
    }
}

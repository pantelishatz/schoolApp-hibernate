package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

import javax.inject.Named;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
@Named("userDAOImpl")
public class UserDAOImpl implements IUserDAO {
    public User insert(User user) {
        EntityManager em = getEntityManager();
        em.persist(user);
        return user;
    }
    @Override
    public User update(User user) {
        EntityManager em = getEntityManager();
        em.merge(user);
        return user;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        User userToDelete = em.find(User.class, id);
        em.remove(userToDelete);
    }

    @Override
    public  List<User> getByUsername(String username) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> selectQuery = builder.createQuery(User.class);
        Root<User> root = selectQuery.from(User.class);

        ParameterExpression<String> uUsername = builder.parameter(String.class);
        selectQuery.select(root).where((builder.like(root.get("username"), uUsername)));

        TypedQuery<User> query = getEntityManager().createQuery(selectQuery);
        query.setParameter(uUsername, username + "%");
        return query.getResultList();
    }

    @Override
    public User getById(Long id) {
        EntityManager em = getEntityManager();
        return em.find(User.class, id);
    }

    private EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }
}
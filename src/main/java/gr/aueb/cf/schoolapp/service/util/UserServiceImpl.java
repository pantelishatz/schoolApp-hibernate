package gr.aueb.cf.schoolapp.service.util;

import gr.aueb.cf.schoolapp.dao.IUserDAO;
import gr.aueb.cf.schoolapp.dto.UserDTO;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ext.Provider;
import java.util.List;
@Provider
@RequestScoped
public class UserServiceImpl implements IUserService {

    @Inject
    private IUserDAO userDAO;

    @Override
    public User insertUser(UserDTO userDTO) {
        User user = null;
        try {
            JPAHelper.beginTransaction();
            user = map(userDTO);
            userDAO.insert(user);
            JPAHelper.commitTransaction();
        } catch (Exception e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Insert user - " + "rollback - " + e.getMessage());
        } finally {
            JPAHelper.closeEntityManager();
        }
        return user;
    }
    @Override
    public User updateUser(UserDTO userDTO) throws EntityNotFoundException {
        User userToUpdate = null;
        try {
            JPAHelper.beginTransaction();
            userToUpdate = map(userDTO);
            User existingUser = userDAO.getById(userToUpdate.getId());
            if (existingUser == null) {
                throw new EntityNotFoundException(User.class, userToUpdate.getId());
            }
            existingUser.setUsername(userToUpdate.getUsername());
            existingUser.setPassword(userToUpdate.getPassword());
            userDAO.update(existingUser);
            JPAHelper.commitTransaction();
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Update user - " + "rollback - entity not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return userToUpdate;
    }

    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            User userToDelete = userDAO.getById(id);
            if (userToDelete == null) {
                throw new EntityNotFoundException(User.class, id);
            }
            userDAO.delete(id);
            JPAHelper.commitTransaction();
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Delete rollback");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<User> getByUsername(String username) throws EntityNotFoundException {
        List<User> users;
        try {
            JPAHelper.beginTransaction();
            users = userDAO.getByUsername(username);
            if (users.size() == 0) {
                throw new EntityNotFoundException(List.class, 0L);
            }
            JPAHelper.commitTransaction();
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Get User rollback " + "- User not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return users;
    }

    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        User user = null;
        try {
            JPAHelper.beginTransaction();
            user = userDAO.getById(id);
            if (user == null) {
                throw new EntityNotFoundException(User.class, id);
            }
            JPAHelper.commitTransaction();
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Get user by id rollback " + "- User not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return user;
    }

    private User map(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
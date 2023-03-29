package gr.aueb.cf.schoolapp.service.util;

import gr.aueb.cf.schoolapp.dto.UserDTO;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IUserService {
    User insertUser(UserDTO userDTO) throws EntityAlreadyExistsException;
    User updateUser(UserDTO userDTO) throws EntityNotFoundException;
    void deleteUser(Long id) throws EntityNotFoundException;
    List<User> getByUsername(String username) throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
}
package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.dto.UserDTO;
import gr.aueb.cf.schoolapp.model.User;

import gr.aueb.cf.schoolapp.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.util.IUserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class UserRestController {
    @Inject
    private IUserService userService;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByUsername(@QueryParam("username") String username) {
        List<User> users;
        try {
            users = userService.getByUsername(username);
            List<UserDTO> usersDTO = new ArrayList<>();
            for (User user : users) {
                usersDTO.add(new UserDTO(user.getId(), user.getUsername(), user.getPassword()));
            }
            return Response.status(Response.Status.OK).entity(usersDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("NOT FOUND").build();
        }
    }

    @Path("/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Long userId) {
        User user;
        try {
            user = userService.getUserById(userId);
            UserDTO userDto = new UserDTO(user.getId(), user.getUsername(), user.getPassword());
            return Response.status(Response.Status.OK).entity(userDto).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("NOT FOUND").build();
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO userDTO) {
        try {
            User user = userService.insertUser(userDTO);
            UserDTO createdUserDTO = map(user);
            return Response.status(Response.Status.CREATED).entity(createdUserDTO).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
        }
    }
    @Path("/{userId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Long userId) {
        try {
            User user = userService.getUserById(userId);
            userService.deleteUser(userId);
            UserDTO userDTO = map(user);
            return Response.status(Response.Status.OK).entity(userDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User Not Found")
                    .build();
        }
    }
    @Path("/{userId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") Long userId, UserDTO dto) {
        try {
            dto.setId(userId);
            User user = userService.updateUser(dto);
            UserDTO userDTO = map(user);
            return Response.status(Response.Status.OK).entity(userDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }
    }
    private UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}

package gr.aueb.cf.schoolapp.service.exceptions;

public class EntityAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(Class<?> entityClass, long id) {
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " does not exist");
    }
}

package br.ufal.ic.jackut.service;

import java.util.UUID;

import br.ufal.ic.jackut.exception.user.AlreadyUserException;
import br.ufal.ic.jackut.exception.user.AttributeNotFillException;
import br.ufal.ic.jackut.exception.user.InvalidPasswordException;
import br.ufal.ic.jackut.exception.user.InvalidSessionException;
import br.ufal.ic.jackut.exception.user.InvalidUsernameException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.User;
import br.ufal.ic.jackut.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void CleanUp() {
        this.userRepository.CleanUp();
    }

    public User getUserByLogin(String username) throws UserNotFoundException {
        return this.userRepository.getUserList()
            .stream()
            .filter(e -> e.getUsername().equals(username))
            .findFirst()
            .orElseThrow(UserNotFoundException::new);
    }

    public String getAtributeByUsername(String username, String attribute) throws UserNotFoundException, AttributeNotFillException {
        User user = this.getUserByLogin(username);
        
        if(user.getAttribute(attribute) == null) {
            throw new AttributeNotFillException();
        }

        return user.getAttribute(attribute);
    }

    public void createUser(String username, String password, String name) throws AlreadyUserException, InvalidUsernameException, InvalidPasswordException {
        if(username == null || username.isEmpty()) {
            throw new InvalidUsernameException();
        }

        if(password == null || password.isEmpty()) {
            throw new InvalidPasswordException();
        }
        
        if(this.hasUser(username)) {
            throw new AlreadyUserException();
        }
        
        String id = UUID.randomUUID().toString();
        User user = new User(username, password, name, id);

        this.userRepository.addUser(user);
    }

    public String openSession(String username, String password) throws InvalidSessionException {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidSessionException();
        }
        
        try {
            User user = this.getUserByLogin(username);
            if (!user.getPassword().equals(password)) {
                throw new InvalidSessionException();
            }

            return user.getId();
        } catch (Exception e) {
            throw new InvalidSessionException();
        }
    }

    public User getUserById(String id) throws UserNotFoundException {
        return this.userRepository.getUserList()
            .stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElseThrow(UserNotFoundException::new);
    }


    public void editProfile(String id, String attribute, String value) throws UserNotFoundException {
        User record = this.getUserById(id);
        record.setAttribute(attribute, value);
        
        this.updateUser(record);
    }

    public boolean isRegistered(String idOrUsername) throws UserNotFoundException{
        try {
            if (this.isUUID(idOrUsername)) {
                this.getUserById(idOrUsername);
            } else {
                this.getUserByLogin(idOrUsername);
            }
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
        
    }

    private void updateUser(User modifiedUser) {
        this.userRepository.updateUser(modifiedUser);
    }

    private boolean hasUser(String username) {
        try {
            this.getUserByLogin(username);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    private boolean isUUID(String value) {
        return value != null && value.length() == 36 && value.contains("-");
    }
}
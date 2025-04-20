package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exception.user.AlreadyUserException;
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

    public String getAtributeByLogin(String login, String attribute) throws UserNotFoundException {
        User user = this.getUserByLogin(login);
        
        return switch (attribute.toLowerCase()) {
            case "nome" -> user.getName();
            case "senha" -> user.getPassword();
            default -> throw new IllegalArgumentException("Atributo inválido: " + attribute);
        };
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
        
        User user = new User(username, password, name);
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

            return user.getName();
        } catch (Exception e) {
            throw new InvalidSessionException();
        }
      

        
    }

    private boolean hasUser(String username) {
        try {
            this.getUserByLogin(username);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
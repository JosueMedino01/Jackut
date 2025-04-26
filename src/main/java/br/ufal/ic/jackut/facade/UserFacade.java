package br.ufal.ic.jackut.facade;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.friendship.RegisteredFriendshipException;
import br.ufal.ic.jackut.exception.friendship.RegisteredInviteException;
import br.ufal.ic.jackut.exception.friendship.SelfFriendshipException;
import br.ufal.ic.jackut.exception.user.AlreadyUserException;
import br.ufal.ic.jackut.exception.user.AttributeNotFillException;
import br.ufal.ic.jackut.exception.user.InvalidPasswordException;
import br.ufal.ic.jackut.exception.user.InvalidSessionException;
import br.ufal.ic.jackut.exception.user.InvalidUsernameException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Friendship;
import br.ufal.ic.jackut.service.FriendshipService;
import br.ufal.ic.jackut.service.UserService;

public class UserFacade {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserFacade() {
        this.userService = new UserService();
        this.friendshipService = new FriendshipService();
    }

    public void zerarSistema(){
        this.userService.CleanUp();
    }

    public String getAtributoUsuario(String username, String attribute) throws UserNotFoundException, AttributeNotFillException{
        return this.userService.getAtributeByUsername(username, attribute);
    }

    public void criarUsuario(String username, String password, String name) throws AlreadyUserException, InvalidUsernameException, InvalidPasswordException {
        this.userService.createUser(username, password, name);
    }

    public String abrirSessao(String username, String password) throws InvalidSessionException {
        return this.userService.openSession(username, password);
    }

    public void editarPerfil(String id, String attribute, String value) throws UserNotFoundException {
        this.userService.editProfile(id, attribute, value);
    }

    public boolean ehAmigo(String username, String friend) throws UserNotFoundException {
        return this.friendshipService.isFriend(username, friend);
    }

    public void adicionarAmigo(String id, String friendUsername) throws UserNotFoundException, RegisteredInviteException, RegisteredFriendshipException, SelfFriendshipException {
        this.friendshipService.addFriend(id, friendUsername);
    }

    public String getAmigos(String username) throws UserNotFoundException {
        return this.friendshipService.getFriends(username);
    }

    public void encerrarSistema() {
        
    }
}

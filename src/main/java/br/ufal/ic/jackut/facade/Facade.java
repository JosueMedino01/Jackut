package br.ufal.ic.jackut.facade;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.community.CommunityNotFoundException;
import br.ufal.ic.jackut.exception.community.RegisteredCommunityException;
import br.ufal.ic.jackut.exception.community.UserAlreadyInCommunityException;
import br.ufal.ic.jackut.exception.friendship.RegisteredFriendshipException;
import br.ufal.ic.jackut.exception.friendship.RegisteredInviteException;
import br.ufal.ic.jackut.exception.friendship.SelfFriendshipException;
import br.ufal.ic.jackut.exception.message.MessageIsEmptyException;
import br.ufal.ic.jackut.exception.message.SelfMessageException;
import br.ufal.ic.jackut.exception.user.AlreadyUserException;
import br.ufal.ic.jackut.exception.user.AttributeNotFillException;
import br.ufal.ic.jackut.exception.user.InvalidPasswordException;
import br.ufal.ic.jackut.exception.user.InvalidSessionException;
import br.ufal.ic.jackut.exception.user.InvalidUsernameException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Friendship;
import br.ufal.ic.jackut.service.CommunityService;
import br.ufal.ic.jackut.service.FriendshipService;
import br.ufal.ic.jackut.service.MessageService;
import br.ufal.ic.jackut.service.UserService;

public class Facade {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final CommunityService communityService;

    public Facade() {
        this.userService = new UserService();
        this.friendshipService = new FriendshipService();
        this.messageService = new MessageService();
        this.communityService = new CommunityService();
    }

    public void zerarSistema(){
        this.userService.cleanUP();
        this.friendshipService.cleanUp();
        this.messageService.cleanUp();
        this.communityService.cleanUp();
    }

    public String getAtributoUsuario(String username, String attribute) throws UserNotFoundException, AttributeNotFillException{
        return this.userService.getAtributeByUsername(username, attribute);
    }

    public void criarUsuario(String username, String password, String name) throws AlreadyUserException, InvalidUsernameException, InvalidPasswordException, UserNotFoundException {
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

    public void enviarRecado(String broadcasterId, String receptorUsername, String message) throws UserNotFoundException, SelfMessageException {
        this.messageService.sendMessage(broadcasterId, receptorUsername, message);
    }

    public String lerRecado(String userId) throws MessageIsEmptyException {
        return this.messageService.readMessage(userId);
    }

    public void criarComunidade(String createrID, String name, String description) throws RegisteredCommunityException {
        this.communityService.createCommunity(createrID, name, description);
    }

    public String getDescricaoComunidade(String name) throws CommunityNotFoundException{
        return this.communityService.getDescriptionByCommunityName(name);
    }

    public String getDonoComunidade(String name) throws CommunityNotFoundException{
        return this.communityService.getOwner(name);
    }

    public String getMembrosComunidade(String name) throws CommunityNotFoundException {
        return this.communityService.getMembers(name);
    }

    public String getComunidades(String username) throws UserNotFoundException{
        return this.communityService.getCommunities(username);
    }

    public void adicionarComunidade(String userId, String nameCommunity) 
        throws UserAlreadyInCommunityException, CommunityNotFoundException, UserNotFoundException 
    {
        this.communityService.addMember(userId, nameCommunity);
    }

    public void encerrarSistema() {
    }
}

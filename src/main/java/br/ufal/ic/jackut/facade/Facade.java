package br.ufal.ic.jackut.facade;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.community.CommunityNotFoundException;
import br.ufal.ic.jackut.exception.community.MessageNotFoundException;
import br.ufal.ic.jackut.exception.community.RegisteredCommunityException;
import br.ufal.ic.jackut.exception.community.UserAlreadyInCommunityException;
import br.ufal.ic.jackut.exception.friendship.EnemyBlockException;
import br.ufal.ic.jackut.exception.friendship.RegisteredEnemyException;
import br.ufal.ic.jackut.exception.friendship.RegisteredFanException;
import br.ufal.ic.jackut.exception.friendship.RegisteredFlirtingException;
import br.ufal.ic.jackut.exception.friendship.RegisteredFriendshipException;
import br.ufal.ic.jackut.exception.friendship.RegisteredInviteException;
import br.ufal.ic.jackut.exception.friendship.SelfEnemyException;
import br.ufal.ic.jackut.exception.friendship.SelfFanException;
import br.ufal.ic.jackut.exception.friendship.SelfFlirtingException;
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
import br.ufal.ic.jackut.service.ManagerAccountService;
import br.ufal.ic.jackut.service.MessageService;
import br.ufal.ic.jackut.service.UserService;

public class Facade {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final CommunityService communityService;
    private final ManagerAccountService managerAccountService;

    public Facade() {
        this.userService = new UserService();
        this.friendshipService = new FriendshipService();
        this.messageService = new MessageService(this.friendshipService);
        this.communityService = new CommunityService();
        this.managerAccountService = new ManagerAccountService();
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

    public void adicionarAmigo(String id, String friendUsername) 
        throws UserNotFoundException, RegisteredInviteException, RegisteredFriendshipException, 
        SelfFriendshipException, EnemyBlockException
    {
        this.friendshipService.addFriend(id, friendUsername);
    }

    public String getAmigos(String username) throws UserNotFoundException {
        return this.friendshipService.getFriends(username);
    }

    public void enviarRecado(String broadcasterId, String receptorUsername, String message) 
        throws UserNotFoundException, SelfMessageException, EnemyBlockException
    {
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

    public String lerMensagem(String userId) throws MessageNotFoundException {
        return this.communityService.readMessage(userId);
    }

    public void enviarMensagem(String userId, String community, String message) 
        throws CommunityNotFoundException, UserNotFoundException 
    {
        this.communityService.sendMessage(userId, community, message);
    }

    public boolean ehFa(String userName, String idolName) throws UserNotFoundException {
        return this.friendshipService.isFan(userName, idolName);
    }

    public void adicionarIdolo(String userId, String idolName) 
        throws RegisteredFanException, UserNotFoundException, SelfFanException, EnemyBlockException
    {
        this.friendshipService.addIdol(userId, idolName);
    }

    public String getFas(String userName) {
        return this.friendshipService.getFans(userName);
    }

    public boolean ehPaquera(String userId, String flirtingName ) 
        throws UserNotFoundException
    {
        return this.friendshipService.isFlirting(userId, flirtingName);
    }
    
    public void adicionarPaquera(String userId, String flirtingName) 
        throws UserNotFoundException, SelfMessageException, SelfFlirtingException, RegisteredFlirtingException, EnemyBlockException
    {
        this.friendshipService.addFlirting(userId, flirtingName);
    }
    
    public String getPaqueras(String userId) {
        return this.friendshipService.getFlirting(userId);
    }

    public void adicionarInimigo(String userId, String enemyUsername) 
        throws UserNotFoundException, RegisteredEnemyException, SelfEnemyException
    {
        this.friendshipService.addEnemy(userId, enemyUsername);
    }

    public void removerUsuario(String userId) throws UserNotFoundException{
        this.managerAccountService.dellAccount(userId);
    }
    public void encerrarSistema() {
    }
}

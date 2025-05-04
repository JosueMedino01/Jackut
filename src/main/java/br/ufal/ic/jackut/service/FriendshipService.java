package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.friendship.RegisteredFanException;
import br.ufal.ic.jackut.exception.friendship.RegisteredFlirtingException;
import br.ufal.ic.jackut.exception.friendship.RegisteredFriendshipException;
import br.ufal.ic.jackut.exception.friendship.RegisteredInviteException;
import br.ufal.ic.jackut.exception.friendship.SelfFanException;
import br.ufal.ic.jackut.exception.friendship.SelfFlirtingException;
import br.ufal.ic.jackut.exception.friendship.SelfFriendshipException;
import br.ufal.ic.jackut.exception.message.SelfMessageException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Friendship;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.User;
import br.ufal.ic.jackut.repository.FriendshipRepository;

public class FriendshipService {

    private FriendshipRepository friendshipRepository;
    private UserService userService;
    private MessageService messageService;

    public FriendshipService() {
        this.friendshipRepository = new FriendshipRepository();
        this.userService = new UserService();
        this.messageService = new MessageService();
    }
    /**
     * Método utilizado para limpar o arquivo de persistência de dados do usuário
     */
    public void cleanUp() {
        this.friendshipRepository.cleanUp();
    }

    /**
     * Método usado para carregar as informações dos usuários cadastrados
     * @return Retorna os usuários cadastrados
     */
    public Friendship getFriendshipData() {
        return this.friendshipRepository.getFriendshipData();
    }

    /**
     * Método usado para verificar dois usuários são amigos
     * @param username Login/Username do primeiro usuário
     * @param frien Login/Username do segundo usuário
     * @return Retorna TRUE se os usuários são amigos, e FALSE caso contrário
     * @throws UserNotFoundException Caso algum Login/Username informado não esteja cadastrado
     */
    public boolean isFriend(String username, String friend) throws UserNotFoundException {
        if (!this.userService.isRegistered(username) || 
            !this.userService.isRegistered(friend)) 
        {
            throw new UserNotFoundException();
        }

        String usernameID = this.userService.getUserByLogin(username).getId(); 
        String friendID = this.userService.getUserByLogin(friend).getId(); 

        return this.isFriendByIds(usernameID, friendID);
    }

    /**
     * Método responsável por enviar um convite de amizade caso não exista uma solicitação de amizade,
     * caso exista uma solicitaçao de amizede pendente, é feito a confirmação de amizade e a solicitação
     * pendente é excluída 
     * @param requesterId ID do usuário que solicita a amizade
     * @param receiverUsername Login/Username do usuário que recebe o convite
     * @throws UserNotFoundException Caso um dos usuário não esteja cadastrado
     * @throws RegisteredInviteException Caso já exista um registro de convite pendente
     * @throws RegisteredFriendshipException Caso os usuários já sejam amigos
     * @throws SelfFriendshipException Caso seja os mesmos usuários na solicitação
     */
    public void addFriend(String requesterId, String receiverUsername) throws UserNotFoundException, RegisteredInviteException, RegisteredFriendshipException, SelfFriendshipException {
        if (!this.userService.isRegistered(receiverUsername)) throw new UserNotFoundException();
        
        String receiverId = this.userService.getUserByLogin(receiverUsername).getId();

        if (!this.userService.isRegistered(requesterId)) {
            throw new UserNotFoundException();
        }

        if(requesterId.equals(receiverId)) {
            throw new SelfFriendshipException();
        }

        if(isFriendByIds(requesterId, receiverId)) {
            throw new RegisteredFriendshipException();
        }

        if(hasInviteByIds(requesterId, receiverId)) {
            throw new RegisteredInviteException();
        }

        if(hasInviteByIds(receiverId, requesterId)) {
           this.confirmFriendship(requesterId, receiverId);
           return;
        }

        this.addInvite(requesterId, receiverId);
    }
    
    /**
     * Método para confirmar a solicitação de amizade
     * @param requesterId ID do usuário que solicita a amizade
     * @param receiverId ID do usuário que recebe o convite
     */
    private void confirmFriendship(String requesterId, String receiverId) {
        Friendship data = this.getFriendshipData();

        data.getFriendships().putIfAbsent(requesterId, new ArrayList<>());
        data.getFriendships().putIfAbsent(receiverId, new ArrayList<>());

        data.getFriendships().get(requesterId).add(receiverId);
        data.getFriendships().get(receiverId).add(requesterId);

        this.deleteInvite(requesterId, receiverId);
    
        this.friendshipRepository.saveFriendshipData(data);
    }
    
    /**
     * Método carrega os amigos pelo Login/Username do usuário
     * @param username Login/Username do usuário
     * @return Retorna uma listagem do nome dos amigos referentes aos usuário passado
     * @throws UserNotFoundException Caso o usuário não esteja cadastrado
     */
    public String getFriends(String username) throws UserNotFoundException{
        if (!this.userService.isRegistered(username)) {
            throw new UserNotFoundException();
        }

        String id = this.userService.getUserByLogin(username).getId();
        List<String> friendsIdList = getFriendsById(id);
        
        return formattedList(friendsIdList);
    }

    public boolean isFan(String userName, String idolName) throws UserNotFoundException {
        String userId = this.userService.getUserByLogin(userName).getId();
        String idolId = this.userService.getUserByLogin(idolName).getId();

        if(userId == null || idolId == null) 
            throw new UserNotFoundException();

        List<String> fans = this.getFansById(idolId);

        if(fans == null) return false;

        for(String user : fans) {
            if (user.equals(userId)) return true;
        }

        return false;
    }

    public void addIdol(String userId, String idolName) 
        throws RegisteredFanException, UserNotFoundException, SelfFanException
    {
        if (!this.userService.isRegistered(userId) || 
            !this.userService.isRegistered(idolName)) 
        {
            throw new UserNotFoundException();
        }

        String idolId = this.userService.getUserByLogin(idolName).getId();

        if (userId.equals(idolId))
            throw new SelfFanException();
        Friendship friendship = this.friendshipRepository.getFriendshipData();

        friendship.getFans().putIfAbsent(idolId, new ArrayList<>());

        if( friendship.getFans().get(idolId).contains(userId))
            throw new RegisteredFanException();

        friendship.getFans().get(idolId).add(userId);

        this.friendshipRepository.saveFriendshipData(friendship);
    }

    public String getFans(String userName) {
        String userId = this.userService.getUserByLogin(userName).getId();
        return formattedList(this.getFansById(userId));
    }

    public boolean isFlirting(String userId, String flirtingName )
        throws UserNotFoundException
    {
        String flirtingId = this.userService.getUserByLogin(flirtingName).getId();

        if(userId == null || flirtingId == null) 
            throw new UserNotFoundException();

        List<String> flirtingList = this.getFlirtingById(userId);

        if(flirtingList == null) return false;

        return flirtingList.contains(flirtingId);
    }

    public void addFlirting(String userId, String flirtingName) 
        throws UserNotFoundException, SelfMessageException, SelfFlirtingException, RegisteredFlirtingException
    {

        if (!this.userService.isRegistered(userId) || 
            !this.userService.isRegistered(flirtingName)) 
        {
            throw new UserNotFoundException();
        }
        
        User flirting = this.userService.getUserByLogin(flirtingName);

        if(flirting.getId().equals(userId)) 
            throw new SelfFlirtingException();

        Friendship friendship = this.friendshipRepository.getFriendshipData();

        friendship.getFlirtingMap().putIfAbsent(userId, new ArrayList<>());

        if (friendship.getFlirtingMap().get(userId).contains(flirting.getId()))
            throw new RegisteredFlirtingException();

        friendship.getFlirtingMap().get(userId).add(flirting.getId());

        if (friendship.getFlirtingMap().get(flirting.getId()) != null && 
            friendship.getFlirtingMap().get(flirting.getId()).contains(userId)
            ) {
            User user = this.userService.getUserById(userId);

            String msgToFliting = user.getAttribute("nome") + " é seu paquera - Recado do Jackut.";
            String msgToUser = flirting.getAttribute("nome") + " é seu paquera - Recado do Jackut.";

            this.messageService.sendMessage("system", user.getUsername(), msgToUser );
            this.messageService.sendMessage("system", flirting.getUsername(), msgToFliting );
            
        }

        this.friendshipRepository.saveFriendshipData(friendship);
    }

    public String getFlirting(String userId) {
        List<String> flirtingList = this.getFlirtingById(userId);
        return formattedList(flirtingList);
    }
    /**
     * Método carrega os amigos pelo ID do usuário
     * @param id  ID do usuário
     * @returnRetorna uma listagem de IDs referentes aos usuário passado
     */
    private List<String> getFriendsById(String id) {
        return this.friendshipRepository.getFriendshipData().getFriendships().get(id);
    }
    /**
     * Método carrega as solicitações de amizade que um usuário está pendente de aceitar/recusar
     * @param id ID do usuário
     * @return Retorna a listagem de IDs das solicitações de amizade que o usuário está pendente de aceitar/recusar
     */
    private List<String> getInvitesById(String id) {
        return this.friendshipRepository.getFriendshipData().getInvites().get(id);
    }

    private List<String> getFansById(String id) {
        return this.friendshipRepository.getFriendshipData().getFans().get(id);
    }

    private List<String> getFlirtingById(String id) {
        return this.friendshipRepository.getFriendshipData().getFlirtingMap().get(id);
    }

    /**
     * Método responsável por deletar uma solicitação de amizade
     * @param requesterId ID do usuário que solicita a amizade
     * @param receiverId ID do usuário que recebe o convite
     */
    private void deleteInvite(String requesterId, String receiverId) {
        Friendship friendship = this.friendshipRepository.getFriendshipData();
        List<String> invites = friendship.getInvites().get(receiverId);
        
        if (invites != null) {
            invites.remove(requesterId);
        }
    }

    /**
     * Método utilizado para enviar um solicitação de amizade.
     * @param requesterId ID do usuário que está enviando a solicitação de amizade
     * @param receiverId ID do usuário que está sendo solicitado a amizade
     */
    private void addInvite(String requesterId, String receiverId) {
        Friendship friendship = this.friendshipRepository.getFriendshipData();

        
        friendship.getInvites().putIfAbsent(receiverId, new ArrayList<>());
        friendship.getInvites().get(receiverId).add(requesterId);

        this.friendshipRepository.saveFriendshipData(friendship);
    }

    /**
     * Método usado para verificar dois usuários são amigos
     * @param requesterId ID do usuário que está enviando a solicitação de amizade
     * @param receiverId ID do usuário que está sendo solicitado a amizade
     * @return  Retorna TRUE se os usuários são amigos, e FALSE caso contrário
     */
    private boolean isFriendByIds(String requesterId, String receiverId) {
        List<String> friends = this.friendshipRepository.getFriendshipData()
            .getFriendships()
            .get(requesterId);
        
        if (friends == null) {
            return false;
        }
    
        return friends.contains(receiverId);
    }
    
    /**
     * Método usado para verificar se existe solicitação de amizade pendente entre dois usuários
     * @param requesterId ID do usuário que está enviando a solicitação de amizade
     * @param receiverId ID do usuário que está sendo solicitado a amizade
     * @return  Retorna TRUE houver solicitação pendente, e FALSE caso contrário
     */
    private boolean hasInviteByIds(String requesterId, String receiverId) {
        List<String> invites =  this.friendshipRepository
                                    .getFriendshipData()
                                    .getInvites()
                                    .get(receiverId);
        
        if (invites == null) {
            return false;
        }

        return invites.contains(requesterId);
    }

    /**
     * Método responsável por formatar a lista de amizade no padrão esperado pelo EasyAccept
     * @param friendList Uma lista de IDs
     * @return Retorna uma string com todos os IDs convertidos aos seus referidos nomes e separados por vírgula
     * @throws UserNotFoundException
     */
    private String formattedList(List<String> list) {
        if (list == null) return "{}";

        list = list
            .stream()
            .map((String e) -> {
                return this.userService.getUserById(e).getUsername();
            })
            .toList();

        return list.toString().replace("[", "{").replace("]", "}").replace(" ", "");
    }
}
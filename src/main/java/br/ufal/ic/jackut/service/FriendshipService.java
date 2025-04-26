package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.friendship.RegisteredFriendshipException;
import br.ufal.ic.jackut.exception.friendship.RegisteredInviteException;
import br.ufal.ic.jackut.exception.friendship.SelfFriendshipException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Friendship;
import br.ufal.ic.jackut.repository.FriendshipRepository;

public class FriendshipService {

    private FriendshipRepository friendshipRepository;
    private UserService userService;

    public FriendshipService() {
        this.friendshipRepository = new FriendshipRepository();
        this.userService = new UserService();
    }

    public Friendship getFriendshipData() {
        return this.friendshipRepository.getFriendshipData();
    }

    public boolean isFriend(String username, String friend) throws UserNotFoundException {
        String usernameID = this.userService.getUserByLogin(username).getId(); 
        String friendID = this.userService.getUserByLogin(friend).getId(); 

        return this.isFriendByIds(usernameID, friendID);
    }

    public void addFriend(String requesterId, String receiverUsername) throws UserNotFoundException, RegisteredInviteException, RegisteredFriendshipException, SelfFriendshipException {
        String receiverId = this.userService.getUserByLogin(receiverUsername).getId();
        
        /* Melhorar melhor a validação de usuário já cadastrado */
        this.userService.getUserById(requesterId).getId();

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
    
    
    private void confirmFriendship(String requesterId, String receiverId) {
        Friendship data = this.getFriendshipData();

        data.getFriendships().putIfAbsent(requesterId, new ArrayList<>());
        data.getFriendships().putIfAbsent(receiverId, new ArrayList<>());

        data.getFriendships().get(requesterId).add(receiverId);
        data.getFriendships().get(receiverId).add(requesterId);

        this.deleteInvite(requesterId, receiverId);
    
        this.friendshipRepository.saveFriendshipData(data);
    }
    
    public String getFriends(String username) throws UserNotFoundException{
        String id = this.userService.getUserByLogin(username).getId();
        List<String> friendsIdList = getFriendsById(id);
        
        return formattedFriendList(friendsIdList);
    }

    private List<String> getFriendsById(String id) {
        return this.friendshipRepository.getFriendshipData().getFriendships().get(id);
    }

    private List<String> getInvitesById(String id) {
        return this.friendshipRepository.getFriendshipData().getInvites().get(id);
    }

    private void deleteInvite(String requesterId, String receiverId) {
        Friendship friendship = this.friendshipRepository.getFriendshipData();
        List<String> invites = friendship.getInvites().get(receiverId);
        
        if (invites != null) {
            invites.remove(requesterId);
        }
    }

    private void addInvite(String requesterId, String receiverId) {
        Friendship friendship = this.friendshipRepository.getFriendshipData();

        
        friendship.getInvites().putIfAbsent(receiverId, new ArrayList<>());
        friendship.getInvites().get(receiverId).add(requesterId);

        this.friendshipRepository.saveFriendshipData(friendship);
    }

    private boolean isFriendByIds(String requesterId, String receiverId) {
        List<String> friends = this.friendshipRepository.getFriendshipData()
            .getFriendships()
            .get(requesterId);
        
        if (friends == null) {
            return false;
        }
    
        return friends.contains(receiverId);
    }
    

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

    private String formattedFriendList(List<String> friendList) throws UserNotFoundException {
        if (friendList == null) return "{}";

        friendList = friendList.stream().map((String e) -> {
            try {
                String username = this.userService.getUserById(e).getUsername();
                e = username;
            } 
            catch (UserNotFoundException ex) {
                throw new RuntimeException("User not found for ID: " + e, ex);
            } 
            return e;
        }).toList();

        return friendList.toString().replace("[", "{").replace("]", "}").replace(" ", "");
    }

}
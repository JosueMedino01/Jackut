package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.friendship.RegisteredFriendshipException;
import br.ufal.ic.jackut.exception.friendship.RegisteredInviteException;
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

    public void addFriend(String requesterId, String receiverUsername) throws UserNotFoundException, RegisteredInviteException, RegisteredFriendshipException {
        String receiverId = this.userService.getUserByLogin(receiverUsername).getId();

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
        System.out.println("requesterId:" + requesterId + " receiverId: "+ receiverId);
        
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

}

/*   

    public void addInvite(String id, String friendId) {
        this.invites.putIfAbsent(friendId, new ArrayList<>());
        this.invites.get(friendId).add(id);
    }

    public void dellInvite(String id, String friendId) {
        this.invites.get(friendId).remove(id);
    }

    public void addFriend(String a, String b) {
        this.friendships.putIfAbsent(a, new ArrayList<>());
        this.friendships.get(a).add(b);

        this.friendships.putIfAbsent(b, new ArrayList<>());
        this.friendships.get(b).add(a);
    } */
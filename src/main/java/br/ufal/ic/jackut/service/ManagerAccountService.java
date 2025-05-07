package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exception.user.UserNotFoundException;

public class ManagerAccountService {
    private final UserService userService;
    private final CommunityService communityService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    

    public ManagerAccountService() {
        this.userService = new UserService();
        this.communityService = new CommunityService();
        this.friendshipService = new FriendshipService();
        this.messageService = new MessageService(friendshipService);
    }

    public void dellAccount(String userId) throws UserNotFoundException{
        this.userService.dellProfile(userId);
        this.communityService.removeCommunityByOwnerId(userId);
        this.messageService.deleteMessagesByUserId(userId);
    }
}

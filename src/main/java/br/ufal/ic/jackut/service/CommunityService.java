package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.ufal.ic.jackut.exception.community.CommunityNotFoundException;
import br.ufal.ic.jackut.exception.community.MessageNotFoundException;
import br.ufal.ic.jackut.exception.community.RegisteredCommunityException;
import br.ufal.ic.jackut.exception.community.UserAlreadyInCommunityException;
import br.ufal.ic.jackut.exception.message.MessageIsEmptyException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Community;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageStore;
import br.ufal.ic.jackut.repository.CommunityRepository;
import br.ufal.ic.jackut.repository.MessageRepository;

public class CommunityService {
    private CommunityRepository communityRepository;
    private UserService userService;
    private MessageRepository messageRepository;
    private MessageStore data;

    public CommunityService() {
        this.communityRepository = new CommunityRepository();
        this.userService = new UserService();
        this.messageRepository = new MessageRepository();
        this.data = this.messageRepository.get();
    }
    
    public void cleanUp() {
        this.communityRepository.cleanUp();
    }

    public void createCommunity(String createrID, String name, String description ) throws RegisteredCommunityException {
        if (this.getCommunityByName(name) != null) {
            throw new RegisteredCommunityException();
        }
        List<String> members = new ArrayList<String>(); members.add(createrID);
       
        this.addCommunity(
            new Community(createrID, name, description, members)
        );
    }

    public String getDescriptionByCommunityName(String name) throws CommunityNotFoundException {
        Community community = this.getCommunityByName(name);

        if (community == null) 
            throw new CommunityNotFoundException();

        return community.getDescription();
    }

    public String getOwner(String name) throws CommunityNotFoundException {
        Community community = this.getCommunityByName(name);
        if (community == null) 
            throw new CommunityNotFoundException();
        
        return this.userService.getUserById(community.getOwnerId()).getUsername();
    }

    public String getMembers(String name) throws CommunityNotFoundException {
        Community community = this.getCommunityByName(name);

        if (community == null) 
            throw new CommunityNotFoundException();
        
        return this.formattedMemberList(community.getMembers());
    } 

    public String getCommunities(String username) throws UserNotFoundException {
        if (this.userService.getUserByLogin(username) == null) {
            throw new UserNotFoundException();
        }

        String userId = this.userService.getUserByLogin(username).getId();

        List<String> community = this.getCommunityList()
            .stream()
            .sorted((c1, c2) -> {
                int idx1 = c1.getMembers().indexOf(userId);
                int idx2 = c2.getMembers().indexOf(userId);
                return Integer.compare(idx1, idx2);
            })
            .filter(c -> c.getMembers().contains(userId))
            .map(Community::getName)
            .toList();

        return this.fomattedStringList(community);
    }
    
    public void addMember(String userId, String communityName) throws UserAlreadyInCommunityException, CommunityNotFoundException, UserNotFoundException{
        List<Community> communities = this.getCommunityList();

        if (!this.userService.isRegistered(userId)) 
            throw new UserNotFoundException();

        for(Community community : communities) {
            if (community.getName().equals(communityName)) {
                
                List<String> members;
                members = community.getMembers(); 

                if(members.contains(userId)) 
                    throw new UserAlreadyInCommunityException();
                
                members.add(userId);

                community.setMembers(members);
                this.communityRepository.saveCommunityList(communities);
                return;
            }
        }

        throw new CommunityNotFoundException();
    }

    public String readMessage(String userId) throws MessageNotFoundException {
        Queue<Message> queue = this.data.getCommunityMessages().get(userId);
    
        if (queue == null || queue.isEmpty()) {
            throw new MessageNotFoundException();
        }

        Message msg = queue.poll();
        this.messageRepository.save(this.data);

        return msg.getMessage();
    }

    public void sendMessage(String userId, String communityName, String message) 
        throws CommunityNotFoundException, UserNotFoundException 
    {
        if (!this.userService.isRegistered(userId))
            throw new UserNotFoundException();
       
        Message msg = new Message(userId, communityName, message);
        Community community = this.getCommunityByName(communityName);

        if (community == null) 
            throw new CommunityNotFoundException();
        
        for(String receptorId : community.getMembers()) {
            this.data.getCommunityMessages().putIfAbsent(receptorId, new LinkedList<>());
            this.data.getCommunityMessages().get(receptorId).add(msg);
        }

        this.messageRepository.save(this.data);
    }

    public void addCommunity(Community community) {
        List<Community> communities = getCommunityList();
        communities.add(community);
        this.communityRepository.saveCommunityList(communities);
    }

    public void removeCommunityByOwnerId(String ownerId) {
        List<Community> communities = getCommunityList();
        communities.removeIf(c -> c.getOwnerId().equals(ownerId));
        this.communityRepository.saveCommunityList(communities);
    }

    private Community getCommunityByName(String name) {
        return this.getCommunityList()
            .stream()
            .filter(community -> community.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    private List<Community> getCommunityList() {
        return this.communityRepository.getCommunityList();
    }

    private String formattedMemberList(List<String> memberList) {
        if (memberList == null) return "{}";

        memberList = memberList
            .stream()
            .map((String e) -> {
                return this.userService.getUserById(e).getUsername();
            })
            .toList();

        return memberList.toString().replace("[", "{").replace("]", "}").replace(" ", "");
    }


    private String fomattedStringList(List<String> list) {
        if (list == null) return "{}";

        return list.toString().replace("[", "{").replace("]", "}").replace(", ", ",");
    }
}

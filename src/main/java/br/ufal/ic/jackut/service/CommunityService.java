package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.jackut.exception.community.CommunityNotFoundException;
import br.ufal.ic.jackut.exception.community.RegisteredCommunityException;
import br.ufal.ic.jackut.model.Community;
import br.ufal.ic.jackut.repository.CommunityRepository;

public class CommunityService {
    private CommunityRepository communityRepository;
    private UserService userService;

    public CommunityService() {
        this.communityRepository = new CommunityRepository();
        this.userService = new UserService();
    }

    public void createCommunity(String createrID, String name, String description ) throws RegisteredCommunityException {
        if (this.getCommunityByName(name) != null) {
            throw new RegisteredCommunityException();
        }
        List<String> members = new ArrayList<String>(); members.add(createrID);
       
        this.communityRepository.addCommunity(
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

    public void cleanUp() {
        this.communityRepository.cleanUp();
    }

    private Community getCommunityByName(String name) {
        return this.getCommunity()
            .stream()
            .filter(community -> community.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    private List<Community> getCommunity() {
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
}

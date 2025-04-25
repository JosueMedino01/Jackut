package br.ufal.ic.jackut.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friendship {
    private Map<String, List<String>> friendships;
    private Map<String, List<String>> invites;

    public Friendship() {
        this.friendships = new HashMap<>();
        this.invites = new HashMap<>();
    }

    public Map<String, List<String>> getFriendships() {
        return friendships;
    }

    public void setFriendships(Map<String, List<String>> friendships) {
        this.friendships = friendships;
    }

    public Map<String, List<String>> getInvites() {
        return invites;
    }

    public void setInvites(Map<String, List<String>> invites) {
        this.invites = invites;
    }   
}

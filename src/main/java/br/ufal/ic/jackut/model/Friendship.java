package br.ufal.ic.jackut.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friendship implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, List<String>> friendships;
    private Map<String, List<String>> invites;
    private Map<String, List<String>> fans;
    private Map<String, List<String>> flirtingMap;
    private Map<String, List<String>> enemies;

    public Friendship() {
        this.friendships = new HashMap<>();
        this.invites = new HashMap<>();
        this.fans = new HashMap<>();
        this.flirtingMap = new HashMap<>();
        this.enemies = new HashMap<>();
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

    public Map<String, List<String>> getFans() {
        return fans;
    }

    public void setFans(Map<String, List<String>> fans) {
        this.fans = fans;
    }

    public Map<String, List<String>> getFlirtingMap() {
        return flirtingMap;
    }

    public void setFlirtingMap(Map<String, List<String>> flirtingMap) {
        this.flirtingMap = flirtingMap;
    }

    public Map<String, List<String>> getEnemies() {
        return enemies;
    }

    public void setEnemies(Map<String, List<String>> enemies) {
        this.enemies = enemies;
    }
}

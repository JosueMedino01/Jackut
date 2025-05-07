package br.ufal.ic.jackut.model;

import java.io.Serializable;
import java.util.List;

public class Community implements Serializable  {
    private static final long serialVersionUID = 1L;
    private String ownerId;
    private String name;
    private String description;
    private List<String> members;
    
    public Community(String ownerId, String name, String description, List<String> members) {
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.members = members;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<String> getMembers() {
        return members;
    }
    public void setMembers(List<String> members) {
        this.members = members;
    }
}

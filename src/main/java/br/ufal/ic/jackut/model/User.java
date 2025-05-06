package br.ufal.ic.jackut.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String username;
    private String password;
    private Map<String, String> attributes;


    public User(String username, String password, String name, String id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.attributes = new HashMap<>(); 
        this.attributes.put("nome", name);
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }
}


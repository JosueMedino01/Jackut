package br.ufal.ic.jackut.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MessageStore implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, Queue<Message>> privateMessages;
    private Map<String, Queue<Message>> communityMessages;

    public MessageStore() {
        this.privateMessages = new HashMap<>();
        this.communityMessages = new HashMap<>();
    }

    public Map<String, Queue<Message>> getPrivateMessages() {
        return privateMessages;
    }

    public void setPrivateMessages(Map<String, Queue<Message>> privateMessages) {
        this.privateMessages = privateMessages;
    }

    public Map<String, Queue<Message>> getCommunityMessages() {
        return communityMessages;
    }

    public void setCommunityMessages(Map<String, Queue<Message>> communityMessages) {
        this.communityMessages = communityMessages;
    }
}

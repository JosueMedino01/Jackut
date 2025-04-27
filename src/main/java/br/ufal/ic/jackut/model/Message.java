package br.ufal.ic.jackut.model;

public class Message {
    private String broadcasterId;
    private String receptorId;
    private String message;
    
    public Message(String broadcasterId, String receptorUsername, String message) {
        this.broadcasterId = broadcasterId;
        this.receptorId = receptorUsername;
        this.message = message;
    }
    public String getBroadcasterId() {
        return broadcasterId;
    }
    public void setBroadcasterId(String broadcasterId) {
        this.broadcasterId = broadcasterId;
    }
    public String getReceptorId() {
        return receptorId;
    }
    public void setReceptorUsername(String receptorUsername) {
        this.receptorId = receptorUsername;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

  
}

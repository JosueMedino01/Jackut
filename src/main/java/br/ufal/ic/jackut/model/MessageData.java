package br.ufal.ic.jackut.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MessageData {
    private Map<String, Queue<Message>> tableMessage;

    public MessageData() {
        this.tableMessage = new HashMap<>();
    }

    public Map<String, Queue<Message>> getTableMessage() {
        return tableMessage;
    }

    public void setTableMessage(Map<String, Queue<Message>> tableMessage) {
        this.tableMessage = tableMessage;
    }

}

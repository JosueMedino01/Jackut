package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Friendship;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageData;

import java.util.Queue;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/MessageDB.json";
    private final Gson gson;

    public MessageRepository() {
        this.gson = new Gson();
    }

    public void save(MessageData messageData) {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            this.gson.toJson(messageData, writer);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MessageData get() {
        try (FileReader reader = new FileReader(this.pathDB)) {
            MessageData data =  this.gson.fromJson(reader, MessageData.class);

            return (data == null) ? new MessageData() : data;
        } 
        catch (IOException e) {
            e.printStackTrace();
            return new MessageData();
        }
    }

    public void cleanUp() {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            MessageData empty = new MessageData();
            this.gson.toJson(empty, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
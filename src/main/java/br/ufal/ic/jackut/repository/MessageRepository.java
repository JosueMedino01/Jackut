package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Friendship;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageStore;

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

    /**
     * Método responsável por salvar o Hash de mensagens
     * @param messageData Entidade que representa o Hash que armazena a Fila (Queue) das mensagens
     */
    public void save(MessageStore messageData) {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            this.gson.toJson(messageData, writer);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Método responsável por retornar o Hash que armazena a Fila (Queue) das mensagens
     */
    public MessageStore get() {
        try (FileReader reader = new FileReader(this.pathDB)) {
            MessageStore data =  this.gson.fromJson(reader, MessageStore.class);

            return (data == null) ? new MessageStore() : data;
        } 
        catch (IOException e) {
            e.printStackTrace();
            return new MessageStore();
        }
    }
    /**
     * Método responsável por limpar o arquivo de persistência de dados
     */
    public void cleanUp() {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            MessageStore empty = new MessageStore();
            this.gson.toJson(empty, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
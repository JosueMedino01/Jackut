package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.MessageStore;

import java.io.*;

public class MessageRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/MessageDB.txt";

    /**
     * Método responsável por salvar o Hash de mensagens
     * @param messageData Entidade que representa o Hash que armazena a Fila (Queue) das mensagens
     */
    public void save(MessageStore messageData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.pathDB))) {
            oos.writeObject(messageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por retornar o Hash que armazena a Fila (Queue) das mensagens
     */
    public MessageStore get() {
        File file = new File(this.pathDB);
        if (!file.exists()) {
            return new MessageStore();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.pathDB))) {
            return (MessageStore) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new MessageStore();
        }
    }

    /**
     * Método responsável por limpar o arquivo de persistência de dados
     */
    public void cleanUp() {
        save(new MessageStore()); // só salva um objeto vazio
    }
}

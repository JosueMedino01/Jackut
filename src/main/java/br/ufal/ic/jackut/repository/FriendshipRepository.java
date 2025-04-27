package br.ufal.ic.jackut.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import br.ufal.ic.jackut.model.Friendship;

public class FriendshipRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/FriendshipDB.json";
    private final Gson gson;

    public FriendshipRepository() {
        this.gson = new Gson();
    }

    /**
     * Método responsável por limpar o arquivo de persistência de dados
     */
    public void cleanUp() {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            Friendship empty = new Friendship();
            this.gson.toJson(empty, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por carregar a tabela de amizades
     * @return Retornar uma instância de Friendship, classe gerencia as amizades existentes
     */
    public Friendship getFriendshipData() {
        try (FileReader reader = new FileReader(this.pathDB)) {
            return this.gson.fromJson(reader, Friendship.class);
        } 
        catch (IOException e) {
            e.printStackTrace();
            return new Friendship();
        }
    }

    /**
     * Método responsalver por salvar a tabela de amizades
     * @param friendship Entidade que gerencia as amizades
     */
    public void saveFriendshipData(Friendship friendship) {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            this.gson.toJson(friendship, writer);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

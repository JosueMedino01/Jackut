package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Community;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommunityRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/CommunityDB.txt";

    /**
     * Limpa o arquivo de persistÊncias
     */
    public void cleanUp() {
        saveCommunityList(new ArrayList<>());
    }

    /**
     * Método responsável por listar todas as comunidades salvas
     * @return lista de comunidades
     */
    public List<Community> getCommunityList() {
        try (FileInputStream fileIn = new FileInputStream(this.pathDB);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            Object obj = in.readObject();
            if (obj instanceof List) {
                return (List<Community>) obj;
            } else {
                return new ArrayList<>();
            }

        } catch (IOException | ClassNotFoundException e) {
            // Arquivo não existe ou está vazio? Retorna lista vazia.
            return new ArrayList<>();
        }
    }

    /**
     * Salva uma lista de comunidade 
     * @param communityList lista de comunidade 
     */
    public void saveCommunityList(List<Community> communityList) {
        try (FileOutputStream fileOut = new FileOutputStream(this.pathDB);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(communityList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

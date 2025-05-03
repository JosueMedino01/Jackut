package br.ufal.ic.jackut.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import br.ufal.ic.jackut.model.Community;

public class CommunityRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/CommunityDB.json";
    private final Gson gson;

    public CommunityRepository() {
        this.gson = new Gson();
    }

    public void cleanUp() {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            // Escreve uma lista vazia []
            this.gson.toJson(new ArrayList<Community>(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Community> getCommunityList() {
        try (FileReader reader = new FileReader(this.pathDB)) {
            Type communityListType = new TypeToken<List<Community>>() {}.getType();
            List<Community> communities = this.gson.fromJson(reader, communityListType);

            if (communities == null) {
                // Caso o arquivo esteja vazio ou malformado, retorna lista vazia
                return new ArrayList<>();
            }

            return communities;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveCommunityList(List<Community> communityList) {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            this.gson.toJson(communityList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCommunity(Community community) {
        List<Community> communities = getCommunityList();
        communities.add(community);
        saveCommunityList(communities);
    }

    public void removeCommunityById(String name) {
        List<Community> communities = getCommunityList();
        communities.removeIf(c -> c.getName().equals(name));
        saveCommunityList(communities);
    }
}

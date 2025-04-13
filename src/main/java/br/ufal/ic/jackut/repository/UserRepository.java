package br.ufal.ic.jackut.repository;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class UserRepository {
    final private String pathDB = "./database.json";

    public void CleanUp() {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(this.pathDB)) {
            gson.toJson(new Object[] {}, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

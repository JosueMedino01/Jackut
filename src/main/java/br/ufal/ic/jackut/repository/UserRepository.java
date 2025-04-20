package br.ufal.ic.jackut.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.ufal.ic.jackut.model.User;

public class UserRepository {
    private final String pathDB = "./database.json";
    private final Gson gson;

    public UserRepository() {
        this.gson = new Gson();
    }

    public void CleanUp() {
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            this.gson.toJson(new Object[] {}, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUserList() {
        try (FileReader reader = new FileReader(this.pathDB)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            return this.gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); 
        }
    }

    public void addUser(User newUser) {
        List<User> currentUsers = getUserList(); // lê os usuários existentes
        currentUsers.add(newUser);              // adiciona o novo usuário
    
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            gson.toJson(currentUsers, writer);  // escreve a lista completa no JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User modifiedUser) {
        List<User> currentUsers = getUserList();
        
        for (int i = 0; i < currentUsers.size(); i++) {
            User existingUser = currentUsers.get(i);
            if (existingUser.getId().equals(modifiedUser.getId())) {
                
                currentUsers.set(i, modifiedUser); // substitui pelo novo
                break;
            }
        }
    
        try (FileWriter writer = new FileWriter(this.pathDB)) {
            gson.toJson(currentUsers, writer); // salva a lista atualizada
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

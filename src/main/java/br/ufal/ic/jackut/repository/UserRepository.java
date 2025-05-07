package br.ufal.ic.jackut.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import br.ufal.ic.jackut.model.User;

public class UserRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/UserDB.txt";

    public UserRepository() {

    }

    public void cleanUP() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.pathDB))) {
            writer.write(""); // limpa o arquivo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUserList() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.pathDB))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    User user = deserializeUser(line);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addUser(User newUser) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.pathDB, true))) {
            writer.write(serializeUser(newUser));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public void updateUser(User modifiedUser) {
        List<User> currentUsers = getUserList();
        boolean updated = false;

        for (int i = 0; i < currentUsers.size(); i++) {
            User existingUser = currentUsers.get(i);
            if (existingUser.getId().equals(modifiedUser.getId())) {
                currentUsers.set(i, modifiedUser);
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.pathDB))) {
                for (User user : currentUsers) {
                    writer.write(serializeUser(user));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUserList(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.pathDB))) {
            for (User user : users) {
                writer.write(serializeUser(user));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Serializa o User para o formato:
     * id;username;password;chave1=valor1;chave2=valor2;...
     */
    private String serializeUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(safe(user.getId())).append(";");
        sb.append(safe(user.getUsername())).append(";");
        sb.append(safe(user.getPassword()));

        for (Map.Entry<String, String> entry : user.getAttributes().entrySet()) {
            sb.append(";");
            sb.append(escape(entry.getKey())).append("=").append(escape(entry.getValue()));
        }
        return sb.toString();
    }

    /**
     * Desserializa uma linha para reconstruir o User e seus atributos.
     */
    private User deserializeUser(String line) {
        String[] parts = line.split(";");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Linha inválida no arquivo: " + line);
        }
        String id = parts[0];
        String username = parts[1];
        String password = parts[2];

        // Cria User com nome vazio só para inicializar
        User user = new User(username, password, "", id);

        // Pega atributos adicionais
        for (int i = 3; i < parts.length; i++) {
            String attr = parts[i];
            if (attr.contains("=")) {
                String[] kv = attr.split("=", 2);
                String key = unescape(kv[0]);
                String value = kv.length > 1 ? unescape(kv[1]) : "";
                user.setAttribute(key, value);
            }
        }
        return user;
    }

    /**
     * Evita NullPointerException (substitui null por string vazia).
     */
    private String safe(String value) {
        return value == null ? "" : value;
    }

    /**
     * Escapa ";" e "=" em chave/valor para evitar conflito.
     */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace(";", "\\;").replace("=", "\\=");
    }

    /**
     * Desserializa strings escapadas.
     */
    private String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";").replace("\\=", "=");
    }
}

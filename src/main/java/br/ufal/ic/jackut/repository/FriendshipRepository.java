package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Friendship;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FriendshipRepository {
    private final String pathDB = "./src/main/java/br/ufal/ic/jackut/database/FriendshipDB.txt"; // Mudei extensão para .ser só pra clareza, mas não é obrigatório

    /**
     * Método responsável por limpar o arquivo de persistência de dados
     */
    public void cleanUp() {
        saveFriendshipData(new Friendship());
    }

    /**
     * Método responsável por carregar a tabela de amizades
     * @return Retornar uma instância de Friendship, classe que gerencia as amizades existentes
     */
    public Friendship getFriendshipData() {
        try (FileInputStream fileIn = new FileInputStream(this.pathDB);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            Object obj = in.readObject();
            if (obj instanceof Friendship) {
                return (Friendship) obj;
            } else {
                return new Friendship(); // fallback se der ruim
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Friendship();
        }
    }

    /**
     * Método responsável por salvar a tabela de amizades
     * @param friendship Entidade que gerencia as amizades
     */
    public void saveFriendshipData(Friendship friendship) {
        try (FileOutputStream fileOut = new FileOutputStream(this.pathDB);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(friendship);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

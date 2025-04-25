package br.ufal.ic.jackut.exception.friendship;

public class RegisteredFriendshipException extends Exception {
    public RegisteredFriendshipException() {
        super("Usuário já está adicionado como amigo.");
    }
}

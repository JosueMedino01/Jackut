package br.ufal.ic.jackut.exception.friendship;

public class SelfFriendshipException extends Exception {
    public SelfFriendshipException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
}

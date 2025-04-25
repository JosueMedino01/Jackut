package br.ufal.ic.jackut.exception.friendship;

public class RegisteredInviteException extends Exception {
    public RegisteredInviteException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}

package br.ufal.ic.jackut.exception.friendship;

public class RegisteredFlirtingException extends Exception {
    public RegisteredFlirtingException() {
        super("Usuário já está adicionado como paquera.");
    }
}

package br.ufal.ic.jackut.exception.user;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException() {
        super("Login inválido.");
    }
}

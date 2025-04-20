package br.ufal.ic.jackut.exception.user;

public class InvalidSessionException extends Exception {
    public InvalidSessionException() {
        super("Login ou senha inválidos.");
    }
}

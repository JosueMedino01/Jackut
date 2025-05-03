package br.ufal.ic.jackut.exception.community;

public class RegisteredCommunityException extends Exception {
    public RegisteredCommunityException() {
        super("Comunidade com esse nome já existe.");
    }
}

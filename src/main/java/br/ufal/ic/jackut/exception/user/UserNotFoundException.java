package br.ufal.ic.jackut.exception.user;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Usuário não cadastrado.");
    }

    @Override
    public String toString() {
        return "Usuário não cadastrado.";
    }
}

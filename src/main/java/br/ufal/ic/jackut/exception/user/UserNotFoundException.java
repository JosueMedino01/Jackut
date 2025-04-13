package br.ufal.ic.jackut.exception.user;

public class UserNotFoundException extends Exception {
    UserNotFoundException() {
        super();
    }

    @Override
    public String toString() {
        return "Usuário não cadastrado.";
    }
}

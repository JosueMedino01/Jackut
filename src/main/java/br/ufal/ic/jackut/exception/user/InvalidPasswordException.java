package br.ufal.ic.jackut.exception.user;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException() {
        super("Senha inválida.");
    }
}

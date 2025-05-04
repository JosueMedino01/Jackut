package br.ufal.ic.jackut.exception.friendship;

public class RegisteredFanException extends Exception {
    public RegisteredFanException() {
        super("Usuário já está adicionado como ídolo.");
    } 
}

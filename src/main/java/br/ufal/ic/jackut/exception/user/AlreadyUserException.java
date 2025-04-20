package br.ufal.ic.jackut.exception.user;

public class AlreadyUserException  extends Exception{
    public AlreadyUserException() {
        super("Conta com esse nome já existe.");
    }
}

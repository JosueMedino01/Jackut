package br.ufal.ic.jackut.exception.message;

public class SelfMessageException extends Exception {
    public SelfMessageException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
}

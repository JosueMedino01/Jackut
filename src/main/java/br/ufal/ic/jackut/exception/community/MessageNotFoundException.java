package br.ufal.ic.jackut.exception.community;

public class MessageNotFoundException extends Exception {
    public MessageNotFoundException() {
        super("Não há mensagens.");
    }
}

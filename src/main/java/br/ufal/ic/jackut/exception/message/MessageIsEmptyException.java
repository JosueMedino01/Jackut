package br.ufal.ic.jackut.exception.message;

public class MessageIsEmptyException extends Exception {
    public MessageIsEmptyException() {
        super("Não há recados.");
    }
     
}
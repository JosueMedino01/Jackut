package br.ufal.ic.jackut.exception.friendship;

public class SelfFlirtingException extends Exception {
    public SelfFlirtingException() {
        super("Usuário não pode ser paquera de si mesmo.");
    }
}

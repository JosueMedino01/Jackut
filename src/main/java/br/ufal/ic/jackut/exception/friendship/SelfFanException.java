package br.ufal.ic.jackut.exception.friendship;

public class SelfFanException extends Exception {
    public SelfFanException() {
        super("Usuário não pode ser fã de si mesmo.");
    }
}

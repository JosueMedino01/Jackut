package br.ufal.ic.jackut.exception.friendship;

public class EnemyBlockException extends Exception {
    public EnemyBlockException(String name) {
        super("Função inválida: " + name + " é seu inimigo.");
    }
}

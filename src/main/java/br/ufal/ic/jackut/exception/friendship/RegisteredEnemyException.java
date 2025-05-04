package br.ufal.ic.jackut.exception.friendship;

public class RegisteredEnemyException extends Exception {
    public RegisteredEnemyException() {
        super("Usuário já está adicionado como inimigo.");
    }
}

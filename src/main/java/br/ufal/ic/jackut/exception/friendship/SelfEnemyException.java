package br.ufal.ic.jackut.exception.friendship;

public class SelfEnemyException extends Exception{
    public SelfEnemyException() {
        super("Usuário não pode ser inimigo de si mesmo.");
    }
}

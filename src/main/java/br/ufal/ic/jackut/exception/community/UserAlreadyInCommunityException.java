package br.ufal.ic.jackut.exception.community;

public class UserAlreadyInCommunityException extends Exception {
    public UserAlreadyInCommunityException() {
        super("Usuario já faz parte dessa comunidade.");
    }
}

package br.ufal.ic.jackut.exception.community;

public class CommunityNotFoundException extends Exception{
    public CommunityNotFoundException(){
        super("Comunidade não existe.");
    }
}

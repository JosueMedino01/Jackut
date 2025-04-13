package br.ufal.ic.jackut.facade;

import br.ufal.ic.jackut.service.UserService;

public class Facade {
    private final UserService userService;

    public Facade() {
        this.userService = new UserService();
    }

    public void zerarSistema(){
        this.userService.CleanUp();
    }

    public String getAtributoUsuario(String login, String atributo) {
        throw new RuntimeException("Usuário não cadastrado.");
    }
}

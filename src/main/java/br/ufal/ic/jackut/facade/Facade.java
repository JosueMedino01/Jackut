package br.ufal.ic.jackut.facade;

import br.ufal.ic.jackut.exception.user.AlreadyUserException;
import br.ufal.ic.jackut.exception.user.AttributeNotFillException;
import br.ufal.ic.jackut.exception.user.InvalidPasswordException;
import br.ufal.ic.jackut.exception.user.InvalidSessionException;
import br.ufal.ic.jackut.exception.user.InvalidUsernameException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.service.UserService;

public class Facade {
    private final UserService userService;

    public Facade() {
        this.userService = new UserService();
    }

    public void zerarSistema(){
        this.userService.CleanUp();
    }

    public String getAtributoUsuario(String username, String attribute) throws UserNotFoundException, AttributeNotFillException{
        return this.userService.getAtributeByUsername(username, attribute);
    }

    public void criarUsuario(String username, String password, String name) throws AlreadyUserException, InvalidUsernameException, InvalidPasswordException {
        this.userService.createUser(username, password, name);
    }

    public String abrirSessao(String username, String password) throws InvalidSessionException {
        return this.userService.openSession(username, password);
    }

    public void editarPerfil(String id, String attribute, String value) throws UserNotFoundException {
        this.userService.editProfile(id, attribute, value);
    }

    public void encerrarSistema() {

    }
}

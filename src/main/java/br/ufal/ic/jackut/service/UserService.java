package br.ufal.ic.jackut.service;

import java.util.List;
import java.util.UUID;

import br.ufal.ic.jackut.exception.user.AlreadyUserException;
import br.ufal.ic.jackut.exception.user.AttributeNotFillException;
import br.ufal.ic.jackut.exception.user.InvalidPasswordException;
import br.ufal.ic.jackut.exception.user.InvalidSessionException;
import br.ufal.ic.jackut.exception.user.InvalidUsernameException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.User;
import br.ufal.ic.jackut.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
   /*  private final CommunityService communityService; */
    public UserService() {
        this.userRepository = new UserRepository();
        /* this.communityService = new CommunityService(); */
    }
    
    /**
     * Método utilizado para limpar o arquivo de persistência de dados do usuário
     */
    public void cleanUP() {
        this.userRepository.cleanUP();
    }

    /**
     * Método utilizado para trazer as informações do usuário pelo Login/Username
     * @param username Login/Username cadastrado pelo usuário
     * @return Retorna a classe de usuário referente ao Login/Username informado
     * @throws UserNotFoundException Caso seja informado um Login/Username não cadastrado, retorna uma Exception
     */
    public User getUserByLogin(String username) {
        return this.userRepository.getUserList()
            .stream()
            .filter(e -> e.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    /**
     * Método utilizado para retornar um atributo pelo Login/Username do usuário
     * @param username Login/Username cadastrado pelo usuário
     * @param attribute Um atributo qualquer referente ao usuário
     * @return  Retorna o atributo passado referente ao usuário informado
     * @throws UserNotFoundException Em caso do usuário não estiver cadastrado, é retornado uma exceção
     * @throws AttributeNotFillException Caso o atributo passado não tenha sido preenchido, é retornado uma exceção 
     */
    public String getAtributeByUsername(String username, String attribute) throws UserNotFoundException, AttributeNotFillException {
        User user = this.getUserByLogin(username);

        if (user == null) 
            throw new UserNotFoundException();
        
        if(user.getAttribute(attribute) == null) {
            throw new AttributeNotFillException();
        }

        return user.getAttribute(attribute);
    }

    /**
     * Cria uma nova conta no sistema
     * @param username Login/Username do usuário
     * @param password Senha do usuário
     * @param name Nome do usuário
     * @throws AlreadyUserException Caso o Username/Login já estiver cadastrado
     * @throws InvalidUsernameException Caso o Username/Login seja vazio
     * @throws InvalidPasswordException Caso a senha seja vazia
     */
    public void createUser(String username, String password, String name) throws AlreadyUserException, InvalidUsernameException, InvalidPasswordException, UserNotFoundException {
        if(username == null || username.isEmpty()) {
            throw new InvalidUsernameException();
        }

        if(password == null || password.isEmpty()) {
            throw new InvalidPasswordException();
        }
        
        if(this.isRegistered(username)) {
            throw new AlreadyUserException();
        }
        
        String id = UUID.randomUUID().toString();
        User user = new User(username, password, name, id);

        this.userRepository.addUser(user);
    }

    /**
     * Método abre uma sessão para um usuário
     * @param username Login do usuário
     * @param password Senha do usuário
     * @return Retorna o ID referente ao usuário
     * @throws InvalidSessionException Caso login ou senha inválidos
     */
    public String openSession(String username, String password) throws InvalidSessionException {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidSessionException();
        }
        
        try {
            User user = this.getUserByLogin(username);
            if (user == null) 
                throw new UserNotFoundException();
                
            if (!user.getPassword().equals(password)) {
                throw new InvalidSessionException();
            }

            return user.getId();
        } catch (Exception e) {
            throw new InvalidSessionException();
        }
    }

    /**
     * Método utilizado para trazer as informações do usuário pelo ID
     * @param id ID do usuário
     * @return Retorna a classe de usuário referente ao ID informado
     * @throws UserNotFoundException
     */
    public User getUserById(String id) {
        return this.userRepository.getUserList()
            .stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    /**
     * Método edita um atributo do usuário
     * @param id ID do usuário
     * @param attribute Atributo a ser editado pelo usuário
     * @param value Novo valor do atributo
     * @throws UserNotFoundException Caso o usuário não esteja registrado
     */
    public void editProfile(String id, String attribute, String value) throws UserNotFoundException {
        User record = this.getUserById(id);
        
        if (record == null ) 
            throw new UserNotFoundException();

        record.setAttribute(attribute, value);
        
        this.updateUser(record);
    }

    /**
     * Método para saber se um usuário está cadastrado no sistema
     * @param idOrUsername ID ou Login do usuário
     * @return Retorna um boleano TRUE caso o usuário esteja cadastrado, caso contrário FALSE
     * @throws UserNotFoundException Caso o ID passado não esteja registrado
     */
    public boolean isRegistered(String idOrUsername){
        if (this.isUUID(idOrUsername)) {
            return (this.getUserById(idOrUsername) != null);
        }
         
        return (this.getUserByLogin(idOrUsername) != null);
    }

    public void dellProfile(String userId) throws UserNotFoundException{
        if (!this.isRegistered(userId))
            throw new UserNotFoundException();

        List<User> userList = this.userRepository.getUserList();
        userList.removeIf(user -> user.getId().equals(userId));

        this.userRepository.saveUserList(userList);
    }

    /**
     * Método usado para editar o usuário no arquivo de persistência
     * @param modifiedUser Login/Username do usuário modificado
     */
    private void updateUser(User modifiedUser) {
        this.userRepository.updateUser(modifiedUser);
    }

    /**
     * Método usado para saber se uma string é um UUID
     * @param value String a ser verificada
     * @return Retorna TRUE se a string for um UUID, e FALSE caso contrário
     */
    private boolean isUUID(String value) {
        return value != null && value.length() == 36 && value.contains("-");
    }
}
package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.ufal.ic.jackut.exception.community.CommunityNotFoundException;
import br.ufal.ic.jackut.exception.community.MessageNotFoundException;
import br.ufal.ic.jackut.exception.community.RegisteredCommunityException;
import br.ufal.ic.jackut.exception.community.UserAlreadyInCommunityException;
import br.ufal.ic.jackut.exception.message.MessageIsEmptyException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Community;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageStore;
import br.ufal.ic.jackut.repository.CommunityRepository;
import br.ufal.ic.jackut.repository.MessageRepository;

public class CommunityService {
    private CommunityRepository communityRepository;
    private UserService userService;
    private MessageRepository messageRepository;
    private MessageStore data;

    public CommunityService() {
        this.communityRepository = new CommunityRepository();
        this.userService = new UserService();
        this.messageRepository = new MessageRepository();
        this.data = this.messageRepository.get();
    }
    
    /**
     * Limpa o "banco de dados" das comunidades
     */
    public void cleanUp() {
        this.communityRepository.cleanUp();
    }

    /**
     * Cria uma comunidade
     * @param createrID ID do dono da comunidade
     * @param name Nome da comunidade 
     * @param description Descrição da comunidade 
     * @throws RegisteredCommunityException Caso a comunidade já esteja cadastrada
     */
    public void createCommunity(String createrID, String name, String description ) throws RegisteredCommunityException {
        if (this.getCommunityByName(name) != null) {
            throw new RegisteredCommunityException();
        }
        List<String> members = new ArrayList<String>(); members.add(createrID);
       
        this.addCommunity(
            new Community(createrID, name, description, members)
        );
    }

    /**
     * Método lista a descrição de uma comunidade 
     * @param name Nome da comunidade
     * @return  String com a descriçao da comunidade
     * @throws CommunityNotFoundException
     */
    public String getDescriptionByCommunityName(String name) throws CommunityNotFoundException {
        Community community = this.getCommunityByName(name);

        if (community == null) 
            throw new CommunityNotFoundException();

        return community.getDescription();
    }

    /**
     * Método que responsável por encontrar o dono da comunidade
     * @param name nome da comunidade 
     * @return retormar um usuário
     * @throws CommunityNotFoundException caso a comunidade não exista
     */
    public String getOwner(String name) throws CommunityNotFoundException {
        Community community = this.getCommunityByName(name);
        if (community == null) 
            throw new CommunityNotFoundException();
        
        return this.userService.getUserById(community.getOwnerId()).getUsername();
    }   
    /**
     * Método lista os membros de uma comunidade
     * @param name nome da comunidade
     * @return retorna uma string com os nomes dos membros da comunidade
     * @throws CommunityNotFoundException Caso a comunidade não exista
     */
    public String getMembers(String name) throws CommunityNotFoundException {
        Community community = this.getCommunityByName(name);

        if (community == null) 
            throw new CommunityNotFoundException();
        
        return this.formattedMemberList(community.getMembers());
    } 

    /**
     * Método lista as comunidades que um usuário é dono
     * @param username username/login de um usuário
     * @return lista de comunidades
     * @throws UserNotFoundException caso não seja encontrado o usuário
     */
    public String getCommunities(String username) throws UserNotFoundException {
        if (this.userService.getUserByLogin(username) == null) {
            throw new UserNotFoundException();
        }

        String userId = this.userService.getUserByLogin(username).getId();

        List<String> community = this.getCommunityList()
            .stream()
            .sorted((c1, c2) -> {
                int idx1 = c1.getMembers().indexOf(userId);
                int idx2 = c2.getMembers().indexOf(userId);
                return Integer.compare(idx1, idx2);
            })
            .filter(c -> c.getMembers().contains(userId))
            .map(Community::getName)
            .toList();

        return this.fomattedStringList(community);
    }
    
    /**
     * Método responsável por adicionar um novo membro a uma comunidade
     * @param userId ID do membro a ser adicionado
     * @param communityName Nome da comunidade que o membro será adicionado
     * @throws UserAlreadyInCommunityException Caso usuário já pertença a comunidade
     * @throws CommunityNotFoundException Caso a comunidade passada não esteja cadastrada
     * @throws UserNotFoundException Caso o usuário passado não esteja cadastrada
     */
    public void addMember(String userId, String communityName) throws UserAlreadyInCommunityException, CommunityNotFoundException, UserNotFoundException{
        List<Community> communities = this.getCommunityList();

        if (!this.userService.isRegistered(userId)) 
            throw new UserNotFoundException();

        for(Community community : communities) {
            if (community.getName().equals(communityName)) {
                
                List<String> members;
                members = community.getMembers(); 

                if(members.contains(userId)) 
                    throw new UserAlreadyInCommunityException();
                
                members.add(userId);

                community.setMembers(members);
                this.communityRepository.saveCommunityList(communities);
                return;
            }
        }

        throw new CommunityNotFoundException();
    }

    /**
     * Método que ler as mensagens das comunidades que um usuário faz parte
     * @param userId ID do usuário que vai ler a caixa de mensagens 
     * @return A mensagem mais antiga enviada por alguma das comunidades que o userID participa
     * @throws MessageNotFoundException Caso a caixa de mensagens esteja vazia
     */
    public String readMessage(String userId) throws MessageNotFoundException {
        Queue<Message> queue = this.data.getCommunityMessages().get(userId);
    
        if (queue == null || queue.isEmpty()) {
            throw new MessageNotFoundException();
        }

        Message msg = queue.poll();
        this.messageRepository.save(this.data);

        return msg.getMessage();
    }

    /**
     * Método responsável por enviar uma mensagem para todos da comunidade
     * @param userId ID do usuário que envia a mensagem
     * @param communityName Nome da comunidade 
     * @param message Mensagem a ser enviada
     * @throws CommunityNotFoundException Caso a comunidade não seja cadastrado
     * @throws UserNotFoundException Caso o usuário não seja cadastrado
     */
    public void sendMessage(String userId, String communityName, String message) 
        throws CommunityNotFoundException, UserNotFoundException 
    {
        if (!this.userService.isRegistered(userId))
            throw new UserNotFoundException();
       
        Message msg = new Message(userId, communityName, message);
        Community community = this.getCommunityByName(communityName);

        if (community == null) 
            throw new CommunityNotFoundException();
        
        for(String receptorId : community.getMembers()) {
            this.data.getCommunityMessages().putIfAbsent(receptorId, new LinkedList<>());
            this.data.getCommunityMessages().get(receptorId).add(msg);
        }

        this.messageRepository.save(this.data);
    }

    /**
     * Método que adiciona uma nova comunidade
     * @param community Uma entidade Comunidade
     */
    public void addCommunity(Community community) {
        List<Community> communities = getCommunityList();
        communities.add(community);
        this.communityRepository.saveCommunityList(communities);
    }

    /**
     * Método responsável por deletar uma comunidade pelo id do dono da comunidade
     * @param ownerId ID do usuário que criou a comunidade
     */
    public void removeCommunityByOwnerId(String ownerId) {
        List<Community> communities = getCommunityList();
        communities.removeIf(c -> c.getOwnerId().equals(ownerId));
        this.communityRepository.saveCommunityList(communities);
    }

    /**
     * Método responsável por buscar uma comunidade pelo nome
     * @param name Nome da comunidade
     * @return Uma comunidade
     */
    private Community getCommunityByName(String name) {
        return this.getCommunityList()
            .stream()
            .filter(community -> community.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * @return Método retorna uma lista de comunidades 
     */
    private List<Community> getCommunityList() {
        return this.communityRepository.getCommunityList();
    }

    /**
     * Método responsável por formatar uma listagem de IDs para o Username do usuário
     * @param list Uma lista de strings
     * @return Retorna uma string representando os Logins/Username dos usuários
     */
    private String formattedMemberList(List<String> memberList) {
        if (memberList == null) return "{}";

        memberList = memberList
            .stream()
            .map((String e) -> {
                return this.userService.getUserById(e).getUsername();
            })
            .toList();

        return memberList.toString().replace("[", "{").replace("]", "}").replace(" ", "");
    }

    /**
     * Método responsável por formatar uma listagem de acordo com 
     * o padrão que o EasyAccept espera
     * @param list Uma lista de strings
     * @return Retorna uma string representando a lista formatada
     */
    private String fomattedStringList(List<String> list) {
        if (list == null) return "{}";

        return list.toString().replace("[", "{").replace("]", "}").replace(", ", ",");
    }
}

package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import br.ufal.ic.jackut.exception.community.MessageNotFoundException;
import br.ufal.ic.jackut.exception.friendship.SelfFriendshipException;
import br.ufal.ic.jackut.exception.message.MessageIsEmptyException;
import br.ufal.ic.jackut.exception.message.SelfMessageException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageStore;
import br.ufal.ic.jackut.repository.MessageRepository;

public class MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private MessageStore data;

    public MessageService() {
        this.messageRepository = new MessageRepository();
        this.userService = new UserService();
        this.data = this.messageRepository.get();
    }
    /**
     * Método utilizado para limpar o arquivo de persistência de dados do usuário
     */
    public void cleanUp() {
        this.messageRepository.cleanUp();
    }
    
    /**
     * Método responsável por enviar mensagem ao usuário destinatário
     * @param broadcasterId ID do usuário que enviar/emite a mensagem 
     * @param receptorUsername ID do usuário que recebe/recepta a mensagem
     * @param message Mensagem enviada
     * @throws UserNotFoundException Caso um dos IDs informados não esteja cadastrado no sistema
     * @throws SelfMessageException Caso o usuário tenha enviado uma mensagem para ele mesmo
     */
    public void sendMessage(String broadcasterId, String receptorUsername, String message) throws UserNotFoundException, SelfMessageException{
        if (!this.userService.isRegistered(receptorUsername)) {
            throw new UserNotFoundException();
        }

        String receptorId = this.userService.getUserByLogin(receptorUsername).getId();
        
        if(
            !this.userService.isRegistered(broadcasterId) || 
            !this.userService.isRegistered(receptorId)
        ) {
            throw new UserNotFoundException();
        }

        if(broadcasterId.equals(receptorId)) {
            throw new SelfMessageException();
        }

        Message msg = new Message(broadcasterId, receptorId, message);
        this.data.getPrivateMessages().putIfAbsent(receptorId, new LinkedList<>());
        this.data.getPrivateMessages().get(receptorId).add(msg);
        this.onSave();
    }

    /**
     * Método responsável por carregar a mensagem mais antiga e remover ela da listagem
     * @param id ID do usuário que vai ler as mensagens
     * @return Retorna a mensagem mais antiga
     * @throws MessageIsEmptyException Caso a caixa de mensagens esteja vazia
     */
    public String readMessage(String id) throws MessageIsEmptyException{
        Queue<Message> queue = this.data.getPrivateMessages().get(id);
    
        if (queue == null || queue.isEmpty()) {
            throw new MessageIsEmptyException();
        }
        
        Message msg = queue.poll(); 
        
        this.onSave();

        return msg.getMessage();
    }
    
    private void onSave() {
        this.messageRepository.save(this.data);
    }

}

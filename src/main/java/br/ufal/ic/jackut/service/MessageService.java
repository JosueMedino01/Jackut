package br.ufal.ic.jackut.service;

import java.util.ArrayList;
import java.util.LinkedList;

import br.ufal.ic.jackut.exception.friendship.SelfFriendshipException;
import br.ufal.ic.jackut.exception.message.MessageIsEmptyException;
import br.ufal.ic.jackut.exception.message.SelfMessageException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageData;
import br.ufal.ic.jackut.repository.MessageRepository;

public class MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private MessageData data;

    public MessageService() {
        this.messageRepository = new MessageRepository();
        this.userService = new UserService();
        this.data = this.messageRepository.get();
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
        this.data.getTableMessage().putIfAbsent(receptorId, new LinkedList<>());
        this.data.getTableMessage().get(receptorId).add(msg);
        this.onSave();
    }

    /**
     * Método responsável por carregar a mensagem mais antiga e remover ela da listagem
     * @param id ID do usuário que vai ler as mensagens
     * @return Retorna a mensagem mais antiga
     * @throws MessageIsEmptyException Caso a caixa de mensagens esteja vazia
     */
    public String readMessage(String id) throws MessageIsEmptyException{
        Message msg = this.data.getTableMessage().get(id).poll();

        if(msg == null) {
            throw new MessageIsEmptyException();
        }
        
        this.onSave();

        return msg.getMessage();
    }
    
    private void onSave() {
        this.messageRepository.save(this.data);
    }

}

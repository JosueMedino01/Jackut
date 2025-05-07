package br.ufal.ic.jackut.service;


import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import br.ufal.ic.jackut.exception.friendship.EnemyBlockException;
import br.ufal.ic.jackut.exception.message.MessageIsEmptyException;
import br.ufal.ic.jackut.exception.message.SelfMessageException;
import br.ufal.ic.jackut.exception.user.UserNotFoundException;
import br.ufal.ic.jackut.model.Message;
import br.ufal.ic.jackut.model.MessageStore;
import br.ufal.ic.jackut.model.User;
import br.ufal.ic.jackut.repository.MessageRepository;

public class MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private FriendshipService friendshipService;

    public MessageService(FriendshipService friendshipService) {
        this.messageRepository = new MessageRepository();
        this.userService = new UserService();
        this.friendshipService = friendshipService;
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
    public void sendMessage(String broadcasterId, String receptorUsername, String message) 
        throws UserNotFoundException, SelfMessageException, EnemyBlockException
    {
        if (!this.userService.isRegistered(receptorUsername)) {
            throw new UserNotFoundException();
        }
        
        User receptor= this.userService.getUserByLogin(receptorUsername);

        if(this.friendshipService.isEnemy(receptor.getId(), broadcasterId, this.friendshipService.getFriendshipData().getEnemies())) {
            throw new EnemyBlockException(receptor.getAttribute("nome"));
        }

        if(!broadcasterId.equals("system")) {
            if(
                !this.userService.isRegistered(broadcasterId) || 
                !this.userService.isRegistered(receptor.getId())
            ) {
                throw new UserNotFoundException();
            }

            if(broadcasterId.equals(receptor.getId())) {
                throw new SelfMessageException();
            }
        }

        MessageStore ms = this.messageRepository.get();

        Message msg = new Message(broadcasterId, receptor.getId(), message);
        ms.getPrivateMessages().putIfAbsent(receptor.getId(), new LinkedList<>());
        ms.getPrivateMessages().get(receptor.getId()).add(msg);
        this.onSave(ms);
    }

    /**
     * Método responsável por carregar a mensagem mais antiga e remover ela da listagem
     * @param id ID do usuário que vai ler as mensagens
     * @return Retorna a mensagem mais antiga
     * @throws MessageIsEmptyException Caso a caixa de mensagens esteja vazia
     */
    public String readMessage(String id) throws MessageIsEmptyException{
        MessageStore record = this.messageRepository.get();
        Queue<Message> queue = record.getPrivateMessages().get(id);
    
        if (queue == null || queue.isEmpty()) {
            throw new MessageIsEmptyException();
        }
        
        Message msg = queue.poll(); 
        
        this.messageRepository.save(record);

        return msg.getMessage();
    }

    /**
     * Remove todas as mensagens que um usuário enviou
     * @param userId ID do usuário
     */
    public void deleteMessagesByUserId(String userId) {
        MessageStore record = this.messageRepository.get();
        Map<String, Queue<Message>> hashRecord = record.getPrivateMessages();

        for (Map.Entry<String, Queue<Message>> entry : hashRecord.entrySet()) {
            Queue<Message> messages = entry.getValue();
            messages.removeIf(message -> message.getBroadcasterId().equals(userId) || message.getReceptorId().equals(userId));
        }

        this.onSave(record);
    }
    
    /**
     * Salva no arquivo de persistencia os dados
     * @param ms Estrutura de mensagem a ser salvada no arquivo
     */
    private void onSave(MessageStore ms) {
        this.messageRepository.save(ms);
    }

}

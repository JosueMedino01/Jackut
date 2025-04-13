package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void CleanUp() {
        this.userRepository.CleanUp();
    }
}

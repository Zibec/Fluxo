package com.fluxo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import persistencia.jpa.usuario.UsuarioRepositoryImpl;
import usuario.Usuario;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositoryImpl userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> userOptional = this.userRepository.obterUsuarioPorNome(username);

        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("No user exists with username %s", username));
        }

        Usuario user = userOptional.get();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
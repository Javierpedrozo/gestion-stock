package com.traversol.gestion_stock.service;

import com.traversol.gestion_stock.model.Usuario;
import com.traversol.gestion_stock.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {
        if (loginInput == null || loginInput.trim().isEmpty()) {
            throw new UsernameNotFoundException("Login vacío");
        }
        Usuario usuario;
        if (loginInput.contains("@")) {
            usuario = usuarioRepository.findByEmail(loginInput)
                    .orElseThrow(() -> new UsernameNotFoundException("Email no encontrado: " + loginInput));
        } else {
            usuario = usuarioRepository.findByNombre(loginInput)
                    .orElseThrow(() -> new UsernameNotFoundException("Nombre no encontrado: " + loginInput));
        }
        return usuario;
    }

    // Nuevo: Expone findByEmail para usar en controladores (e.g., obtener usuario actual)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Opcional: Expone findByNombre si lo necesitas
    public Optional<Usuario> findByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }
}
package com.Maciel.Impressora.service;
import com.Maciel.Impressora.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

    void deletarUsuario(Long id);

    Optional<Usuario> obterPorId(Long id);
}

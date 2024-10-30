package com.Maciel.Impressora.service.impl;

import com.Maciel.Impressora.exception.ErroAutenticacao;
import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Usuario;
import com.Maciel.Impressora.model.repository.UsuarioRepository;
import com.Maciel.Impressora.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if(!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
        }

        if(!senha.equals(usuario.get().getSenha())) {
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }

    @Override
    public void deletarUsuario(Long id) {
        // Verifica se o usuário existe
        if (!repository.existsById(id)) {
            throw new RegraNegocioException("Usuário não encontrado com o ID: " + id);
        }
        // Realiza a deleção
        repository.deleteById(id);
    }


    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }

}

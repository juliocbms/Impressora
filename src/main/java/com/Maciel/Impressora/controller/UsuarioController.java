package com.Maciel.Impressora.controller;

import com.Maciel.Impressora.api.dto.UsuarioDTO;
import com.Maciel.Impressora.exception.ErroAutenticacao;
import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Usuario;
import com.Maciel.Impressora.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;


    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
        try{
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return  ResponseEntity.ok(usuarioAutenticado);
        }catch (ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto){

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .permissao(dto.getPermissao())
                .build();
        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return  new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.obterPorId(id);

        if (!usuario.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(usuario.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        try {
            service.deletarUsuario(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a deleção for bem-sucedida
        } catch (RegraNegocioException e) {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o usuário não existir
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request para outros erros
        }
    }


}
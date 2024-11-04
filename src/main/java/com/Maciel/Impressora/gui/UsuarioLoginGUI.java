package com.Maciel.Impressora.gui;

import com.Maciel.Impressora.ImpressoraApplication;
import com.Maciel.Impressora.api.dto.UsuarioDTO;
import com.Maciel.Impressora.model.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Anotação para configuração da aplicação Spring
public class UsuarioLoginGUI extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton, cadastrarButton;

    public UsuarioLoginGUI() {
        setTitle("Login de Usuário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos de entrada
        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; // Coluna
        gbc.gridy = 0; // Linha
        add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 25)); // Tamanho do input
        gbc.gridx = 1;
        add(emailField, gbc);

        // Campo de Senha
        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(senhaLabel, gbc);

        senhaField = new JPasswordField();
        senhaField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        add(senhaField, gbc);

        // Botão de Login
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(loginButton, gbc);

        // Eventos de Botão
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });

        setVisible(true);
    }

    // Função para autenticação
    private void autenticarUsuario() {
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());

        // Criar o objeto DTO com as informações de autenticação
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail(email); // Definindo o email
        dto.setSenha(senha); // Definindo a senha

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Usuario> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/usuarios/autenticar", dto, Usuario.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                MenuPrincipalGUI menuPrincipal = new MenuPrincipalGUI();
                menuPrincipal.setVisible(true); // Mostra a tela do menu
                dispose();
            }
        } catch (HttpClientErrorException e) {
            // Se a resposta do servidor foi um erro, você pode capturar a mensagem de erro do corpo da resposta
            String errorMessage = e.getResponseBodyAsString();
            JOptionPane.showMessageDialog(this, "Falha no login: " + errorMessage);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Iniciar a API em uma thread separada
        new Thread(() -> SpringApplication.run(ImpressoraApplication.class, args)).start();
        // Iniciar a interface gráfica
        SwingUtilities.invokeLater(() -> new UsuarioLoginGUI());
    }
}

package com.Maciel.Impressora.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipalGUI extends JFrame {
    public MenuPrincipalGUI() {
        setTitle("Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 450); // Tamanho fixo do JFrame
        setResizable(false); // Impede o redimensionamento

        // Configurar o layout do JFrame
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Criar um painel para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();

        // Botão Produtos
        JButton btnProduto = new JButton("Produtos");
        btnProduto.setFont(new Font("Arial", Font.BOLD, 14));
        buttonGbc.gridx = 0; // Coluna 0
        buttonGbc.gridy = 0; // Linha 0
        buttonGbc.insets = new Insets(10, 20, 10, 20); // Espaçamento ao redor do botão
        buttonPanel.add(btnProduto, buttonGbc);

        // Botão Etiquetas
        JButton btnEtiqueta = new JButton("Etiquetas");
        btnEtiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        buttonGbc.gridx = 1; // Coluna 1
        buttonGbc.gridy = 0; // Linha 0
        buttonPanel.add(btnEtiqueta, buttonGbc);

        // Adicionar o painel de botões ao JFrame
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        gbc.weightx = 1.0; // Preenche horizontalmente
        gbc.weighty = 1.0; // Preenche verticalmente
        gbc.anchor = GridBagConstraints.CENTER; // Centraliza o painel de botões
        add(buttonPanel, gbc);

        // Centralizar a janela na tela
        setLocationRelativeTo(null);
        setVisible(true); // Torna o JFrame visível

        btnProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ao clicar, abrir a tela de edição/cadastro de produtos
                new CadastroProdutosGUI(); // Aqui você chamaria a nova tela
                dispose(); // Fecha a tela atual, se necessário
            }
        });

        btnEtiqueta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para abrir a tela de etiquetas
               new EtiquetasGUI();
                dispose(); // Fecha a tela atual, se necessário
            }
        });
    }

    public static void main(String[] args) {
        new MenuPrincipalGUI();
    }
}

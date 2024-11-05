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
        setResizable(false);

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
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.insets = new Insets(10, 20, 10, 20);
        buttonPanel.add(btnProduto, buttonGbc);

        // Botão Etiquetas
        JButton btnEtiqueta = new JButton("Etiquetas");
        btnEtiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;
        buttonPanel.add(btnEtiqueta, buttonGbc);

        // Adicionar o painel de botões ao JFrame
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Centralizar a janela na tela
        setLocationRelativeTo(null);
        setVisible(true);

        btnProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroProdutosGUI();
                dispose();
            }
        });

        btnEtiqueta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new EtiquetasGUI();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new MenuPrincipalGUI();
    }
}

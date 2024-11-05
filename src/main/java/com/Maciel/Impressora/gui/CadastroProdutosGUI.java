package com.Maciel.Impressora.gui;

import com.Maciel.Impressora.api.dto.ProdutoDTO;
import com.Maciel.Impressora.model.enums.TipoCategoria;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CadastroProdutosGUI extends JFrame {
    private JTextField txtProdutoNome;
    private JTextField txtProdutoCodigo;
    private JTextField txtDescricao;
    private boolean isEditando = false;

    public CadastroProdutosGUI() {
        setTitle("Gerenciar Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 450);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVoltarMenu = new JButton(new ImageIcon(getClass().getResource("/imgs/icons8-voltar-20.png")));

// Remove o fundo, bordas e contorno de foco do botão
        btnVoltarMenu.setContentAreaFilled(false);
        btnVoltarMenu.setBorderPainted(false);
        btnVoltarMenu.setFocusPainted(false);
        btnVoltarMenu.setPreferredSize(new Dimension(20, 20));
        btnVoltarMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVoltarMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVoltarMenu.setPreferredSize(new Dimension(20, 20));
                btnVoltarMenu.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-voltar-25.png")));
                btnVoltarMenu.revalidate();
                btnVoltarMenu.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVoltarMenu.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-voltar-20.png")));
                btnVoltarMenu.setPreferredSize(new Dimension(20, 20));
                btnVoltarMenu.revalidate();
                btnVoltarMenu.repaint();


            }
        });
        topPanel.add(btnVoltarMenu);
        add(topPanel, BorderLayout.NORTH);
        setLocationRelativeTo(null);
        setVisible(true);
// Adiciona o botão ao painel
        topPanel.add(btnVoltarMenu);

        btnVoltarMenu.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });

        JButton btnLimparCampos = new JButton(new ImageIcon(getClass().getResource("/imgs/icons8-vassoura-30.png")));
        btnLimparCampos.setContentAreaFilled(false);
        btnLimparCampos.setBorderPainted(false);
        btnLimparCampos.setFocusPainted(false);
        btnLimparCampos.setPreferredSize(new Dimension(30, 30));
        btnLimparCampos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimparCampos.addActionListener(e -> limparCampos());

        btnLimparCampos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimparCampos.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-mov-vassoura-30.png")));
                btnLimparCampos.revalidate();
                btnLimparCampos.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLimparCampos.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-vassoura-30.png")));
                btnLimparCampos.revalidate();
                btnLimparCampos.repaint();
            }
        });

        JButton btnDeletar = new JButton(new ImageIcon(getClass().getResource("/imgs/icons8-lixo-30.png")));
        btnDeletar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeletar.setContentAreaFilled(false);
        btnDeletar.setBorderPainted(false);
        btnDeletar.setFocusPainted(false);
        btnDeletar.setPreferredSize(new Dimension(30, 30));

        btnDeletar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeletar.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-mov-lixo-30.png")));
                btnDeletar.revalidate();
                btnDeletar.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeletar.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-lixo-30.png")));
                btnDeletar.revalidate();
                btnDeletar.repaint();
            }
        });




        btnDeletar.addActionListener(e -> {
            String codigoProduto = txtProdutoCodigo.getText().trim();
            if (codigoProduto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, insira o código do produto para deletar.");
            } else {
                deletarProduto(codigoProduto);
            }
        });

        JLabel lblProdutoNome = new JLabel("Nome do Produto:");
        txtProdutoNome = new JTextField(20);
        txtProdutoNome.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
          buscarProdutoPorNome(txtProdutoNome.getText());
        }
        });


        JLabel lblProdutoCodigo = new JLabel("Código do Produto:");
        txtProdutoCodigo = new JTextField(20);
        txtProdutoCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProdutoPorCodigo(txtProdutoCodigo.getText());
            }
        });

        JLabel lblDescricao = new JLabel("Descrição:");
        txtDescricao = new JTextField(20);
        txtDescricao.setEditable(true);

        JButton btnSalvar = new JButton("Salvar Produto");
        btnSalvar.addActionListener(e -> {
            ProdutoDTO produtoDTO = new ProdutoDTO();
            produtoDTO.setNomeProduto(txtProdutoNome.getText().trim());
            produtoDTO.setDescricao(txtDescricao.getText().trim());

            try {
                Integer codigoProduto = Integer.parseInt(txtProdutoCodigo.getText().trim());
                produtoDTO.setCodigoProduto(codigoProduto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Código do produto deve ser um número inteiro.");
                return;
            }

            definirValoresFixos(produtoDTO);

            if (isEditando) {
                atualizarProduto(produtoDTO);
            } else {
                salvarProduto(produtoDTO);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(lblProdutoCodigo, gbc);
        gbc.gridx = 1; mainPanel.add(txtProdutoCodigo, gbc);
        gbc.gridx = 2; mainPanel.add(btnDeletar, gbc);
        gbc.gridx = 3; mainPanel.add(btnLimparCampos, gbc);
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(lblProdutoNome, gbc);
        gbc.gridx = 1; mainPanel.add(txtProdutoNome, gbc);
        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(lblDescricao, gbc);
        gbc.gridx = 1; mainPanel.add(txtDescricao, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; mainPanel.add(btnSalvar, gbc);


        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    private void limparCampos() {
        txtProdutoNome.setText("");
        txtProdutoCodigo.setText("");
        txtDescricao.setText("");
        isEditando = false;
    }

    private void buscarProdutoPorCodigo(String codigo) {
        RestTemplate restTemplate = new RestTemplate();
        txtDescricao.setEditable(false);
        try {
            ResponseEntity<ProdutoDTO> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produtos/buscarPorCodigo?codigoProduto=" + codigo, ProdutoDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ProdutoDTO produtoDTO = response.getBody();
                preencherCamposProduto(produtoDTO);
                isEditando = true;
            } else if (response.getStatusCode().is4xxClientError()) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado (Código: " + codigo + ").");
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produto: " + e.getMessage());
        }
    }

    private void deletarProduto(String codigo) {
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este produto?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Void> response = restTemplate.exchange(
                        "http://localhost:8080/api/produtos/deletar/" + codigo, HttpMethod.DELETE, null, Void.class);

                if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                    JOptionPane.showMessageDialog(this, "Produto deletado com sucesso!");
                    limparCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao deletar o produto.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar produto: " + e.getMessage());
            }
        }
    }


    private void buscarProdutoPorNome(String nome) {
        RestTemplate restTemplate = new RestTemplate();
        txtDescricao.setEditable(false);
        try {
            ResponseEntity<ProdutoDTO[]> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produtos/buscarPorNome?nomeProduto=" + nome, ProdutoDTO[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ProdutoDTO[] produtosDTO = response.getBody();
                if (produtosDTO != null && produtosDTO.length > 0) {
                    preencherCamposProduto(produtosDTO[0]);
                    isEditando = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Produto não encontrado.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produto: " + e.getMessage());
        }
    }

    private void preencherCamposProduto(ProdutoDTO produtoDTO) {
        txtProdutoNome.setText(produtoDTO.getNomeProduto());
        txtDescricao.setText(produtoDTO.getDescricao());
        txtDescricao.setEditable(true);
        isEditando = true; // Ativa o modo de edição
    }

    private void definirValoresFixos(ProdutoDTO produto) {
        produto.setRastreabilidade(2398472);
        produto.setDataProducao(LocalDate.parse("2024-10-22"));
        produto.setDataValidade(LocalDate.parse("2025-10-22"));
        produto.setPeso(BigDecimal.valueOf(5));
        produto.setTipo(TipoCategoria.EXTERNA);
    }

    private void salvarProduto(ProdutoDTO produto) {
        if (isEditando) {
            atualizarProduto(produto); // Chame o método de atualização
        } else {
            System.out.println("Nome: " + produto.getNomeProduto());
            System.out.println("Código: " + produto.getCodigoProduto());
            System.out.println("Descrição: " + produto.getDescricao());
            System.out.println("Rastreabilidade: " + produto.getRastreabilidade());
            System.out.println("Data de Produção: " + produto.getDataProducao());
            System.out.println("Data de Validade: " + produto.getDataValidade());
            System.out.println("Peso: " + produto.getPeso());
            System.out.println("Tipo: " + produto.getTipo());

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<ProdutoDTO> request = new HttpEntity<>(produto);
            ResponseEntity<Void> response = restTemplate.exchange(
                    "http://localhost:8080/api/produtos", HttpMethod.POST, request, Void.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar produto.");
            }
        }
    }

    private void atualizarProduto(ProdutoDTO produto) {
        System.out.println("Atualizando produto: " + produto.getCodigoProduto());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProdutoDTO> request = new HttpEntity<>(produto);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8080/api/produtos//atualizar/" + produto.getCodigoProduto(), HttpMethod.PUT, request, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            limparCampos(); // Limpa os campos após atualizar
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto.");
        }
    }

    public static void main(String[] args) {
        new CadastroProdutosGUI();
    }
}


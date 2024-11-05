package com.Maciel.Impressora.gui;

import com.Maciel.Impressora.api.dto.ProdutoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EtiquetasGUI extends JFrame {
    private JTextField txtProdutoNome;
    private JTextField txtProdutoCodigo;
    private JTextField txtDescricao;
    private JTextField txtRastreabilidade;
    private JTextField txtNumeroLayout;
    private JTextField txtLote;
    private JTextField txtDataProducao;
    private JTextField txtDataValidade;
    private JTextField txtPeso;

    public EtiquetasGUI() {
        setTitle("Imprimir Etiquetas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 450);
        setResizable(false);
        setLayout(new BorderLayout());

        // Painel superior com o botão de voltar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVoltarMenu = new JButton();
        btnVoltarMenu.setContentAreaFilled(false);
        btnVoltarMenu.setBorderPainted(false);
        btnVoltarMenu.setFocusPainted(false);
        btnVoltarMenu.setPreferredSize(new Dimension(20, 20));
        btnVoltarMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltarMenu.setIcon(new ImageIcon(getClass().getResource("/imgs/icons8-voltar-20.png")));

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

        btnVoltarMenu.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });

        topPanel.add(btnVoltarMenu);

        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cria um tamanho mínimo para os campos
        Dimension minSize = new Dimension(120, 20);

        // Linha 1
        JLabel lblCodigoProduto = new JLabel("Código do Porduto:");
        gbc.gridx = 0;  mainPanel.add(lblCodigoProduto, gbc);
        txtProdutoCodigo = new JTextField(20);
        txtProdutoCodigo.setMinimumSize(minSize);
        txtProdutoCodigo.setPreferredSize(minSize);
        gbc.gridx = 1; mainPanel.add(txtProdutoCodigo, gbc);
        txtProdutoCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProdutoPorCodigo(txtProdutoCodigo.getText());
            }
        });

        JLabel lblProdutoNome = new JLabel("Nome do Produto:");
        gbc.gridx = 2;  mainPanel.add(lblProdutoNome, gbc);
        txtProdutoNome = new JTextField(20);
        txtProdutoNome.setMinimumSize(minSize);
        txtProdutoNome.setPreferredSize(minSize);
        gbc.gridx = 3; mainPanel.add(txtProdutoNome, gbc);
        txtProdutoNome.setEditable(false);

        JLabel lblDescricao = new JLabel("Descrição:");
        gbc.gridx = 4;  mainPanel.add(lblDescricao, gbc);
        txtDescricao = new JTextField(20);
        txtDescricao.setMinimumSize(minSize);
        txtDescricao.setPreferredSize(minSize);
        gbc.gridx = 5; mainPanel.add(txtDescricao, gbc);
        txtDescricao.setEditable(false);

        // Linha 2
        JLabel lblRastreabilidade = new JLabel("Rastreabilidade:");
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(lblRastreabilidade, gbc);
        txtRastreabilidade = new JTextField(20);
        txtRastreabilidade.setMinimumSize(minSize);
        txtRastreabilidade.setPreferredSize(minSize);
        gbc.gridx = 1; mainPanel.add(txtRastreabilidade, gbc);

        JLabel lblPeso = new JLabel("Peso:");
        gbc.gridx = 2;  mainPanel.add(lblPeso, gbc);
        txtPeso = new JTextField(20);
        txtPeso.setMinimumSize(minSize);
        txtPeso.setPreferredSize(minSize);
        gbc.gridx = 3; mainPanel.add(txtPeso, gbc);

        JLabel lblLote = new JLabel("Lote:");
        gbc.gridx = 4;  mainPanel.add(lblLote, gbc);
        txtLote = new JTextField(20);
        txtLote.setMinimumSize(minSize);
        txtLote.setPreferredSize(minSize);
        gbc.gridx = 5; mainPanel.add(txtLote, gbc);

        // Linha 3
        JLabel lblDataproducao = new JLabel("Data de Produção:");
        gbc.gridx = 0; gbc.gridy = 2 ; mainPanel.add(lblDataproducao, gbc);
        txtDataProducao = new JTextField(20);
        txtDataProducao.setMinimumSize(minSize);
        txtDataProducao.setPreferredSize(minSize);
        gbc.gridx = 1; mainPanel.add(txtDataProducao, gbc);
        txtDataProducao.addActionListener(e -> calcularDataValidade());

        JLabel lblDataValidade = new JLabel("Data de Validade:");
        gbc.gridx = 2;  mainPanel.add(lblDataValidade, gbc);
        txtDataValidade = new JTextField(20);
        txtDataValidade.setMinimumSize(minSize);
        txtDataValidade.setPreferredSize(minSize);
        gbc.gridx = 3; mainPanel.add(txtDataValidade, gbc);

        JLabel lblNUmeroLayout = new JLabel("Número do Layout:");
        gbc.gridx = 4;  mainPanel.add(lblNUmeroLayout, gbc);
        txtNumeroLayout = new JTextField(20);
        txtNumeroLayout.setMinimumSize(minSize);
        txtNumeroLayout.setPreferredSize(minSize);
        gbc.gridx = 5; mainPanel.add(txtNumeroLayout, gbc);

        // Botão de Imprimir
        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener(e -> imprimirEtiqueta());



        gbc.gridx = 2; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(btnImprimir, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void buscarProdutoPorCodigo(String codigo) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<ProdutoDTO> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produtos/buscarPorCodigo?codigoProduto=" + codigo, ProdutoDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ProdutoDTO produtoDTO = response.getBody();
                preencherCamposProduto(produtoDTO);
            } else if (response.getStatusCode().is4xxClientError()) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado (Código: " + codigo + ").");
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado: ");
        }
    }

    private void preencherCamposProduto(ProdutoDTO produtoDTO) {
        txtProdutoNome.setText(produtoDTO.getNomeProduto());
        txtDescricao.setText(produtoDTO.getDescricao());
        // Outros campos permanecem vazios para o usuário preencher
    }

    private void calcularDataValidade() {
        String dataProducaoStr = txtDataProducao.getText().trim();
        if (!dataProducaoStr.isEmpty()) {
            try {
                // Converte a data de produção (ddMMyy) para um objeto Calendar
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
                Calendar dataProducao = Calendar.getInstance();
                dataProducao.setTime(sdf.parse(dataProducaoStr));

                // Adiciona 30 dias à data de produção
                dataProducao.add(Calendar.DAY_OF_MONTH, 30);

                // Define a data de validade no formato (ddMMyy)
                txtDataValidade.setText(sdf.format(dataProducao.getTime()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use ddMMyy.");
            }
        }
    }

    private void imprimirEtiqueta() {
        String numeroLayout = txtNumeroLayout.getText().trim();
        if (numeroLayout.isEmpty() || txtProdutoCodigo.getText().trim().isEmpty() ||
                txtProdutoNome.getText().trim().isEmpty() || txtDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Os campos 'Número do Layout', 'Código', 'Nome' e 'Descrição' são obrigatórios.");
            return;
        }

        // Obtém os dados necessários para impressão
        ProdutosGUI produtos = ProdutosGUI.builder()
                .codigoProduto(txtProdutoCodigo.getText().trim())
                .nomeProduto(txtProdutoNome.getText().trim())
                .descricao(txtDescricao.getText().trim())
                .rastreabilidade(txtRastreabilidade.getText().trim())
                .lote(txtLote.getText().trim())
                .dataProducao(txtDataProducao.getText().trim())
                .dataValidade(txtDataValidade.getText().trim())
                .peso(txtPeso.getText().trim())
                .build();

        // Chama o método de impressão do LayoutEtiqueta
        LayoutEtiqueta.imprimir(numeroLayout, produtos);
    }


    public static void main(String[] args) {
        new EtiquetasGUI();
    }
}

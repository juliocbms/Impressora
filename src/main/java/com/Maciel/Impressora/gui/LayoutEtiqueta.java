package com.Maciel.Impressora.gui;

import javax.print.*;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LayoutEtiqueta {

    public static void imprimir(String numeroLayout, ProdutosGUI produtos) {
        switch (numeroLayout) {
            case "1":
                imprimirLayout1(produtos);
                break;
            case "2":
                imprimirLayout2(produtos);
                break;
            // Adicione mais casos conforme necessário para outros layouts
            default:
                System.out.println("Número de layout inválido.");
                break;
        }
    }

    private static void imprimirLayout1(ProdutosGUI produtos) {
        String eanGTIN = "1" + produtos.getRastreabilidade().substring(0, 11);  // Supondo que getEAN retorne o código EAN
        String pesoLiquido = String.format("%06d", (int) (1000 * Double.parseDouble(produtos.getPeso()))); // peso_liquido * 1000 para formato esperado
        String numeroCaixa = "1232"; // Atribui valor direto para numeroCaixa
        // Supondo que getNumeroCaixa retorne o número da caixa
        // Formata o comando ZPL usando os dados da classe ProdutosGUI
        String comandoImpressao = "^XA" +
                "~JSN^XA" +
                "^COY,0^MTT^MD+0" +
                "^XZ" +
                "^XA" +
                "^PRC^FS" +
                // contornos
                "^FO7,27^GB665,1644,10,B^FS" +
                "^FO70,401^GB602,871,10,B^FS" +
                "^FO523,401^GB149,221,10,B^FS" +
                "^FO523,572^GB0,700,10,B^FS" +
                "^FO452,401^GB0,866,10,B^FS" +
                "^FO342,401^GB0,866,10,B^FS" +
                "^FO251,401^GB0,866,10,B^FS" +
                "^FO161,401^GB0,866,10,B^FS" +
                "^FO79,831^GB375,0,10,B^FS" +

                // produto
                "^FT627,460^FB977,1,,C^A0R,34,21^FH_^FD" + produtos.getNomeProduto() + "^FS" +
                "^FT584,460^FB977,1,,C^A0R,34,21^FH_^FD" + produtos.getDescricao() + "^FS" +


                // temperatura
                "^FT478,550^FB632,1,,C^A0R,34,32^FH^FDMANTER RESFRIADO A 18 _f8C^FS" +

                // cod. produto
                "^FT573,419^A0R,75,64^FD" + produtos.getCodigoProduto() + "^FS" +

                // data produção
                "^FT420,415^A0R,34,25^FH^FDDATA DE PRODU_80_C70:^FS" +
                "^FT370,600^A0R,50,48^FD" + produtos.getDataProducao() + "^FS" +

                // data validade
                "^FT420,850^A0R,34,25^FDDATA DE VALIDADE:^FS" +
                "^FT370,1020^A0R,50,48^FD" + produtos.getDataValidade() + "^FS" +

                // rastreabilidade
                "^FT304,415^A0R,34,25^FDRASTREABILIDADE:^FS" +
                "^FT277,626^A0R,58,35^FD" + produtos.getRastreabilidade() + "^FS" +

                // tipo
                "^FT304,847^A0R,34,25^FH^FDNUMERO CAIXA:^FS" +
                "^FT277,1038^A0R,58,51^FD" + produtos.getLote() + 1 + "^FS" + // substitua por um valor real se necessário

                // quantidade
                "^FT217,415^A0R,34,25^FDQTDE:^FS" +
                "^FT190,626^A0R,58,51^FD1^FS" +

                // lote
                "^FT217,847^A0R,34,25^FDLOTE:^FS" +
                "^FT190,908^A0R,58,49^FD" + produtos.getLote() + "^FS" +

                // peso líquido
                "^FT124,415^A0R,34,25^FH^FDPESO L_d6QUIDO:^FS" +
                "^FT98,626^A0R,58,51^FD" + produtos.getPeso() + " Kg^FS" +

                // peso bruto
                "^FT124,847^A0R,34,25^FDPESO BRUTO:^FS" +
                "^FT98,1038^A0R,58,51^FD"+ (Integer.parseInt(produtos.getPeso()) + 1) + "Kg^FS" +

                // Código de barras
                // Código de barras principal
                "^BY2,2.5^FS" +
                "^FT118,339^BCN,118,N,N,Y^FD>;>801" + verificadorEAN128(eanGTIN) +
                "3103" + pesoLiquido +
                "21" + numeroCaixa + "^FS" +
                "^FT143,363^A0N,28,23^FD(01)" + verificadorEAN128(eanGTIN) +
                "(3103)" + pesoLiquido +
                "(21)" + numeroCaixa + "^FS" +

                // Outro código de barras
                "^BY2,2.5^FS" +
                "^FT118,176^BCN,118,N,N,Y^FD>;>801" + verificadorEAN128(eanGTIN) +
                "3103" + pesoLiquido +
                "21" + numeroCaixa + "^FS" +
                "^FT143,200^A0N,28,23^FD(01)" + verificadorEAN128(eanGTIN) +
                "(3103)" + pesoLiquido +
                "(21)" + numeroCaixa + "^FS" +

                // Código de barras inferior
                "^BY2,2.5^FS" +
                "^FT118,1588^BCN,118,N,N,Y^FD>;>801" + verificadorEAN128(eanGTIN) +
                "3103" + pesoLiquido +
                "21" + numeroCaixa + "^FS" +
                "^FT143,1612^A0N,28,23^FD(01)" + verificadorEAN128(eanGTIN) +
                "(3103)" + pesoLiquido +
                "(21)" + numeroCaixa + "^FS" +




                // Configuração de impressão
                "ISSTRNWARE,N^FS;" +
                "^XZ" +
                "^XA" +
                "^ILSTRNWARE^FS" +
                "^PF0^FS" +
                "^PQ1,0,0,Y" +
                "^XZ";

        salvarComoZPL(comandoImpressao);
    }

    private static void imprimirLayout2(ProdutosGUI produtos) {
        // Exemplo de processamento de dados para layout 2 usando ProdutosGUI
        String comandoImpressao = "^XA" +
                "^FO50,50^A0N,50,50^FDProduto: " + produtos.getNomeProduto() + "^FS" +
                "^FO50,120^A0N,50,50^FDCódigo: " + produtos.getCodigoProduto() + "^FS" +
                "^FO50,190^A0N,50,50^FDDescrição: " + produtos.getDescricao() + "^FS" +
                "^XZ";

        salvarComoZPL(comandoImpressao);
    }

    // Método que calcula o dígito verificador de um EAN-13
    private static String verificadorEAN128(String ean) {
        if (ean.length() != 12) {
            throw new IllegalArgumentException("O código EAN deve ter 12 dígitos para calcular o dígito verificador.");
        }
        int soma = 0;
        for (int i = 0; i < ean.length(); i++) {
            int digito = Character.getNumericValue(ean.charAt(i));
            soma += (i % 2 == 0) ? digito : digito * 3;
        }
        int verificador = (10 - (soma % 10)) % 10;
        return ean + verificador;
    }


    private static void salvarComoZPL(String comandoImpressao) {
        String caminhoFixo = "C:\\Users\\julio\\OneDrive\\Área de Trabalho\\Nova pasta/etiqueta.txt"; // Caminho fixo para salvar o arquivo

        try (FileOutputStream fos = new FileOutputStream(new File(caminhoFixo))) {
            fos.write(comandoImpressao.getBytes());
            fos.flush();
            System.out.println("Etiqueta salva em: " + caminhoFixo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar etiqueta: " + e.getMessage());
        }
    }
}

package com.Maciel.Impressora.gui;

import javax.swing.*;
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
            default:
                System.out.println("Número de layout inválido.");
                break;
        }
    }

    private static void imprimirLayout1(ProdutosGUI produtos) {
        StringBuilder comandoImpressao = new StringBuilder();
        String eanGTIN = "1" + produtos.getRastreabilidade().substring(0, 11);
        String pesoLiquido = String.format("%06d", (int) (1000 * Double.parseDouble(produtos.getPeso())));
        String numeroCaixa = "1232";

        // Comando ZPL com StringBuilder
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("~JSN^XA\n");
        comandoImpressao.append("^COY,0^MTT^MD+0\n");
        comandoImpressao.append("^XZ\n");
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("^PRC^FS\n");

        // Contornos
        comandoImpressao.append("^FO7,27^GB665,1644,10,B^FS\n");
        comandoImpressao.append("^FO70,401^GB602,871,10,B^FS\n");
        comandoImpressao.append("^FO523,401^GB149,221,10,B^FS\n");
        comandoImpressao.append("^FO523,572^GB0,700,10,B^FS\n");
        comandoImpressao.append("^FO452,401^GB0,866,10,B^FS\n");
        comandoImpressao.append("^FO342,401^GB0,866,10,B^FS\n");
        comandoImpressao.append("^FO251,401^GB0,866,10,B^FS\n");
        comandoImpressao.append("^FO161,401^GB0,866,10,B^FS\n");
        comandoImpressao.append("^FO79,831^GB375,0,10,B^FS\n");

        // Produto
        comandoImpressao.append("^FT627,460^FB977,1,,C^A0R,34,21^FH_^FD").append(produtos.getNomeProduto()).append("^FS\n");
        comandoImpressao.append("^FT584,460^FB977,1,,C^A0R,34,21^FH_^FD").append(produtos.getDescricao()).append("^FS\n");

        // Temperatura
        comandoImpressao.append("^FT478,550^FB632,1,,C^A0R,34,32^FH^FDMANTER RESFRIADO A 18 _f8C^FS\n");

        // Código do Produto
        comandoImpressao.append("^FT573,419^A0R,75,64^FD").append(produtos.getCodigoProduto()).append("^FS\n");

        // Data de Produção
        comandoImpressao.append("^FT420,415^A0R,34,25^FH^FDDATA DE PRODU_80_C70:^FS\n");
        comandoImpressao.append("^FT370,600^A0R,50,48^FD").append(produtos.getDataProducao()).append("^FS\n");

        // Data de Validade
        comandoImpressao.append("^FT420,850^A0R,34,25^FDDATA DE VALIDADE:^FS\n");
        comandoImpressao.append("^FT370,1020^A0R,50,48^FD").append(produtos.getDataValidade()).append("^FS\n");

        // Rastreabilidade
        comandoImpressao.append("^FT304,415^A0R,34,25^FDRASTREABILIDADE:^FS\n");
        comandoImpressao.append("^FT277,626^A0R,58,35^FD").append(produtos.getRastreabilidade()).append("^FS\n");

        // Tipo
        comandoImpressao.append("^FT304,847^A0R,34,25^FH^FDNUMERO CAIXA:^FS\n");
        comandoImpressao.append("^FT277,1038^A0R,58,51^FD").append(produtos.getLote()).append(1).append("^FS\n");

        // Quantidade
        comandoImpressao.append("^FT217,415^A0R,34,25^FDQTDE:^FS\n");
        comandoImpressao.append("^FT190,626^A0R,58,51^FD1^FS\n");

        // Lote
        comandoImpressao.append("^FT217,847^A0R,34,25^FDLOTE:^FS\n");
        comandoImpressao.append("^FT190,908^A0R,58,49^FD").append(produtos.getLote()).append("^FS\n");

        // Peso Líquido
        comandoImpressao.append("^FT124,415^A0R,34,25^FH^FDPESO L_d6QUIDO:^FS\n");
        comandoImpressao.append("^FT98,626^A0R,58,51^FD").append(produtos.getPeso()).append(" Kg^FS\n");

        // Peso Bruto
        comandoImpressao.append("^FT124,847^A0R,34,25^FDPESO BRUTO:^FS\n");
        comandoImpressao.append("^FT98,1038^A0R,58,51^FD").append((Integer.parseInt(produtos.getPeso()) + 1)).append(" Kg^FS\n");

        // Código de Barras Principal
        comandoImpressao.append("^BY2,2.5^FS\n");
        comandoImpressao.append("^FT118,339^BCN,118,N,N,Y^FD>;>801").append(verificadorEAN128(eanGTIN)).append("3103").append(pesoLiquido).append("21").append(numeroCaixa).append("^FS\n");

        // Código de Barras Inferior
        comandoImpressao.append("^BY2,2.5^FS\n");
        comandoImpressao.append("^FT118,1588^BCN,118,N,N,Y^FD>;>801").append(verificadorEAN128(eanGTIN)).append("3103").append(pesoLiquido).append("21").append(numeroCaixa).append("^FS\n");

        comandoImpressao.append("^XZ\n");

        salvarComoZPL(comandoImpressao.toString());
    }

    private static void imprimirLayout2(ProdutosGUI produtos) {
        StringBuilder comandoImpressao = new StringBuilder();



        comandoImpressao.append("~JSN^XA\n");
        comandoImpressao.append("^COY,0^MTT^MD+0\n");
        comandoImpressao.append("^XZ\n");
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("^PRB^FS\n");


        //SIF IMG
        comandoImpressao.append("~DGEtiquetaVerificacao,17542,049,\n");
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("^PRB^FS\n");
        comandoImpressao.append("^MNY^FS\n");


        //SIF IMG CHMD
        comandoImpressao.append("^FO321,474^GB392,0,358,W^FS\n");
        comandoImpressao.append("^FO321,474^XGSIF,1,1^FS\n");


        //FILIAL
        comandoImpressao.append("^FT442,738^A0N,62,79^FD1294^FS\n");

        //CONTORNOS
        comandoImpressao.append("^FO21,184^GB673,221,2,B^FS\n");
        comandoImpressao.append("^FO21,227^GB168,0,2,B^FS\n");
        comandoImpressao.append("^FO21,328^GB673,0,2,B^FS\n");
        comandoImpressao.append("^FO21,271^GB672,0,2,B^FS\n");
        comandoImpressao.append("^FO188,185^GB0,144,2,B^FS\n");
        comandoImpressao.append("^FO239,330^GB0,75,2,B^FS\n");
        comandoImpressao.append("^FO331,272^GB0,56,2,B^FS\n");
        comandoImpressao.append("^FO472,328^GB0,75,2,B^FS\n");
        comandoImpressao.append("^FO524,272^GB0,58,2,B^FS\n");


        //ENDEREÇO
        comandoImpressao.append("^FT389,55^A0N,27,31^FDETIQUETA VERIFICACAO^FS\n");
        comandoImpressao.append("^FT340,75^A0N,15,13^FH_^FDUNIDADE DE BENEFICIAMENTO DE CARNE E PRODUTOS C_b5RNEOS^FS\n");
        comandoImpressao.append("^FT367,95^A0N,18,17^FH^FDAv. da Recupera_87_c6o, 7380, Dois Irm_c6os^FS\n");
        comandoImpressao.append("^FT362,113^A0N,18,17^FDRecife/PE - Brasil -  CEP: 52.171-340^FS\n");
        comandoImpressao.append("^FT342,130^A0N,18,17^FDSAC: 0800.281.3333 - sac@masterboi.com.br^FS\n");
        comandoImpressao.append("^FT408,150^A0N,18,17^FDCNPJ: 03.721.769/0002-78^FS\n");

        //TITULO
        comandoImpressao.append("^FT197,224^A0N,36,23^FH_^FD^FS\n");
        comandoImpressao.append("^FT197,256^A0N,36,23^FH_^FD^FS\n");

        //COD DO PRODUTO
        comandoImpressao.append("^FT35,210^A0N,19,19^FDCOD.").append(produtos.getCodigoProduto()).append("^FS\n");


        comandoImpressao.append("//MASTERBOI NOME\n");
        comandoImpressao.append("^FT35,256^A0N,19,19^FD^FS\n");

        //CONSERVAÇÃO
        comandoImpressao.append("^FT35,295^A0N,19,19^FH^FDCONSERVA_80_c7O:^FS\n");
        comandoImpressao.append("^FT35,318^A0N,17,17^FH^FD0 _f8C A 7 _f8C^FS\n");

        //DATA DE ABATE
        comandoImpressao.append("^FT194,292^A0N,19,19^FDDATA DE ABATE:^FS\n");
        comandoImpressao.append("^FT206,321^A0N,29,22^FD").append(produtos.getDataProducao()).append("^FS\n");

        //LOTE
        comandoImpressao.append("^FT490,360^A0N,22,21^FDLOTE:^FS\n");
        comandoImpressao.append("^FT490,390^A0N,27,26^FD").append(produtos.getLote()).append("^FS\n");

        //DATA DE VALIDADE
        comandoImpressao.append("^^FT530,294^A0N,19,19^FDDATA DE VALIDADE:^FS\n");
        comandoImpressao.append("^FT555,323^A0N,29,22^FD").append(produtos.getDataValidade()).append("^FS\n");

        //RASTREABILIDADE
        comandoImpressao.append("^FT35,360^A0N,22,21^FDRASTREABILIDADE:^FS\n");
        comandoImpressao.append("^FT35,392^A0N,27,26^FD").append(produtos.getRastreabilidade()).append("^FS\n");

        //DATA EMBALAGEM
        comandoImpressao.append("^FT337,293^A0N,19,19^FDDATA DE EMBALAGEM:^FS\n");
        comandoImpressao.append("^FT374,321^A0N,29,22^FD121224^FS\n");

        //PESO DA EMBALAGEM
        comandoImpressao.append("^FT257,360^A0N,22,21^FDPESO DA EMBALAGEM:^FS\n");
        comandoImpressao.append("^FT257,390^A0N,27,26^FD").append(produtos.getPeso()).append("^FS\n");

        //REGISTRO SIF
        comandoImpressao.append("^FT361,439^A0N,22,21^FH^FDRegistro no Minist_82rio da Agricultura^FS\n");
        comandoImpressao.append("^FT397,460^A0N,22,21^FH^FDSIF/DIPOA sob n_f8^FS\n");

        //NÃO CONTÉM GLÚTEN
        comandoImpressao.append("^FT94,657^A0N,18,17^FH^FDN_c7O CONT_90M GL_e9TEN^FS\n");

        //INDÚSTRIA BRASILEIRA
        comandoImpressao.append("^FT357,868^A0N,37,42^FH^FDInd_a3stria Brasileira^FS\n");

        //COD EAN
        comandoImpressao.append("^BY3,3.0^FS\n");
        comandoImpressao.append("^FT50,494^BEN,64,Y,N^FD213231312^FS\n");

        //QR CODE
        comandoImpressao.append("^FT100,850^BQN,2,5\n");
        comandoImpressao.append("^FH\\^FDMA,https://www.instagram.com/masterboibrasil/^FS\n");


        comandoImpressao.append("^ISSTRNWARE,N^FS\n");
        comandoImpressao.append("^XZ\n");
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("^ILSTRNWARE^FS\n");
        comandoImpressao.append("^PF0^FS\n");
        comandoImpressao.append("^PQ,0,0,Y\n");
        comandoImpressao.append("^^XZ\n");
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("^IDAAAAAAAA^FS\n");
        comandoImpressao.append("^XZ\n");
        comandoImpressao.append("^XA\n");
        comandoImpressao.append("^IDSTRNWARE\n");
        comandoImpressao.append("^XZ\n");

        if (produtos.getCodigoProduto().equals("2197")){
            comandoImpressao.append("~JSN^XA\n");
            comandoImpressao.append("^COY,0^MTT^MD+0\n");
            comandoImpressao.append("^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^PRB^FS\n");


            //SIF IMG
            comandoImpressao.append("~DGEtiquetaDerivada1,17542,049,\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^PRB^FS\n");
            comandoImpressao.append("^MNY^FS\n");


            //SIF IMG CHMD
            comandoImpressao.append("^FO321,474^GB392,0,358,W^FS\n");
            comandoImpressao.append("^FO321,474^XGSIF,1,1^FS\n");


            //FILIAL
            comandoImpressao.append("^FT442,738^A0N,62,79^FD1294^FS\n");

            //CONTORNOS
            comandoImpressao.append("^FO21,184^GB673,221,2,B^FS\n");
            comandoImpressao.append("^FO21,227^GB168,0,2,B^FS\n");
            comandoImpressao.append("^FO21,328^GB673,0,2,B^FS\n");
            comandoImpressao.append("^FO21,271^GB672,0,2,B^FS\n");
            comandoImpressao.append("^FO188,185^GB0,144,2,B^FS\n");
            comandoImpressao.append("^FO239,330^GB0,75,2,B^FS\n");
            comandoImpressao.append("^FO331,272^GB0,56,2,B^FS\n");
            comandoImpressao.append("^FO472,328^GB0,75,2,B^FS\n");
            comandoImpressao.append("^FO524,272^GB0,58,2,B^FS\n");


            //ENDEREÇO
            comandoImpressao.append("^FT389,55^A0N,27,31^FDETIQUETA ADC 1^FS\n");
            comandoImpressao.append("^FT340,75^A0N,15,13^FH_^FDUNIDADE DE BENEFICIAMENTO DE CARNE E PRODUTOS C_b5RNEOS^FS\n");
            comandoImpressao.append("^FT367,95^A0N,18,17^FH^FDAv. da Recupera_87_c6o, 7380, Dois Irm_c6os^FS\n");
            comandoImpressao.append("^FT362,113^A0N,18,17^FDRecife/PE - Brasil -  CEP: 52.171-340^FS\n");
            comandoImpressao.append("^FT342,130^A0N,18,17^FDSAC: 0800.281.3333 - sac@masterboi.com.br^FS\n");
            comandoImpressao.append("^FT408,150^A0N,18,17^FDCNPJ: 03.721.769/0002-78^FS\n");

            //TITULO
            comandoImpressao.append("^FT197,224^A0N,36,23^FH_^FD^FS\n");
            comandoImpressao.append("^FT197,256^A0N,36,23^FH_^FD^FS\n");

            //COD DO PRODUTO
            comandoImpressao.append("^FT35,210^A0N,19,19^FDCOD.^FS\n");


            comandoImpressao.append("//MASTERBOI NOME\n");
            comandoImpressao.append("^FT35,256^A0N,19,19^FD^FS\n");

            //CONSERVAÇÃO
            comandoImpressao.append("^FT35,295^A0N,19,19^FH^FDCONSERVA_80_c7O:^FS\n");
            comandoImpressao.append("^FT35,318^A0N,17,17^FH^FD0 _f8C A 7 _f8C^FS\n");

            //DATA DE ABATE
            comandoImpressao.append("^FT194,292^A0N,19,19^FDDATA DE ABATE:^FS\n");
            comandoImpressao.append("^FT206,321^A0N,29,22^FD^FS\n");

            //LOTE
            comandoImpressao.append("^FT490,360^A0N,22,21^FDLOTE:^FS\n");
            comandoImpressao.append("^FT490,390^A0N,27,26^FD^FS\n");

            //DATA DE VALIDADE
            comandoImpressao.append("^^FT530,294^A0N,19,19^FDDATA DE VALIDADE:^FS\n");
            comandoImpressao.append("^FT555,323^A0N,29,22^FD^FS\n");

            //RASTREABILIDADE
            comandoImpressao.append("^FT35,360^A0N,22,21^FDRASTREABILIDADE:^FS\n");
            comandoImpressao.append("^FT35,392^A0N,27,26^FD^FS\n");

            //DATA EMBALAGEM
            comandoImpressao.append("^FT337,293^A0N,19,19^FDDATA DE EMBALAGEM:^FS\n");
            comandoImpressao.append("^FT374,321^A0N,29,22^FD121224^FS\n");

            //PESO DA EMBALAGEM
            comandoImpressao.append("^FT257,360^A0N,22,21^FDPESO DA EMBALAGEM:^FS\n");
            comandoImpressao.append("^FT257,390^A0N,27,26^FD^FS\n");

            //REGISTRO SIF
            comandoImpressao.append("^FT361,439^A0N,22,21^FH^FDRegistro no Minist_82rio da Agricultura^FS\n");
            comandoImpressao.append("^FT397,460^A0N,22,21^FH^FDSIF/DIPOA sob n_f8^FS\n");

            //NÃO CONTÉM GLÚTEN
            comandoImpressao.append("^FT94,657^A0N,18,17^FH^FDN_c7O CONT_90M GL_e9TEN^FS\n");

            //INDÚSTRIA BRASILEIRA
            comandoImpressao.append("^FT357,868^A0N,37,42^FH^FDInd_a3stria Brasileira^FS\n");

            //COD EAN
            comandoImpressao.append("^BY3,3.0^FS\n");
            comandoImpressao.append("^FT50,494^BEN,64,Y,N^FD213231312^FS\n");

            //QR CODE
            comandoImpressao.append("^FT100,850^BQN,2,5\n");
            comandoImpressao.append("^FH\\^FDMA,https://www.instagram.com/masterboibrasil/^FS\n");

            comandoImpressao.append("^ISSTRNWARE,N^FS\n");
            comandoImpressao.append("^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^ILSTRNWARE^FS\n");
            comandoImpressao.append("^PF0^FS\n");
            comandoImpressao.append("^PQ,0,0,Y\n");
            comandoImpressao.append("^^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^IDAAAAAAAA^FS\n");
            comandoImpressao.append("^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^IDSTRNWARE\n");
            comandoImpressao.append("^XZ\n");

            comandoImpressao.append("~JSN^XA\n");
            comandoImpressao.append("^COY,0^MTT^MD+0\n");
            comandoImpressao.append("^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^PRB^FS\n");

            //SIF IMG
            comandoImpressao.append("~DGEtiquetaDerivada2,17542,049,\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^PRB^FS\n");
            comandoImpressao.append("^MNY^FS\n");


            //SIF IMG CHMD
            comandoImpressao.append("^FO321,474^GB392,0,358,W^FS\n");
            comandoImpressao.append("^FO321,474^SIF,1,1^FS\n");


            //FILIAL
            comandoImpressao.append("^FT442,738^A0N,62,79^FD1294^FS\n");

            //CONTORNOS
            comandoImpressao.append("^FO21,184^GB673,221,2,B^FS\n");
            comandoImpressao.append("^FO21,227^GB168,0,2,B^FS\n");
            comandoImpressao.append("^FO21,328^GB673,0,2,B^FS\n");
            comandoImpressao.append("^FO21,271^GB672,0,2,B^FS\n");
            comandoImpressao.append("^FO188,185^GB0,144,2,B^FS\n");
            comandoImpressao.append("^FO239,330^GB0,75,2,B^FS\n");
            comandoImpressao.append("^FO331,272^GB0,56,2,B^FS\n");
            comandoImpressao.append("^FO472,328^GB0,75,2,B^FS\n");
            comandoImpressao.append("^FO524,272^GB0,58,2,B^FS\n");


            //ENDEREÇO
            comandoImpressao.append("^FT389,55^A0N,27,31^FDETIQUETA ADC 2^FS\n");
            comandoImpressao.append("^FT340,75^A0N,15,13^FH_^FDUNIDADE DE BENEFICIAMENTO DE CARNE E PRODUTOS C_b5RNEOS^FS\n");
            comandoImpressao.append("^FT367,95^A0N,18,17^FH^FDAv. da Recupera_87_c6o, 7380, Dois Irm_c6os^FS\n");
            comandoImpressao.append("^FT362,113^A0N,18,17^FDRecife/PE - Brasil -  CEP: 52.171-340^FS\n");
            comandoImpressao.append("^FT342,130^A0N,18,17^FDSAC: 0800.281.3333 - sac@masterboi.com.br^FS\n");
            comandoImpressao.append("^FT408,150^A0N,18,17^FDCNPJ: 03.721.769/0002-78^FS\n");

            //TITULO
            comandoImpressao.append("^FT197,224^A0N,36,23^FH_^FD^FS\n");
            comandoImpressao.append("^FT197,256^A0N,36,23^FH_^FD^FS\n");

            //COD DO PRODUTO
            comandoImpressao.append("^FT35,210^A0N,19,19^FDCOD.").append(produtos.getCodigoProduto()).append("^FS\n");


            comandoImpressao.append("//MASTERBOI NOME\n");
            comandoImpressao.append("^FT35,256^A0N,19,19^FD^FS\n");

            //CONSERVAÇÃO
            comandoImpressao.append("^FT35,295^A0N,19,19^FH^FDCONSERVA_80_c7O:^FS\n");
            comandoImpressao.append("^FT35,318^A0N,17,17^FH^FD0 _f8C A 7 _f8C^FS\n");

            //DATA DE ABATE
            comandoImpressao.append("^FT194,292^A0N,19,19^FDDATA DE ABATE:^FS\n");
            comandoImpressao.append("^FT206,321^A0N,29,22^FD").append(produtos.getDataProducao()).append("^FS\n");

            //LOTE
            comandoImpressao.append("^FT490,360^A0N,22,21^FDLOTE:^FS\n");
            comandoImpressao.append("^FT490,390^A0N,27,26^FD").append(produtos.getLote()).append("^FS\n");

            //DATA DE VALIDADE
            comandoImpressao.append("^^FT530,294^A0N,19,19^FDDATA DE VALIDADE:^FS\n");
            comandoImpressao.append("^FT555,323^A0N,29,22^FD").append(produtos.getDataValidade()).append("^FS\n");

            //RASTREABILIDADE
            comandoImpressao.append("^FT35,360^A0N,22,21^FDRASTREABILIDADE:^FS\n");
            comandoImpressao.append("^FT35,392^A0N,27,26^FD^FS\n");

            //DATA EMBALAGEM
            comandoImpressao.append("^FT337,293^A0N,19,19^FDDATA DE EMBALAGEM:^FS\n");
            comandoImpressao.append("^FT374,321^A0N,29,22^FD121224^FS\n");

            //PESO DA EMBALAGEM
            comandoImpressao.append("^FT257,360^A0N,22,21^FDPESO DA EMBALAGEM:^FS\n");
            comandoImpressao.append("^FT257,390^A0N,27,26^FD^FS\n");

            //REGISTRO SIF
            comandoImpressao.append("^FT361,439^A0N,22,21^FH^FDRegistro no Minist_82rio da Agricultura^FS\n");
            comandoImpressao.append("^FT397,460^A0N,22,21^FH^FDSIF/DIPOA sob n_f8^FS\n");

            //NÃO CONTÉM GLÚTEN
            comandoImpressao.append("^FT94,657^A0N,18,17^FH^FDN_c7O CONT_90M GL_e9TEN^FS\n");

            //INDÚSTRIA BRASILEIRA
            comandoImpressao.append("^FT357,868^A0N,37,42^FH^FDInd_a3stria Brasileira^FS\n");

            //COD EAN
            comandoImpressao.append("^BY3,3.0^FS\n");
            comandoImpressao.append("^FT50,494^BEN,64,Y,N^FD213231312^FS\n");

            //QR CODE
            comandoImpressao.append("^FT100,850^BQN,2,5\n");
            comandoImpressao.append("^FH\\^FDMA,https://www.instagram.com/masterboibrasil/^FS\n");

            comandoImpressao.append("^ISSTRNWARE,N^FS\n");
            comandoImpressao.append("^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^ILSTRNWARE^FS\n");
            comandoImpressao.append("^PF0^FS\n");
            comandoImpressao.append("^PQ,0,0,Y\n");
            comandoImpressao.append("^^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^IDAAAAAAAA^FS\n");
            comandoImpressao.append("^XZ\n");
            comandoImpressao.append("^XA\n");
            comandoImpressao.append("^IDSTRNWARE\n");
            comandoImpressao.append("^XZ\n");
        }


        salvarComoZPL(comandoImpressao.toString());
    }

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

    private static void salvarComoZPL(String comandocomandoImpressao) {
        String caminhoFixo = "C:\\Users\\julio\\OneDrive\\Área de Trabalho\\Nova pasta/etiqueta.txt";

        try (FileOutputStream fos = new FileOutputStream(new File(caminhoFixo))) {
            fos.write(comandocomandoImpressao.getBytes());
            fos.flush();
            System.out.println("Etiqueta salva em: " + caminhoFixo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar etiqueta: " + e.getMessage());
        }
    }
}

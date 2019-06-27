/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.carregarBD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author vinicius.pinheiro
 */
public class LeitorDeArquivo implements ILeitorDeArquivo {
FileReader arq;

    @Override
    public String getFileName() {
        Scanner ler = new Scanner(System.in);
        System.out.printf("Informe o nome de arquivo texto:\n");
        String filename= ler.nextLine();
        return filename; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BufferedReader openfile(String fileName) {
        
        BufferedReader lerArq = null;
        try {
            arq = new FileReader(fileName);
            lerArq = new BufferedReader(arq);
        } catch (FileNotFoundException ex) {
                    System.err.printf("Erro ao achar o arquivo: %s.\n",
          ex.getMessage());
        }
     return lerArq;
    }

    @Override
    public String readfile(BufferedReader lerArq) {
        String linha = null;
        try {
            linha = lerArq.readLine();
        } catch (IOException ex) {
             System.err.printf("Erro na leitura do arquivo: %s.\n",
             ex.getMessage());
        }
        return linha;
    }

    @Override
    public void closeArquive() {
        try {
            arq.close();
        } catch (IOException ex) {
                               System.err.printf("Erro ao fechar do arquivo: %s.\n",
          ex.getMessage());
        }
    }
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.carregarBD;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 *
 * @author vinicius.pinheiro
 */
public interface ILeitorDeArquivo {
    
    /**
     *
     * @return
     */
    String getFileName();

    /**
     *
     * @param fileName
     * @return
     */
    BufferedReader openfile(String fileName);

    /**
     *
     * @param lerArq
     * @return
     */
    String readfile(BufferedReader lerArq);

    /**
     *
     * @param arq
     */
    void closeArquive();
    
}

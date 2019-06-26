/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author vinicius.pinheiro
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class Configuracao {
        private File arquivo_propriedades;
    private Properties propriedades;
    private final String nome_arquivo_configuracao;

    private final Boolean utilizar_chave_alternativa_ao_criptografar;

    public Configuracao() throws Exception {
        nome_arquivo_configuracao = "configuracao.properties";
        utilizar_chave_alternativa_ao_criptografar = false;
        inicializar(false);
    }
    
    public Configuracao(String nome_arquivo_configuracao, Boolean criar_arquivo) throws Exception {
        this.nome_arquivo_configuracao = nome_arquivo_configuracao;
        utilizar_chave_alternativa_ao_criptografar = false;
        inicializar(criar_arquivo);
    }
    
    public Configuracao(String nome_arquivo_configuracao, 
            Boolean criar_arquivo, Boolean utilizar_chave_alternativa_ao_criptografar) throws Exception {
        this.nome_arquivo_configuracao = nome_arquivo_configuracao;
        this.utilizar_chave_alternativa_ao_criptografar = utilizar_chave_alternativa_ao_criptografar;
        inicializar(criar_arquivo);
    }
     
    public Configuracao(String nome_arquivo_configuracao) throws Exception {
        this.nome_arquivo_configuracao = nome_arquivo_configuracao;
        utilizar_chave_alternativa_ao_criptografar = false;
        inicializar(false);
    }

    private void inicializar(Boolean criar_arquivo) throws Exception {
        propriedades = new Properties();
        carregar(criar_arquivo);
    }

     public void carregar(Boolean criar_arquivo) throws Exception {
        InputStream fis;

        arquivo_propriedades = new File(nome_arquivo_configuracao);
        if (arquivo_propriedades.exists()) {
            fis = new FileInputStream(arquivo_propriedades);
        } else {
            //Recurso imbutido no arquivo (JAR ou WAR)
            fis = getClass().getClassLoader().
                    getResourceAsStream(nome_arquivo_configuracao);
        }

        if (fis != null) {
            propriedades.load(fis);
            fis.close();
        } else {
            if (criar_arquivo) {
               Files.createDirectories(Paths.get(arquivo_propriedades.getParent()));
               if (arquivo_propriedades.createNewFile() == false) {
                     throw new Exception("Não foi possível criar o arquivo de configuração '" + nome_arquivo_configuracao + "'");
               } else {
                   fis = new FileInputStream(arquivo_propriedades);
                   propriedades.load(fis);
                   fis.close();
               }
            } else {
                throw new Exception("Arquivo de configuração '" + nome_arquivo_configuracao + "' não encontrado.");
            }
        }
    }
     
    public void salvar() throws Exception {
        try (FileOutputStream fos = new FileOutputStream(arquivo_propriedades)) {
            propriedades.store(fos, "");
        }
    }
    
      public void definirPropriedade(String nomePropriedade,
            String conteudoPropriedade) throws Exception {
        if (conteudoPropriedade == null) {
            propriedades.setProperty(nomePropriedade, "");
        } else {
            propriedades.setProperty(nomePropriedade, 
                    Criptografia.criptografarTexto(conteudoPropriedade, 
                    (utilizar_chave_alternativa_ao_criptografar? 
                    Criptografia.CHAVE_ALTERNATIVA :
                    Criptografia.CHAVE)));
        }
    }

    public String obterPropriedade(String nome_propriedade) throws Exception {
        String conteudo = propriedades.getProperty(nome_propriedade);
        if (Optional.ofNullable(conteudo).orElse("").isEmpty() == false) {
            try {
                return Criptografia.descriptografarTexto(conteudo, Criptografia.CHAVE);
            } catch (Exception ex1) {
                try {
                    return Criptografia.descriptografarTexto(conteudo, 
                           Criptografia.CHAVE_ALTERNATIVA);
                } catch(Exception ex2) {
                    return "";    
                }
            }
        } else {
            return "";
        }
    }

    public Map<String, String> obterLista(String prefixo) throws Exception {
        Map<String, String> lista = new HashMap<>();

        for (Enumeration<Object> e = propriedades.keys(); e.hasMoreElements();) {
            String nome_propriedade = (String) e.nextElement();
            if (nome_propriedade.contains(prefixo)) {
                String conteudo = propriedades.getProperty(nome_propriedade);
                if (Optional.ofNullable(conteudo).orElse("").isEmpty() == false) {
                    try {
                        conteudo = Criptografia.descriptografarTexto(
                                conteudo, Criptografia.CHAVE);
                    } catch (Exception ex1) {
                        try {
                            conteudo = Criptografia.descriptografarTexto(
                                    conteudo, Criptografia.CHAVE_ALTERNATIVA);
                        } catch (Exception ex2) {
                            conteudo = "";
                        }
                    }

                    lista.put(nome_propriedade, conteudo);
                } else {
                    lista.put(nome_propriedade, "");
                }
            }
        }
        return lista;
    }

    public void removerLista(String prefixo) throws Exception {
        for (Enumeration<Object> e = propriedades.keys(); e.hasMoreElements();) {
            String nome_propriedade = (String) e.nextElement();
            if (nome_propriedade.contains(prefixo)) {
                propriedades.remove(nome_propriedade);
            }
        }
    }
}


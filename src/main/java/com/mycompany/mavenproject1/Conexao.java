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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class Conexao {

    private Connection conexao_bd;
    private String provedor;
    private String banco_dados_servidor_endereco;
    private String banco_dados_servidor_porta;
    private String banco_dados_nome;
    private String banco_dados_usuario_nome;
    private String banco_dados_usuario_senha;
    private String banco_dados_utiliza_ssl;
    private String texto_conexao;
    private Configuracao config;

    public Conexao() throws Exception {
        inicializar("ns2e.properties");
    }

    public Conexao(String arquivo_configuracao) throws Exception {
        inicializar(arquivo_configuracao);
    }

    public Conexao(String provedor, String texto_conexao) throws Exception {
        this.provedor = provedor;
        this.texto_conexao = texto_conexao;
        abrir();
    }
    public Conexao(String provedor, String texto_conexao, String usuario, String senha) throws Exception {
        this.provedor = provedor;
        this.texto_conexao = texto_conexao;
        this.banco_dados_usuario_nome = usuario;
        this.banco_dados_usuario_senha = senha;
        abrir();
    }
    
    public Conexao(String provedor, String banco_dados_servidor_endereco,
            String banco_dados_servidor_porta,
            String banco_dados_nome,
            String banco_dados_usuario_nome,
            String banco_dados_usuario_senha) throws Exception {
        this.provedor = provedor;
        this.banco_dados_servidor_endereco = banco_dados_servidor_endereco;
        this.banco_dados_servidor_porta = banco_dados_servidor_porta;
        this.banco_dados_nome = banco_dados_nome;
        this.banco_dados_usuario_nome = banco_dados_usuario_nome;
        this.banco_dados_usuario_senha = banco_dados_usuario_senha;
        abrir();
    }

    private void inicializar(String arquivo_configuracao) throws Exception {
        config = new Configuracao(arquivo_configuracao);
        provedor = config.obterPropriedade("banco_dados_provedor");
        banco_dados_servidor_endereco = config.obterPropriedade("banco_dados_servidor_endereco");
        banco_dados_servidor_porta = config.obterPropriedade("banco_dados_servidor_porta");
        banco_dados_nome = config.obterPropriedade("banco_dados_nome");
        banco_dados_usuario_nome = config.obterPropriedade("banco_dados_usuario_nome");
        banco_dados_usuario_senha = config.obterPropriedade("banco_dados_usuario_senha");
        banco_dados_utiliza_ssl = config.obterPropriedade("banco_dados_utiliza_ssl");
        abrir();
    }

    @Override
    protected void finalize() throws Throwable {
        fechar();
        super.finalize();
    }

    public String getProvedor() {
        return provedor;
    }

    public Connection getConexaoBD() {
        return conexao_bd;
    }

    private Connection obterConexao() throws Exception {
        switch (provedor) {
            case "postgresql":
                Class.forName("org.postgresql.Driver").newInstance();
                if (texto_conexao != null && texto_conexao.isEmpty() == false) {
                    String texto_conexao_temp = texto_conexao;
                    if (texto_conexao_temp.startsWith("jdbc:postgresql:") == false) {
                        texto_conexao_temp = "jdbc:postgresql:" + texto_conexao_temp;
                    }
                    return DriverManager.getConnection(texto_conexao_temp);
                }
                return DriverManager.getConnection("jdbc:postgresql:"
                        + "//" + banco_dados_servidor_endereco
                        + ":" + banco_dados_servidor_porta
                        + "/" + banco_dados_nome
                        + "?user=" + banco_dados_usuario_nome
                        + "&password=" + banco_dados_usuario_senha
                        + ("S".equalsIgnoreCase(
                                banco_dados_utiliza_ssl) ? "&ssl=true" : ""));
            case "odbc":
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
                if (texto_conexao != null && texto_conexao.isEmpty() == false) {
                    String texto_conexao_temp = texto_conexao;
                    if (texto_conexao_temp.startsWith("jdbc:odbc:") == false) {
                        texto_conexao_temp = "jdbc:odbc:" + texto_conexao_temp;
                    }
                    return DriverManager.getConnection(texto_conexao_temp);
                }
                throw new Exception("Montagem de conexão para ODBC não implementado.");
                
                case "derby":
                
                if (texto_conexao != null && texto_conexao.isEmpty() == false) {
                    Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                    String texto_conexao_temp = texto_conexao;
                    if (texto_conexao_temp.startsWith("jdbc:derby:") == false) {
                        texto_conexao_temp = "jdbc:derby:" + texto_conexao_temp;
                    }
                    return DriverManager.getConnection(texto_conexao_temp);
                }
                
                if(Optional.ofNullable(banco_dados_servidor_endereco).orElse("").isEmpty() && 
                   Optional.ofNullable(banco_dados_servidor_porta).orElse("").isEmpty()){
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
                    return DriverManager.getConnection("jdbc:derby:"
                        + banco_dados_nome, banco_dados_usuario_nome,
                        banco_dados_usuario_senha);
                }
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                return DriverManager.getConnection("jdbc:derby:"
                        + "//" + banco_dados_servidor_endereco
                        + ":" + banco_dados_servidor_porta
                        + "/" + banco_dados_nome
                        ,banco_dados_usuario_nome
                        ,banco_dados_usuario_senha);
        }

        throw new Exception("Provedor não informado ou não implementado. Nenhuma conexão criada.");
    }

    public final void abrir() throws Exception {
        conexao_bd = obterConexao();
    }

    public void fechar() {
        try {
            if (conexao_bd != null && !conexao_bd.isClosed()) {
                conexao_bd.close();
                if ("derby".equals(provedor) && 
                     Optional.ofNullable(banco_dados_servidor_endereco).orElse("").isEmpty()) {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                }
            }
        } catch (SQLException excessao) {

        }
    }

    public void iniciarTransacao() throws SQLException {
        conexao_bd.setAutoCommit(false);
    }

    public void confirmarTransacao() throws SQLException {
        if (conexao_bd != null && !conexao_bd.isClosed()) {
            conexao_bd.commit();
            conexao_bd.setAutoCommit(true);
        }
    }

    public void reverterTransacao() throws SQLException {
        if (conexao_bd != null && !conexao_bd.isClosed()) {
            conexao_bd.rollback();
            conexao_bd.setAutoCommit(true);
        }
    }

    public void setProvedor(String provedor) {
        this.provedor = provedor;
    }

    public Boolean emTransacao() throws SQLException {
        return !conexao_bd.getAutoCommit();
    }

    /**
     * Modificar o banco de dados da conexão atual
     * <p style="font-weight:bold;color:red;">Obs: A conexão atual será fechada
     * e será criado um novo objeto de conexão</p>
     *
     * @param banco_dados_nome Nome do novo banco de dados a ser acessado
     * @throws SQLException
     */
    public void modificarBancoDados(String banco_dados_nome) throws Exception {
        if (conexao_bd != null && !conexao_bd.isClosed()) {
            conexao_bd.close();
            this.banco_dados_nome = banco_dados_nome;
            conexao_bd = obterConexao();
        }
    }

    /**
     * Cria uma nova conexão de banco de dados utilizando informações da conexão
     * atual
     *
     * @param banco_dados_nome Nome do novo banco de dados a ser acessado. Se
     * não for informado será utilizado o mesmo banco de dados da conexão atual.
     * @return
     * @throws SQLException
     */
    public Conexao clonar(String banco_dados_nome) throws Exception {
        Conexao conexao = new Conexao(
                provedor,
                banco_dados_servidor_endereco,
                banco_dados_servidor_porta,
                banco_dados_nome == null || banco_dados_nome.isEmpty()
                ? this.banco_dados_nome : banco_dados_nome,
                banco_dados_usuario_nome,
                banco_dados_usuario_senha);
        return conexao;
    }
}

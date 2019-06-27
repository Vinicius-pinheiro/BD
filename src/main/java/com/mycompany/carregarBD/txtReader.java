/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.CarregarBD;
import br.com.ns2e.comum.bd.Conexao;
import com.mycompany.carregarBD.LeitorDeArquivo;
import com.mycompany.carregarBD.Empresas_brasileiras;
import java.io.BufferedReader;

public class txtReader {
    
    public static void main(String[] args) throws Exception {
   
   Empresas_brasileiras empresas_brasileiras = new Empresas_brasileiras();
        
        LeitorDeArquivo leitor= new LeitorDeArquivo();
        String filename = leitor.getFileName();
        BufferedReader lerArq = leitor.openfile(filename);
        String linha = leitor.readfile(lerArq);
        
      while (linha != null) {
        empresas_brasileiras.setCnpj(linha.substring(4,18));
        empresas_brasileiras.setId_matriz_filial(linha.substring(18,19)); 
        empresas_brasileiras.setNome_empresarial(linha.substring(19,169)); 
        empresas_brasileiras.setNome_fantasia(linha.substring(169,224));
        empresas_brasileiras.setSituacao_cadastral(linha.substring(224,226));
        empresas_brasileiras.setData_situacao_cadastral(linha.substring(226,234)); 
        empresas_brasileiras.setMotivo_situacao_cadastral(linha.substring(234, 236)); 
        empresas_brasileiras.setNome_cidade_exterior(linha.substring(236, 291)); 
        empresas_brasileiras.setCodigo_pais(linha.substring(291, 294));
        empresas_brasileiras.setNome_pais(linha.substring(294, 364));
        empresas_brasileiras.setCodigo_natureza_juridica(linha.substring(364, 368));
        empresas_brasileiras.setData_inicio_atividade(linha.substring(368, 376));
        empresas_brasileiras.setCnae_principal(linha.substring(376, 383));
        empresas_brasileiras.setTipo_logradouro(linha.substring(383, 403));
        empresas_brasileiras.setLogradouro(linha.substring(403, 463));
        empresas_brasileiras.setNumero(linha.substring(463, 469));
        empresas_brasileiras.setComplemento(linha.substring(469, 625));
        empresas_brasileiras.setBairro(linha.substring(625, 675));
        empresas_brasileiras.setCep(linha.substring(675, 683));
        empresas_brasileiras.setUf(linha.substring(683, 685));
        empresas_brasileiras.setCodigo_tom(linha.substring(685, 689));
        empresas_brasileiras.setMunicipio(linha.substring(689, 739));
        empresas_brasileiras.setTelefone1(linha.substring(739, 751));
        empresas_brasileiras.setTelefone2(linha.substring(751, 763));
        empresas_brasileiras.setFax(linha.substring(763, 775));
        empresas_brasileiras.setEndereco_e_mail(linha.substring(775, 890));
        empresas_brasileiras.setQualificacao_responsavel(linha.substring(890, 892));
        empresas_brasileiras.setCapital_social(linha.substring(892, 906));
        empresas_brasileiras.setPorte_empresa(linha.substring(906, 908));
        empresas_brasileiras.setOpcao_simples_nacional(linha.substring(908, 909));
        empresas_brasileiras.setData_opcao_simples_nacional(linha.substring(909, 917));
        empresas_brasileiras.setData_exclusao_simples_nacional(linha.substring(917, 925));
        empresas_brasileiras.setOpcao_mei(linha.substring(925, 926));
        empresas_brasileiras.setSituacao_especial(linha.substring(926, 949));
        empresas_brasileiras.setData_situacao_especial(linha.substring(949, 957));

        System.out.printf(empresas_brasileiras.cnpj + "\n");
        linha = lerArq.readLine(); // lê da segunda até a última linha
      }
    leitor.closeArquive();
    System.out.println();
  }
}
    
//\\SRV04\Projetos\EmpresasReceita\F.K032001K.D81106D.txt
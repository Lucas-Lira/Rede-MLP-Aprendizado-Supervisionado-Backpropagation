package Modulos;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

public class FuncoesGerais
{
    public static ArrayList<ArrayList<Double>> abrirArquivo(Registro reg /* Primeira Coluna da Tabela */, ArrayList<String> list_str /* Coluna das Classes */, int[] camadas)
    {
        ArrayList<RegMaiorMenor> list_maior_menor = new ArrayList();
        ArrayList<ArrayList<Double>> list = new ArrayList();
        int camada_entrada = 0;
        int camada_saida = 0;
        boolean sair = false;
        try
        {
            FileChooser fc;
            fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

            fc.setTitle("Abrir Arquivo CSV");
            File arq = fc.showOpenDialog(null);

            if(arq != null)
            {
                String linha = "";
                RandomAccessFile rf = new RandomAccessFile(arq.getAbsolutePath(), "r");
                String[] vetValores;
                int qtde_col;
                
                linha = rf.readLine();
                if(!linha.isEmpty())
                {
                    vetValores = new String[46];
                    String[] vet_aux = linha.split(",");
                    for(int i = 0; i < 46; i++)
                    {
                        if(i < vet_aux.length)
                            vetValores[i] = vet_aux[i];
                        else
                            vetValores[i] = "";
                    }
                    
                    qtde_col = vet_aux.length;
                    
                    if(qtde_col > 1)
                    {
                        camada_entrada = qtde_col - 1;
                        
                        list_maior_menor = new ArrayList();
                        for(int i = 0; i < qtde_col - 1; i++)
                            list_maior_menor.add(new RegMaiorMenor());

                        //reg = new Registro();
                        reg.setTotal(vetValores[0], vetValores[1], vetValores[2], vetValores[3], vetValores[4], vetValores[5], vetValores[6], vetValores[7], vetValores[8], vetValores[9], 
                                vetValores[10], vetValores[11], vetValores[12], vetValores[13], vetValores[14], vetValores[15], vetValores[16], vetValores[17], vetValores[18], vetValores[19], 
                                vetValores[20], vetValores[21], vetValores[22], vetValores[23], vetValores[24], vetValores[25], vetValores[26], vetValores[27], vetValores[28], 
                                vetValores[29], vetValores[30], vetValores[31], vetValores[32], vetValores[33], vetValores[34], vetValores[35], vetValores[36], vetValores[37], 
                                vetValores[38], vetValores[39], vetValores[40], vetValores[41], vetValores[42], vetValores[43], vetValores[44], vetValores[45]);

                        ArrayList<Double> list_aux;
                        int cont_leitura = 0;
                        while(rf.getFilePointer() < rf.length() && !sair)
                        {
                            linha = rf.readLine();
                            vet_aux = linha.split(",");
                            list_aux = new ArrayList();

                            if(vet_aux.length == qtde_col)
                            {
                                for(int i = 0; i < (vet_aux.length - 1) && !sair; i++)
                                {
                                    if(!vet_aux[i].isEmpty())
                                    {
                                        try
                                        {
                                            double d = Double.parseDouble(vet_aux[i]);
                                            list_aux.add(d);
                                            RegMaiorMenor reg_aux = list_maior_menor.get(i);

                                            if(cont_leitura == 0)
                                            {
                                                reg_aux.setMenor(d);
                                                reg_aux.setMaior(d);
                                            }
                                            else
                                            {
                                                if(d < reg_aux.getMenor())
                                                    reg_aux.setMenor(d);
                                                if(d > reg_aux.getMaior())
                                                    reg_aux.setMaior(d);
                                            }
                                        }
                                        catch(Exception ex2) { sair = true; }
                                    }
                                    else
                                        sair = true;
                                }

                                if(!sair && !vet_aux[vet_aux.length - 1].isEmpty())
                                {
                                    if(!list_str.contains(vet_aux[vet_aux.length - 1]))
                                        camada_saida++;
                                    list_str.add(vet_aux[vet_aux.length - 1]);
                                    
                                    list.add(list_aux);
                                }
                                else
                                    sair = true;
                            }
                            else
                                sair = true;
                            cont_leitura++;
                        }
                    }
                    else
                        sair = true;
                }
                else
                    sair = true;
            }
        }
        catch(Exception ex) { sair = true; }
        
        camadas[0] = camada_entrada;
        camadas[1] = camada_saida;
        if(sair)
        {
            list = null;
            list_str = null;
        }
        else
            list = normaliza(list, list_maior_menor);
        
        return list;
    }

    public static ArrayList<ArrayList<Double>> normaliza(ArrayList<ArrayList<Double>> list_valores, List<RegMaiorMenor> list_maior_menor)
    {
        double valor = 0, diferenca = 0, maior = 0, menor = 0;
        for (int i = 0; i < list_valores.size(); i++)
            for (int j = 0; j < list_valores.get(i).size(); j++)
            {
                valor = list_valores.get(i).get(j);
                maior = list_maior_menor.get(j).getMaior();
                menor = list_maior_menor.get(j).getMenor();
                diferenca = maior - menor;
                valor = (valor - menor)/diferenca;
                list_valores.get(i).set(j, valor);
            }
        return list_valores;
    }
    
    /*
    *     1 => linear
    *     2 => logistica
    *     3 => tangenteHip
    */
    public static List<Double> calculaSaidas(List<Double> list_nets, int tipo_funcao)
    {
        List<Double> list_saidas = new ArrayList();
        switch(tipo_funcao)
        {
            case 1:
                for (int i = 0; i < list_nets.size(); i++)
                    list_saidas.add(FunAritmeticas.linear(list_nets.get(i)));
            break;
            
            case 2:
                for (int i = 0; i < list_nets.size(); i++)
                    list_saidas.add(FunAritmeticas.logistica(list_nets.get(i)));
            break;
            
            case 3:
                for (int i = 0; i < list_nets.size(); i++)
                    list_saidas.add(FunAritmeticas.tangenteHip(list_nets.get(i)));
            break;
        }
        return list_saidas;
    }
    
    public static List multiplicaMatriz(List<List<Double>> list_pesos, List<Double> list_entradas)
    {
        if(!list_entradas.isEmpty() && !list_pesos.isEmpty())
            if(list_pesos.get(0).size() == list_entradas.size())
            {
                List<Double> list_nets = new ArrayList();
                double soma = 0;
                int k = 0;
                for (int i = 0; i < list_pesos.size(); i++)
                {
                    for (int j = 0; j < list_pesos.get(i).size(); j++) {
                        soma += list_pesos.get(i).get(j) * list_entradas.get(k++);
                    }
                    list_nets.add(soma);
                    soma = 0;
                    k = 0;
                }
                return list_nets;
            }
        return null;
    }
}

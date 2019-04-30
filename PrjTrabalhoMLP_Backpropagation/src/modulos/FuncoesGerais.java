package modulos;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

public class FuncoesGerais
{
    public static ArrayList<ArrayList<ArrayList<Double>>> criarKFold(ArrayList<ArrayList<Double>> list)
    {
        ArrayList<ArrayList<ArrayList<Double>>> list_folds = new ArrayList();
        Random gerador = new Random();
        int i, j, size = list.size(), folds, data_fold_size, qtde_selecionada = 0;
        if(size >= 100)
            folds = gerador.nextInt(6) + 5; // Sorteia de 5 - 10 FOLDS
        else
            folds = 2;
        data_fold_size = size / folds;
        ArrayList<Integer> list_indices = new ArrayList();
        for(i = 0; i < size; i++)
            list_indices.add(i);
        Collections.shuffle(list_indices);
        ArrayList<ArrayList<Double>> k_fold;
        for(i = 0; i < folds; i++)
        {
            k_fold = new ArrayList();
            for(qtde_selecionada = 0, j = (i * data_fold_size); qtde_selecionada < data_fold_size && 
                    j < size; qtde_selecionada++, j++)
                k_fold.add(list.get(list_indices.get(j)));
            list_folds.add(k_fold);
        }
        return list_folds;
    }
    
    public static ArrayList<ArrayList<ArrayList<Double>>> abrirArquivoKFold(ArrayList<String> list_classes, 
            int[] camadas, TableView<Registro> tabeladados, List<TableColumn> list_colunas)
    {
        ArrayList<ArrayList<ArrayList<Double>>> list_folds = null;
        ArrayList<ArrayList<Double>> list = new ArrayList();
        ArrayList<RegMaiorMenor> list_maior_menor = new ArrayList();
        Registro reg = null;
        ArrayList<Integer> list_linhas_erro = new ArrayList();
        double percent_aceitacao_erro = 15.0, valor_insercao_erro = -1.0;
        int tf = 46, cols_adicionais = 0;
        
        tabeladados.getItems().clear();
        
        if(list_classes == null)
            list_classes = new ArrayList();
        if(camadas == null)
            camadas = new int[2];
        
        camadas[0] = 0;
        camadas[1] = 0;
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
                    vetValores = new String[tf];
                    String[] vet_aux = linha.split(",");
                    for(int i = 0; i < tf; i++)
                    {
                        if(i < vet_aux.length)
                            vetValores[i] = vet_aux[i].toUpperCase();
                        else
                            vetValores[i] = "";
                    }
                    
                    qtde_col = vet_aux.length;
                    
                    if(qtde_col >= 1)
                    {
                        camadas[0] = qtde_col - 1;
                        if(camadas[0] < (tf - 3))
                        {
                            cols_adicionais = 2;
                            vetValores[camadas[0] + 1] = "SAÍDA";
                            vetValores[camadas[0] + 2] = "E. REDE";
                            
                            for(int i = 0; i < qtde_col - 1; i++)
                                list_maior_menor.add(new RegMaiorMenor());
                            
                            reg = new Registro();
                            reg.setTotal(vetValores);
                            
                            ArrayList<Double> list_aux;
                            int cont_leitura = 0;
                            while(rf.getFilePointer() < rf.length())
                            {
                                linha = rf.readLine();
                                if(!linha.isEmpty())
                                {
                                    vet_aux = linha.split(",");
                                    list_aux = new ArrayList();

                                    if(vet_aux.length == qtde_col) list_linhas_erro.add(cont_leitura);

                                    for(int i = 0; i < (vet_aux.length - 1) && !sair; i++)
                                    {
                                        if(vet_aux[i].isEmpty())
                                        {
                                            if(!list_linhas_erro.contains(cont_leitura))
                                                list_linhas_erro.add(cont_leitura);
                                            vet_aux[i] = "" + valor_insercao_erro;
                                        }

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
                                        catch(Exception ex2)
                                        {
                                            //if(vet_aux[i].equals("?")) {
                                            if(vet_aux[i].isEmpty())
                                            if(!list_linhas_erro.contains(cont_leitura))
                                                list_linhas_erro.add(cont_leitura);
                                            vet_aux[i] = "" + valor_insercao_erro;
                                            
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
                                            // }
                                        }
                                    }

                                    if(!vet_aux[vet_aux.length - 1].isEmpty()) // Linha com a sua classe definida
                                    {
                                        vet_aux[vet_aux.length - 1] = vet_aux[vet_aux.length - 1].toUpperCase();
                                        if(!list_classes.contains(vet_aux[vet_aux.length - 1]))
                                            camadas[1]++;

                                        list_classes.add(vet_aux[vet_aux.length - 1]);
                                        list.add(list_aux);
                                    }
                                    else // Linha sem a sua classe definida == Linha não inserida!
                                    {
                                        if(!list_linhas_erro.contains(cont_leitura))
                                            list_linhas_erro.remove(list_linhas_erro.size() - 1);
                                    }

                                    cont_leitura++;
                                }
                            }
                            
                            double percent_erro = (list_linhas_erro.size() * 100) / list.size();
                            if(percent_erro > percent_aceitacao_erro) // Linhas com erro extrapolam o limite definido como aceitável!
                            {
                                // Removendo as linhas com erro, pois muitos valores inválidos foram inseridos para tentar solucionar a rede!
                                for(int i = list_linhas_erro.size() - 1; i >= 0; i--)
                                    list.remove(list_linhas_erro.get(i));
                            }
                        }
                        else
                            sair = true;
                    }
                    else
                        sair = true;
                }
                else
                    sair = true;
            }
        }
        catch(Exception ex) { sair = true; }
        
        if(sair)
        {
            list = null;
            list_classes = null;
            //list_folds = null;
        }
        else
        {
            list = normaliza(list, list_maior_menor, tabeladados);
            list_folds = criarKFold(list);
            
            if(reg != null)
            {
                String[] vet = reg.getTotal();
                for(int i = 0; i < tf && !vet[i].isEmpty(); i++)
                {
                    list_colunas.get(i).setVisible(true);
                    list_colunas.get(i).setText(vet[i]);
                    list_colunas.get(i).setPrefWidth(85);
                }
            }
        }
        
        return list_folds;
    }
    
    public static ArrayList<ArrayList<Double>> abrirArquivo(ArrayList<String> list_classes, 
            int[] camadas, TableView<Registro> tabeladados, List<TableColumn> list_colunas)
    {
        ArrayList<ArrayList<Double>> list = new ArrayList();
        ArrayList<RegMaiorMenor> list_maior_menor = new ArrayList();
        Registro reg = null;
        ArrayList<Integer> list_linhas_erro = new ArrayList();
        double percent_aceitacao_erro = 15.0, valor_insercao_erro = -1.0;
        int tf = 46, cols_adicionais = 0;
        
        tabeladados.getItems().clear();
        
        if(list_classes == null)
            list_classes = new ArrayList();
        if(camadas == null)
            camadas = new int[2];
        
        camadas[0] = 0;
        camadas[1] = 0;
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
                    vetValores = new String[tf];
                    String[] vet_aux = linha.split(",");
                    for(int i = 0; i < tf; i++)
                    {
                        if(i < vet_aux.length)
                            vetValores[i] = vet_aux[i].toUpperCase();
                        else
                            vetValores[i] = "";
                    }
                    
                    qtde_col = vet_aux.length;
                    
                    if(qtde_col >= 1)
                    {
                        camadas[0] = qtde_col - 1;
                        if(camadas[0] < (tf - 3))
                        {
                            cols_adicionais = 2;
                            vetValores[camadas[0] + 1] = "SAÍDA";
                            vetValores[camadas[0] + 2] = "E. REDE";
                            
                            for(int i = 0; i < qtde_col - 1; i++)
                                list_maior_menor.add(new RegMaiorMenor());
                            
                            reg = new Registro();
                            reg.setTotal(vetValores);
                            
                            ArrayList<Double> list_aux;
                            int cont_leitura = 0;
                            while(rf.getFilePointer() < rf.length())
                            {
                                linha = rf.readLine();
                                if(!linha.isEmpty())
                                {
                                    vet_aux = linha.split(",");
                                    list_aux = new ArrayList();

                                    if(vet_aux.length == qtde_col) list_linhas_erro.add(cont_leitura);

                                    for(int i = 0; i < (vet_aux.length - 1) && !sair; i++)
                                    {
                                        if(vet_aux[i].isEmpty())
                                        {
                                            if(!list_linhas_erro.contains(cont_leitura))
                                                list_linhas_erro.add(cont_leitura);
                                            vet_aux[i] = "" + valor_insercao_erro;
                                        }

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
                                        catch(Exception ex2)
                                        {
                                            //if(vet_aux[i].equals("?")) {
                                            if(vet_aux[i].isEmpty())
                                            if(!list_linhas_erro.contains(cont_leitura))
                                                list_linhas_erro.add(cont_leitura);
                                            vet_aux[i] = "" + valor_insercao_erro;
                                            
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
                                            // }
                                        }
                                    }

                                    if(!vet_aux[vet_aux.length - 1].isEmpty()) // Linha com a sua classe definida
                                    {
                                        vet_aux[vet_aux.length - 1] = vet_aux[vet_aux.length - 1].toUpperCase();
                                        if(!list_classes.contains(vet_aux[vet_aux.length - 1]))
                                            camadas[1]++;

                                        list_classes.add(vet_aux[vet_aux.length - 1]);
                                        list.add(list_aux);
                                    }
                                    else // Linha sem a sua classe definida == Linha não inserida!
                                    {
                                        if(!list_linhas_erro.contains(cont_leitura))
                                            list_linhas_erro.remove(list_linhas_erro.size() - 1);
                                    }

                                    cont_leitura++;
                                }
                            }
                            
                            double percent_erro = (list_linhas_erro.size() * 100) / list.size();
                            if(percent_erro > percent_aceitacao_erro) // Linhas com erro extrapolam o limite definido como aceitável!
                            {
                                // Removendo as linhas com erro, pois muitos valores inválidos foram inseridos para tentar solucionar a rede!
                                for(int i = list_linhas_erro.size() - 1; i >= 0; i--)
                                    list.remove(list_linhas_erro.get(i));
                            }
                        }
                        else
                            sair = true;
                    }
                    else
                        sair = true;
                }
                else
                    sair = true;
            }
        }
        catch(Exception ex) { sair = true; }
        
        if(sair)
        {
            list = null;
            list_classes = null;
        }
        else
        {
            list = normaliza(list, list_maior_menor, tabeladados);
            
            if(reg != null)
            {
                String[] vet = reg.getTotal();
                for(int i = 0; i < tf && !vet[i].isEmpty(); i++)
                {
                    list_colunas.get(i).setVisible(true);
                    list_colunas.get(i).setText(vet[i]);
                    list_colunas.get(i).setPrefWidth(85);
                }
            }
        }
        
        return list;
    }

    public static ArrayList<ArrayList<Double>> normaliza(ArrayList<ArrayList<Double>> list_valores, 
            List<RegMaiorMenor> list_maior_menor, TableView<Registro> tabeladados)
    {
        double valor, diferenca, maior, menor;
        int tf = 46, tl1, tl2;
        ArrayList<Registro> list_reg = new ArrayList();
        Registro reg;
        String[] vetValores;
        
        tl1 = list_valores.size();
        for (int i = 0; i < tl1; i++)
        {
            vetValores = new String[tf];
            tl2 = list_valores.get(i).size();
            for (int j = 0; j < tl2; j++)
            {
                valor = list_valores.get(i).get(j);
                maior = list_maior_menor.get(j).getMaior();
                menor = list_maior_menor.get(j).getMenor();
                diferenca = maior - menor;
                valor = (valor - menor)/diferenca;
                list_valores.get(i).set(j, valor);
                
                vetValores[j] = "" + valor;
            }
            
            for(int k = tl2; k < tf; k++)
                vetValores[k] = "";
            reg = new Registro();
            reg.setTotal(vetValores);
            list_reg.add(reg);
        }
        
        tabeladados.setItems(FXCollections.observableArrayList(list_reg));
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
        {
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
        }
        return null;
    }
}
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
    public static ArrayList<Integer> criarKFold(ArrayList<ArrayList<String>> list,ArrayList<Integer> list_folds)
    {
        Random gerador = new Random();
        int i, size = list.size(), folds, data_fold_size;
        if(size >= 500)
            folds = gerador.nextInt(6) + 5; // Sorteia de 5 - 10 FOLDS
        else
            folds = 2;
        data_fold_size = size / folds;
        for(i = 0; i < folds; i++)
            list_folds.add((i * data_fold_size));
        return list_folds;
    }
    
    public static ArrayList<ArrayList<String>> abrirArquivoKFold(ArrayList<Integer> list_folds, 
            ArrayList<String> list_classes, int[] camadas, TableView<Registro> tabeladados, 
            List<TableColumn> list_colunas)
    {
        ArrayList<ArrayList<String>> list = null;
        ArrayList<RegMaiorMenor> list_maior_menor = new ArrayList();
        ArrayList<Integer> list_linhas_erro = new ArrayList();
        ArrayList<String> list_classes_acumuladas = new ArrayList();
        Registro reg = null;
        double percent_aceitacao_erro = 15.0, valor_insercao_erro = (-1.0);
        int tf = 46, cols_adicionais = 0;
        int cont_leitura = 0;
        
        tabeladados.getItems().clear();
        camadas[0] = 0;
        camadas[1] = 0;
        if(list_folds != null && list_classes != null)
        {
            list = new ArrayList();
            list_folds.clear();
            list_classes.clear();
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

                                ArrayList<String> list_aux;
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
                                                list_aux.add("" + d);
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
                                                list_aux.add("" + d);
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
                                            {
                                                camadas[1]++;
                                                list_classes.add(vet_aux[vet_aux.length - 1]);
                                            }

                                            list_classes_acumuladas.add(vet_aux[vet_aux.length - 1]);
                                            list_aux.add(vet_aux[vet_aux.length - 1]);
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
                                    {
                                        list.remove(list_linhas_erro.get(i));
                                        list_classes_acumuladas.remove(list_linhas_erro.get(i));
                                    }
                                    for(int i = 0; i < list_classes.size(); i++)
                                        if(!list_classes_acumuladas.contains(list_classes.get(i)))
                                        {
                                            list_classes.remove(i);
                                            i--;
                                        }
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
        
            if(cont_leitura < 2)
                sair = true;

            if(sair)
            {
                list = null;
                list_folds = null;
                list_classes = null;
            }
            else
            {
                Collections.shuffle(list);
                Collections.sort(list_classes);
                list = normaliza(list, list_maior_menor, tabeladados);
                list_folds = criarKFold(list, list_folds);

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
        }
        return list;
    }
    
    /*
    * @ Normaliza os dados do arquivo para que seja processado corretamente
    */
    public static ArrayList<ArrayList<String>> normaliza(ArrayList<ArrayList<String>> list_valores, 
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
            tl2 = list_valores.get(i).size() - 1;
            for (int j = 0; j < tl2; j++)
            {
                valor = Double.parseDouble(list_valores.get(i).get(j));
                maior = list_maior_menor.get(j).getMaior();
                menor = list_maior_menor.get(j).getMenor();
                diferenca = maior - menor;
                valor = (valor - menor)/diferenca;
                list_valores.get(i).set(j, "" + valor);
                vetValores[j] = "" + valor;
            }
            vetValores[tl2] = "" + list_valores.get(i).get(tl2);
            for(int k = tl2 + 1; k < tf; k++)
                vetValores[k] = "";
            reg = new Registro();
            reg.setTotal(vetValores);
            list_reg.add(reg);
        }
        
        tabeladados.setItems(FXCollections.observableArrayList(list_reg));
        return list_valores;
    }
    
    /*
    * @ Função para embaralhar os dados a fim de proporcionar ao algoritmo uma sequência de entradas de 
    *                                                                   classes intercaladas e não viciar a rede
    *
    * @ Variação do algoritmo Fisher-Yates
    */
    public static ArrayList<ArrayList<Double>> embaralhar(ArrayList<ArrayList<Double>> list_dados)
    {
        if(list_dados != null && list_dados.size() > 0)
        {
            int k = 0, n = list_dados.size()-1, limite = Integer.parseInt(""+(list_dados.size()/2));
            ArrayList<Double> list_aux = null;
            while(n > 0 && n > limite)
            {
                k = (int)(Math.random()*(n-1)+1);
                /* Realizando a Troca */
                list_aux = list_dados.get(k);
                list_dados.set(k, list_dados.get(n));
                list_dados.set(n, list_aux);
                /* Decrementa N */
                n--;
            }
            return list_dados;
        }
        else
            return null;
    }
}
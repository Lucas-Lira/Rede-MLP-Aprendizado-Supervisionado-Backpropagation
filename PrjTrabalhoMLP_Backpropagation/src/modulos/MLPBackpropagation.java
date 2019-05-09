package modulos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TableView;

public class MLPBackpropagation 
{
    private double[][] mat_pesos_entrada, mat_pesos_saida, mat_desejada, mat_confusao;
    private ArrayList<Neuronio> list_camada_oculta, list_camada_saida;
    private int pos_inic_dados_adicionais;
    private int qtd_neuronios_entradas;
    private int qtd_neuronios_oculta;
    private int qtd_neuronios_saida;
    private double taxa_aprendizado;
    private int linha_mat_entrada, coluna_mat_entrada;
    private int linha_mat_saida, coluna_mat_saida;
    private ArrayList<String> list_classes;
    
    public MLPBackpropagation(int pos_inic_dados_adicionais, int qtd_neuronios_entradas, int qtd_neuronios_oculta, int qtd_neuronios_saida, double taxa_aprendizado, ArrayList<String> list_classes)
    {
        this.pos_inic_dados_adicionais = pos_inic_dados_adicionais;
        this.qtd_neuronios_entradas = qtd_neuronios_entradas;
        this.qtd_neuronios_oculta = qtd_neuronios_oculta;
        this.qtd_neuronios_saida = qtd_neuronios_saida;
        this.taxa_aprendizado = taxa_aprendizado;
        this.linha_mat_entrada = (qtd_neuronios_entradas+1);
        this.coluna_mat_entrada = qtd_neuronios_oculta;
        this.linha_mat_saida = (qtd_neuronios_oculta+1);
        this.coluna_mat_saida = qtd_neuronios_saida;
        this.list_classes = list_classes;
        this.mat_desejada = geraMatDesejada();
        this.mat_confusao = geraMatConfusao();
        inicializaMatPesosEntrada();
        inicializaMatPesosSaida();
        inicializaListCamadaOculta();
        inicializaListCamadaSaida();
        geraPesosIniciais();
    }
    
    /*
    * O formato da tabela de pesos é: Linhas Representam os neurônios da camada antecessora incluindo o vies (Bias)
    *               e as colunas respresentam a camada seguinte; 
    */
    public void inicializaMatPesosEntrada(){
        mat_pesos_entrada = new double[linha_mat_entrada][];
        for (int i = 0; i < linha_mat_entrada; i++)
            mat_pesos_entrada[i] = new double[coluna_mat_entrada]; 
    }
    
    public void inicializaMatPesosSaida(){
        mat_pesos_saida = new double[linha_mat_saida][];
        for (int i = 0; i < (qtd_neuronios_oculta+1); i++)
            mat_pesos_saida[i] = new double[coluna_mat_saida];
    }
    
    public void inicializaListCamadaOculta()
    {
        list_camada_oculta = new ArrayList();
        for (int i = 0; i < qtd_neuronios_oculta; i++)
            list_camada_oculta.add(new Neuronio());
    }
    
    public void inicializaListCamadaSaida()
    {
        list_camada_saida = new ArrayList();
        for (int i = 0; i < qtd_neuronios_saida; i++)
            list_camada_saida.add(new Neuronio());
    }

    public void executaTreinamento(TableView<Registro> tabeladados, Canvas canvasgrafico, ArrayList<ArrayList<String>>list_dados, 
            ArrayList<Integer> list_folds, int teste, int tipo_funcao, int qtd_epocas, int limiar)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int cont_epocas = 0, i, indice_teste;
                double erro_rede;
                boolean sair = false;
                
                while(!sair && cont_epocas < qtd_epocas)
                {
                    i = 0;
                    erro_rede = Double.POSITIVE_INFINITY;
                    while(!sair && i < list_dados.size())
                    {
                        if(i == teste)
                        {
                            indice_teste = list_folds.indexOf(i);
                            if(indice_teste < (list_folds.size() - 1))
                                i = list_folds.get(indice_teste + 1);
                            else
                            {
                                //i = list_dados.size();
                                break;
                            }
                        }

                        calculaNetsEntrada(list_dados.get(i));
                        calculaSaidasEntrada(tipo_funcao);
                        
                        calculaNetsSaida();
                        calculaSaidasSaida(tipo_funcao);
                        
                        // ...
                        
                        if(erro_rede <= limiar)
                            sair = true;
                        i++;
                    }
                }
                
                /*
                Platform.runLater(()-> { });
                try{ Thread.sleep(400); } catch(Exception e) { }
                */
            }
        }).start();
    }
    
    /*
    public double calculaErroCamadaSaida(String classe_saida)
    {
    }
    */
    
    public void geraPesosIniciais(){
        /* Sorteando pesos mat_entradas */
        Random random = new Random();
        for (int i = 0; i < linha_mat_entrada; i++)
            for (int j = 0; j < coluna_mat_entrada; j++)
                mat_pesos_entrada[i][j] = random.nextInt(5) + (-2);
        /* Sorteando pesos mat_saidas */
        for (int i = 0; i < linha_mat_saida; i++)
            for (int j = 0; j < coluna_mat_saida; j++)
                mat_pesos_saida[i][j] = random.nextInt(5) + (-2);
    }
    
    /*
    *     1 => linear
    *     2 => logistica
    *     3 => tangenteHip
    */
    public void calculaSaidasEntrada(int tipo_funcao)
    {
        switch(tipo_funcao)
        {
            case 1:
                for (int i = 0; i < list_camada_oculta.size(); i++)
                    list_camada_oculta.get(i).setINet(FunAritmeticas.linear(list_camada_oculta.get(i).getNet()));
            break;
            
            case 2:
                for (int i = 0; i < list_camada_oculta.size(); i++)
                    list_camada_oculta.get(i).setINet(FunAritmeticas.logistica(list_camada_oculta.get(i).getNet()));
            break;
            
            case 3:
                for (int i = 0; i < list_camada_oculta.size(); i++)
                    list_camada_oculta.get(i).setINet(FunAritmeticas.tangenteHip(list_camada_oculta.get(i).getNet()));
            break;
        }
    }
    
    public void calculaSaidasSaida(int tipo_funcao)
    {
        switch(tipo_funcao)
        {
            case 1:
                for (int i = 0; i < list_camada_saida.size(); i++)
                    list_camada_saida.get(i).setINet(FunAritmeticas.linear(list_camada_saida.get(i).getNet()));
            break;
            
            case 2:
                for (int i = 0; i < list_camada_saida.size(); i++)
                    list_camada_saida.get(i).setINet(FunAritmeticas.logistica(list_camada_saida.get(i).getNet()));
            break;
            
            case 3:
                for (int i = 0; i < list_camada_saida.size(); i++)
                    list_camada_saida.get(i).setINet(FunAritmeticas.tangenteHip(list_camada_saida.get(i).getNet()));
            break;
        }
    }
    
    public void calculaNetsEntrada(ArrayList<String> list_entradas)
    {
        int j = 0;
        double soma = 0;
        for (int i = 0; i < coluna_mat_entrada; i++) {
            soma = 0;
            for (j = 0; j < qtd_neuronios_entradas; j++) {
                soma += (Double.parseDouble(list_entradas.get(j)) * mat_pesos_entrada[j][i]);
            }
            // Calculando o Bias (Viés)
            soma += (1 * mat_pesos_entrada[j][i]);
            list_camada_oculta.get(i).setNet(soma);
        }
    }
    
    public void calculaNetsSaida(){
        int j = 0;
        double soma = 0;
        for (int i = 0; i < coluna_mat_saida; i++) {
            soma = 0;
            for (j = 0; j < qtd_neuronios_oculta; j++) {
                soma += (list_camada_oculta.get(j).getINet() * mat_pesos_saida[j][i]);
            }
            // Calculando o Bias (Viés)
            soma += (1 * mat_pesos_saida[j][i]);
            list_camada_saida.get(i).setNet(soma);
        }
    }
    
    /*
    * @ Gera a matriz de confusão que será usada no sistema
    */
    public double[][] geraMatConfusao(){
        int dimensao = qtd_neuronios_saida;
        double[][] mat = new double[dimensao][];
        for (int i = 0; i < dimensao; i++){
            mat[i] = new double[dimensao];
            for (int j = 0; j < dimensao; j++)
                mat[i][j] = 0;
        }
        return mat;
    }
    
    /*
    * @ Gera a matriz desejada que será usada no sistema
    */
    public double[][] geraMatDesejada(){
        int dimensao = qtd_neuronios_saida;
        double[][] mat = new double[dimensao][];
        for (int i = 0; i < dimensao; i++){
            mat[i] = new double[dimensao];
            for (int j = 0; j < dimensao; j++)
            {
                if(i == j){
                    mat[i][j] = 1;
                }
                else{
                    mat[i][j] = 0;
                }
            }
        }
        return mat;
    }
    
    
    public List multiplicaMatriz(List<List<Double>> list_pesos, List<Double> list_entradas)
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

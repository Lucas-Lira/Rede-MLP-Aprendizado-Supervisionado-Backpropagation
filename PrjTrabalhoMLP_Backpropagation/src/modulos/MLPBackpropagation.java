package modulos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.control.TableView;

public class MLPBackpropagation 
{
    private double[][] mat_pesos_entrada, mat_pesos_saida;
    private ArrayList<Neuronio> list_camada_oculta, list_camada_saida;
    private int pos_inic_dados_adicionais;
    private int qtd_neuronios_entradas;
    private int qtd_neuronios_oculta;
    private int qtd_neuronios_saida;
    private double taxa_aprendizado;
    private int linha_mat_entrada, coluna_mat_entrada;
    private int linha_mat_saida, coluna_mat_saida;
    
    public MLPBackpropagation(int pos_inic_dados_adicionais, int qtd_neuronios_entradas, int qtd_neuronios_oculta, int qtd_neuronios_saida, double taxa_aprendizado)
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

    public void executa(TableView<Registro> tabeladados, int tipo_funcao, int qtd_epocas)
    {
        
    }  
    
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
    public ArrayList<Double> calculaSaidas(ArrayList<Double> list_nets, int tipo_funcao)
    {
        ArrayList<Double> list_saidas = new ArrayList();
        
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
    
    /* Tem considerar o Vies na Multiplicação */
    /* Retornar List Nets */
    /* Verificar se a quantidade informada de neuronios se adapta a contagem iniciando de zero */
    public ArrayList<Double> calculaNetsEntrada(ArrayList<String> list_entradas)
    {
        // O list Nets nesse caso representa os neurônios da camada oculta em ordem, o 1º da lista representa o neurônio de
            // cima e assim sucessivamente
        ArrayList<Double> list_nets = new ArrayList();
        int j = 0;
        double soma = 0;
        for (int i = 0; i < coluna_mat_entrada; i++) {
            soma = 0;
            for (j = 0; j < qtd_neuronios_entradas; j++) {
                soma += (Double.parseDouble(list_entradas.get(j)) * mat_pesos_entrada[j][i]);
            }
            // Calculando o Bias (Viés)
            soma += (1 * mat_pesos_entrada[j][i]);
            list_nets.add(soma); 
        }
        return list_nets;
    }
    
    public ArrayList<Double> calculaNetsSaida(ArrayList<Double> list_entradas){
        ArrayList<Double> list_nets = new ArrayList();
        int j = 0;
        double soma = 0;
        for (int i = 0; i < coluna_mat_saida; i++) {
            soma = 0;
            for (j = 0; j < qtd_neuronios_oculta; j++) {
                soma += (list_entradas.get(j) * mat_pesos_saida[j][i]);
            }
            // Calculando o Bias (Viés)
            soma += (1 * mat_pesos_saida[j][i]);
            list_nets.add(soma); 
        }
        return list_nets;
    }
    
    /*
    * @ Gera a matriz de confusão que será usada no sistema
    */
    public double[][] geraMatConfusao(int dimensao){
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
    public double[][] geraMatDesejada(int dimensao /* Número diferente de classes que a base possui */){
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

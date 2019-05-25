package modulos;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
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
    private Thread thread_treinamento;
    private Thread thread_teste;
    
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
        this.thread_treinamento = null;
        this.thread_teste = null;
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
    
    public void executaTeste(ArrayList<ArrayList<String>>list_dados, 
            ArrayList<Integer> list_folds, int teste, int tipo_funcao, JFXButton binicializar)
    {
        this.mat_confusao = geraMatConfusao();
        /*
        if(thread_teste != null && thread_teste.isAlive())
        {
            thread_teste.interrupt();
            thread_teste = null;
        }
        */
        //thread_teste = new Thread(new Runnable()
        //{
        //    @Override
        //    public void run()
        //    {
                int i, pos, lenght;
                String classe;
                ArrayList<String> dados_teste;
                
                pos = list_folds.get(teste);
                if(teste < (list_folds.size() - 1))
                    lenght = list_folds.get(teste + 1);
                else
                    lenght = list_folds.size();
                
                i = pos;
                while(i < lenght)
                {
                    calculaNetsEntrada(list_dados.get(i));
                    calculaSaidasEntrada(tipo_funcao);
                    calculaNetsSaida();
                    calculaSaidasSaida(tipo_funcao);
                    classe = list_dados.get(i).get(pos_inic_dados_adicionais - 1);
                    verificaResultadoTeste(classe);
                    
                    dados_teste = list_dados.get(i);
                    System.out.println("");
                    System.out.print("[ " + i + " ] - ");
                    for(int j = 0; j < dados_teste.size(); j++)
                        System.out.print(dados_teste.get(j) + "   ;   ");
                    i++;
                }
                System.out.println("\nClasse(s):");
                for(int j = 0; j < list_classes.size(); j++)
                    System.out.print(list_classes.get(j) + "    ");
                System.out.println("\nResultado(s):");
                for(int j = 0; j < qtd_neuronios_saida; j++) {
                    for(int k = 0; k < qtd_neuronios_saida; k++) {
                        System.out.print("" + mat_confusao[j][k] + " ");
                    }
                    System.out.println("");
                }
        //        Platform.runLater(()-> {
                    //informa("Aviso", "Matriz gerada com Sucesso!\nClique no botão 'M. Confusão' para visualiza-la");
                    binicializar.setDisable(false);
        //        });
        //    }
        //});
        //thread_teste.start();
    }
    
    public ArrayList<Registro> getMatrizDeConfusaoRegistro()
    {
        ArrayList<Registro> list_mat = new ArrayList();
        Registro reg;
        String[] vet;
        for(int i = 0; i < qtd_neuronios_saida; i++) {
            vet = new String[46];
            vet[0] = list_classes.get(i);
            for(int j = 0; j < qtd_neuronios_saida; j++) {
                vet[j + 1] = "" + ((int)mat_confusao[i][j]);
            }
            reg = new Registro();
            reg.setTotal(vet);
            list_mat.add(reg);
        }
        return list_mat;
    }
    
    public void verificaResultadoTeste(String classe)
    {
        int linha = list_classes.indexOf(classe);
        int pos = buscaInetMaior();
        mat_confusao[linha][pos] += 1.0;
    }
    
    public int buscaInetMaior()
    {
        int pos = 0;
        double maior = list_camada_saida.get(0).getINet();
        double valor;
        for(int i = 1; i < qtd_neuronios_saida; i++) {
            valor = list_camada_saida.get(i).getINet();
            if(valor > maior) {
                maior = valor;
                pos = i;
            }
        }
        return pos;
    }

    public void executaTreinamento(TableView<Registro> tabeladados, LineChart<String, Number> lcgrafico, 
            ArrayList<ArrayList<String>>list_dados, ArrayList<Integer> list_folds, int teste, int tipo_funcao, 
            int qtd_epocas, double limiar, Parametros parameros, JFXButton binicializar)
    {
        if(thread_treinamento != null && thread_treinamento.isAlive())
        {
            thread_treinamento.interrupt();
            thread_treinamento = null;
        }
        thread_treinamento = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int cont_epocas = 0, i, indice_teste = list_folds.get(teste), cont_linhas;
                double erro_rede = (-1), erro = (-1);
                boolean sair = false;
                String classe;
                Registro registro = null;
                
                Platform.runLater(()-> {
                    parameros.getErro().setText("");
                    parameros.getEpocas().setText("");
                });
                
                while(!sair && cont_epocas < qtd_epocas)
                {
                    i = 0;
                    erro_rede = 0;
                    cont_linhas = 0;
                    while(i < list_dados.size())
                    {
                        if(i == indice_teste)
                        {
                            if(teste < (list_folds.size() - 1)) { i = list_folds.get(teste + 1); }
                            else { break; }
                        }

                        calculaNetsEntrada(list_dados.get(i));
                        calculaSaidasEntrada(tipo_funcao);
                        calculaNetsSaida();
                        calculaSaidasSaida(tipo_funcao);
                        classe = list_dados.get(i).get(pos_inic_dados_adicionais - 1);
                        erro = calculaErroCamadaSaida(classe, tipo_funcao);
                        
                        /*
                        final int i_final = i;
                        final int pos_inic = pos_inic_dados_adicionais;
                        final double erro_final = erro;
                        
                        Platform.runLater(()-> {
                            Registro reg = tabeladados.getItems().get(i_final);
                            String[] vet_str = reg.getTotal();
                            vet_str[pos_inic] = "" + erro_final;
                            reg.setTotal(vet_str);
                            tabeladados.getItems().set(i_final, reg);
                        });
                        */
                        
                        erro_rede += erro;
                        calculaErroCamadaOculta(tipo_funcao);
                        ajustarPesosCamadaSaida();
                        ajustarPesosCamadaOculta(list_dados.get(i));
                        
                        i++;
                        cont_linhas++;
                        //try{ Thread.sleep(20); } catch(Exception e) { }
                    }
                    
                    erro_rede /= cont_linhas;
                    if(erro_rede <= limiar)
                        sair = true;
                    System.out.println("[ Época(s) ]: " + cont_epocas + " ; [ Erro médio ]: " + erro_rede);
                    
                    final int i_epocas = cont_epocas;
                    final double e = erro_rede;
                    Platform.runLater(()-> {
                        //tabeladados.refresh();
                        lcgrafico.getData().get(lcgrafico.getData().size() - 1).getData().add(new XYChart.Data(""+i_epocas, e));
                    });
                    
                    cont_epocas++;
                }
                
                final int ep = cont_epocas;
                final double e = erro_rede;
                Platform.runLater(()-> {
                    parameros.getErro().setText("Erro: " + e);
                    parameros.getEpocas().setText("Época(s): " + ep);
                    
                    binicializar.setDisable(false);
                });
            }
        });
        thread_treinamento.start();
    }
    
    public void ajustarPesosCamadaOculta(ArrayList<String> list_entradas)
    {
        double novo_peso;
        int i = 0;
        for(; i < linha_mat_entrada - 1; i++)
        {
            for(int j = 0; j < qtd_neuronios_oculta; j++)
            {
                novo_peso = mat_pesos_entrada[i][j] + (taxa_aprendizado * 
                        list_camada_oculta.get(j).getErro() * Double.parseDouble(list_entradas.get(i)));
                mat_pesos_entrada[i][j] = novo_peso;
           }
        }
        for(int j = 0; j < qtd_neuronios_oculta; j++)
        {
            novo_peso = mat_pesos_entrada[i][j] + (taxa_aprendizado * 
                    list_camada_oculta.get(j).getErro()); // *1
            mat_pesos_entrada[i][j] = novo_peso;
        }
    }
    
    public void ajustarPesosCamadaSaida()
    {
        double novo_peso;
        int i = 0;
        for(; i < linha_mat_saida - 1; i++)
        {
            for(int j = 0; j < qtd_neuronios_saida; j++)
            {
                novo_peso = mat_pesos_saida[i][j] + (taxa_aprendizado * 
                        list_camada_saida.get(j).getErro() * list_camada_oculta.get(i).getINet());
                mat_pesos_saida[i][j] = novo_peso;
            }
        }
        for(int j = 0; j < qtd_neuronios_saida; j++)
        {
            novo_peso = mat_pesos_saida[i][j] + (taxa_aprendizado * 
                    list_camada_saida.get(j).getErro()); // *1
            mat_pesos_saida[i][j] = novo_peso;
        }
    }
    
    public double calculaErroCamadaSaida(String classe, int tipo_funcao)
    {
        int linha = list_classes.indexOf(classe);
        double soma, erro_rede = 0.0;
        int i = 0;
        for(; i < qtd_neuronios_saida; i++)
        {
            soma = mat_desejada[linha][i] - list_camada_saida.get(i).getINet();
            erro_rede += Math.pow(soma, 2);
            soma = aplicaDerivadaErroSaida(tipo_funcao, soma, list_camada_saida.get(i).getNet());
            list_camada_saida.get(i).setErro(soma);
        }
        return (erro_rede * 0.5);
    }
    
    public void calculaErroCamadaOculta(int tipo_funcao)
    {
        double soma;
        int i = 0;
        for(; i < linha_mat_saida - 1; i++)
        {
            soma = 0.0;
            for(int j = 0; j < qtd_neuronios_saida; j++)
                soma += mat_pesos_saida[i][j] * list_camada_saida.get(j).getErro();
            soma = aplicaDerivadaErroSaida(tipo_funcao, soma, list_camada_oculta.get(i).getNet());
            list_camada_oculta.get(i).setErro(soma);
        }
    }
    
    public double aplicaDerivadaErroSaida(int tipo_funcao, double soma, double net)
    {
        double result = 0.0;
        switch(tipo_funcao)
        {
            case 1:
                result = soma * FunAritmeticas.linearDer();
            break;
            
            case 2:
                result = soma * FunAritmeticas.logisticaDer(net);
            break;
            
            case 3:
                result = soma * FunAritmeticas.tangenteHipDer(net);
            break;
        }
        return result;
    }
    
    public void geraPesosIniciais(){
        /* Sorteando pesos mat_entradas */
        Random random = new Random();
        for (int i = 0; i < linha_mat_entrada; i++)
            for (int j = 0; j < coluna_mat_entrada; j++)
                mat_pesos_entrada[i][j] = Double.parseDouble("" + (random.nextInt(5) + (-2)));
        /* Sorteando pesos mat_saidas */
        for (int i = 0; i < linha_mat_saida; i++)
            for (int j = 0; j < coluna_mat_saida; j++)
                mat_pesos_saida[i][j] = Double.parseDouble("" + (random.nextInt(5) + (-2)));
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
        int j;
        double soma;
        for (int i = 0; i < coluna_mat_entrada; i++) {
            soma = 0.0;
            for (j = 0; j < qtd_neuronios_entradas; j++) {
                soma += (Double.parseDouble(list_entradas.get(j)) * mat_pesos_entrada[j][i]);
            }
            // Calculando o Bias (Viés)
            soma += (mat_pesos_entrada[j][i]);
            list_camada_oculta.get(i).setNet(soma);
        }
    }
    
    public void calculaNetsSaida(){
        int j;
        double soma;
        for (int i = 0; i < coluna_mat_saida; i++) {
            soma = 0.0;
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
                mat[i][j] = 0.0;
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
                    mat[i][j] = 1.0;
                }
                else{
                    mat[i][j] = 0.0;
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
                double soma = 0.0;
                int k = 0;
                for (int i = 0; i < list_pesos.size(); i++)
                {
                    for (int j = 0; j < list_pesos.get(i).size(); j++) {
                        soma += list_pesos.get(i).get(j) * list_entradas.get(k++);
                    }
                    list_nets.add(soma);
                    soma = 0.0;
                    k = 0;
                }
                return list_nets;
            }
        }
        return null;
    }
    
    public void informa(String str1, String str2)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(str1);
        alert.setContentText(str2);
        alert.showAndWait();
    }
}
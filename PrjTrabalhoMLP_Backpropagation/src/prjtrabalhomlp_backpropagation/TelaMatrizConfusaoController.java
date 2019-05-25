package prjtrabalhomlp_backpropagation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modulos.Registro;

public class TelaMatrizConfusaoController implements Initializable
{
    @FXML
    private TableColumn<String, String> coltransicao1;
    @FXML
    private TableColumn<String, String> coltransicao2;
    @FXML
    private TableColumn<String, String> coltransicao3;
    @FXML
    private TableColumn<String, String> coltransicao4;
    @FXML
    private TableColumn<String, String> coltransicao5;
    @FXML
    private TableColumn<String, String> coltransicao6;
    @FXML
    private TableColumn<String, String> coltransicao7;
    @FXML
    private TableColumn<String, String> coltransicao8;
    @FXML
    private TableColumn<String, String> coltransicao9;
    @FXML
    private TableColumn<String, String> coltransicao10;
    @FXML
    private TableColumn<String, String> coltransicao11;
    @FXML
    private TableColumn<String, String> coltransicao12;
    @FXML
    private TableColumn<String, String> coltransicao13;
    @FXML
    private TableColumn<String, String> coltransicao14;
    @FXML
    private TableColumn<String, String> coltransicao15;
    @FXML
    private TableColumn<String, String> coltransicao16;
    @FXML
    private TableColumn<String, String> coltransicao17;
    @FXML
    private TableColumn<String, String> coltransicao18;
    @FXML
    private TableColumn<String, String> coltransicao19;
    @FXML
    private TableColumn<String, String> coltransicao20;
    @FXML
    private TableColumn<String, String> coltransicao21;
    @FXML
    private TableColumn<String, String> coltransicao22;
    @FXML
    private TableColumn<String, String> coltransicao23;
    @FXML
    private TableColumn<String, String> coltransicao24;
    @FXML
    private TableColumn<String, String> coltransicao25;
    @FXML
    private TableColumn<String, String> coltransicao26;
    @FXML
    private TableColumn<String, String> coltransicao27;
    @FXML
    private TableColumn<String, String> coltransicao28;
    @FXML
    private TableColumn<String, String> coltransicao29;
    @FXML
    private TableColumn<String, String> coltransicao30;
    @FXML
    private TableColumn<String, String> coltransicao31;
    @FXML
    private TableColumn<String, String> coltransicao32;
    @FXML
    private TableColumn<String, String> coltransicao33;
    @FXML
    private TableColumn<String, String> coltransicao34;
    @FXML
    private TableColumn<String, String> coltransicao35;
    @FXML
    private TableColumn<String, String> coltransicao36;
    @FXML
    private TableColumn<String, String> coltransicao37;
    @FXML
    private TableColumn<String, String> coltransicao38;
    @FXML
    private TableColumn<String, String> coltransicao39;
    @FXML
    private TableColumn<String, String> coltransicao40;
    @FXML
    private TableColumn<String, String> coltransicao41;
    @FXML
    private TableColumn<String, String> coltransicao42;
    @FXML
    private TableColumn<String, String> coltransicao43;
    @FXML
    private TableColumn<String, String> coltransicao44;
    @FXML
    private TableColumn<String, String> coltransicao45;
    @FXML
    private TableColumn<String, String> coltransicao46;
    @FXML
    private TableView<Registro> tabelamatrizconfusao;
    
    private List<TableColumn> list_colunas;
    private static ArrayList<Registro> list_reg;
    private static ArrayList<String> list_class;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.list_reg = TelaPrincipalController.list_reg;
        this.list_class = TelaPrincipalController.list_class;
        configurarTabelaColunas();
    }
    
    public void configurarTabelaColunas()
    {
        coltransicao1.setCellValueFactory(new PropertyValueFactory<>("a0"));
        coltransicao2.setCellValueFactory(new PropertyValueFactory<>("a1"));
        coltransicao3.setCellValueFactory(new PropertyValueFactory<>("a2"));
        coltransicao4.setCellValueFactory(new PropertyValueFactory<>("a3"));
        coltransicao5.setCellValueFactory(new PropertyValueFactory<>("a4"));
        coltransicao6.setCellValueFactory(new PropertyValueFactory<>("a5"));
        coltransicao7.setCellValueFactory(new PropertyValueFactory<>("a6"));
        coltransicao8.setCellValueFactory(new PropertyValueFactory<>("a7"));
        coltransicao9.setCellValueFactory(new PropertyValueFactory<>("a8"));
        coltransicao10.setCellValueFactory(new PropertyValueFactory<>("a9"));
        coltransicao11.setCellValueFactory(new PropertyValueFactory<>("a10"));
        coltransicao12.setCellValueFactory(new PropertyValueFactory<>("a11"));
        coltransicao13.setCellValueFactory(new PropertyValueFactory<>("a12"));
        coltransicao14.setCellValueFactory(new PropertyValueFactory<>("a13"));
        coltransicao15.setCellValueFactory(new PropertyValueFactory<>("a14"));
        coltransicao16.setCellValueFactory(new PropertyValueFactory<>("a15"));
        coltransicao17.setCellValueFactory(new PropertyValueFactory<>("a16"));
        coltransicao18.setCellValueFactory(new PropertyValueFactory<>("a17"));
        coltransicao19.setCellValueFactory(new PropertyValueFactory<>("a18"));
        coltransicao20.setCellValueFactory(new PropertyValueFactory<>("a19"));
        coltransicao21.setCellValueFactory(new PropertyValueFactory<>("a20"));
        coltransicao22.setCellValueFactory(new PropertyValueFactory<>("a21"));
        coltransicao23.setCellValueFactory(new PropertyValueFactory<>("a22"));
        coltransicao24.setCellValueFactory(new PropertyValueFactory<>("a23"));
        coltransicao25.setCellValueFactory(new PropertyValueFactory<>("a24"));
        coltransicao26.setCellValueFactory(new PropertyValueFactory<>("a25"));
        coltransicao27.setCellValueFactory(new PropertyValueFactory<>("a26"));
        coltransicao28.setCellValueFactory(new PropertyValueFactory<>("a27"));
        coltransicao29.setCellValueFactory(new PropertyValueFactory<>("a28"));
        coltransicao30.setCellValueFactory(new PropertyValueFactory<>("a29"));
        coltransicao31.setCellValueFactory(new PropertyValueFactory<>("a30"));
        coltransicao32.setCellValueFactory(new PropertyValueFactory<>("a31"));
        coltransicao33.setCellValueFactory(new PropertyValueFactory<>("a32"));
        coltransicao34.setCellValueFactory(new PropertyValueFactory<>("a33"));
        coltransicao35.setCellValueFactory(new PropertyValueFactory<>("a34"));
        coltransicao36.setCellValueFactory(new PropertyValueFactory<>("a35"));
        coltransicao37.setCellValueFactory(new PropertyValueFactory<>("a36"));
        coltransicao38.setCellValueFactory(new PropertyValueFactory<>("a37"));
        coltransicao39.setCellValueFactory(new PropertyValueFactory<>("a38"));
        coltransicao40.setCellValueFactory(new PropertyValueFactory<>("a49"));
        coltransicao41.setCellValueFactory(new PropertyValueFactory<>("a40"));
        coltransicao42.setCellValueFactory(new PropertyValueFactory<>("a41"));
        coltransicao43.setCellValueFactory(new PropertyValueFactory<>("a42"));
        coltransicao44.setCellValueFactory(new PropertyValueFactory<>("a43"));
        coltransicao45.setCellValueFactory(new PropertyValueFactory<>("a44"));
        coltransicao46.setCellValueFactory(new PropertyValueFactory<>("a45"));
        
        inicializarListColunas();
    }
    
    public void inicializarListColunas()
    {
        tabelamatrizconfusao.getItems().clear();
        
        list_colunas = new ArrayList();
        list_colunas.add(coltransicao1);
        list_colunas.add(coltransicao2);
        list_colunas.add(coltransicao3);
        list_colunas.add(coltransicao4);
        list_colunas.add(coltransicao5);
        list_colunas.add(coltransicao6);
        list_colunas.add(coltransicao7);
        list_colunas.add(coltransicao8);
        list_colunas.add(coltransicao9);
        list_colunas.add(coltransicao10);
        list_colunas.add(coltransicao11);
        list_colunas.add(coltransicao12);
        list_colunas.add(coltransicao13);
        list_colunas.add(coltransicao14);
        list_colunas.add(coltransicao15);
        list_colunas.add(coltransicao16);
        list_colunas.add(coltransicao17);
        list_colunas.add(coltransicao18);
        list_colunas.add(coltransicao19);
        list_colunas.add(coltransicao20);
        list_colunas.add(coltransicao21);
        list_colunas.add(coltransicao22);
        list_colunas.add(coltransicao23);
        list_colunas.add(coltransicao24);
        list_colunas.add(coltransicao25);
        list_colunas.add(coltransicao26);
        list_colunas.add(coltransicao27);
        list_colunas.add(coltransicao28);
        list_colunas.add(coltransicao29);
        list_colunas.add(coltransicao30);
        list_colunas.add(coltransicao31);
        list_colunas.add(coltransicao32);
        list_colunas.add(coltransicao33);
        list_colunas.add(coltransicao34);
        list_colunas.add(coltransicao35);
        list_colunas.add(coltransicao36);
        list_colunas.add(coltransicao37);
        list_colunas.add(coltransicao38);
        list_colunas.add(coltransicao39);
        list_colunas.add(coltransicao40);
        list_colunas.add(coltransicao41);
        list_colunas.add(coltransicao42);
        list_colunas.add(coltransicao43);
        list_colunas.add(coltransicao44);
        list_colunas.add(coltransicao45);
        list_colunas.add(coltransicao46);
        
        for(int i = 0; i < list_class.size() + 1; i++)
        {
            list_colunas.get(i).setVisible(true);
            if(i != 0) list_colunas.get(i).setText(list_class.get(i - 1));
            else list_colunas.get(i).setText("");
            list_colunas.get(i).setPrefWidth(100);
        }
        for(int i = 0; i < list_reg.size(); i++)
            tabelamatrizconfusao.getItems().add(list_reg.get(i));
        tabelamatrizconfusao.refresh();
    }
}
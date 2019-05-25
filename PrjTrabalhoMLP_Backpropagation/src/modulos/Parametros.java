package modulos;

import javafx.scene.control.Label;

public class Parametros
{
    private Label erro;
    private Label epocas;

    public Parametros(Label erro, Label epocas)
    {
        this.erro = erro;
        this.epocas = epocas;
    }

    public Label getErro() { return erro; }

    public void setErro(Label erro) { this.erro = erro; }

    public Label getEpocas() { return epocas; }

    public void setEpocas(Label epocas) { this.epocas = epocas; }
}
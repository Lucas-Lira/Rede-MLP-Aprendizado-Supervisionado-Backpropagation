package modulos;

public class Neuronio
{
    private double net, INet, erro;

    public Neuronio(){
        this.net = 0;
        this.INet = 0;
        this.erro = 0;
    }
    public Neuronio(double net) {
        this.net = net;
        this.INet = 0;
        this.erro = 0;
    }

    public Neuronio(double net, double INet) {
        this.net = net;
        this.INet = INet;
        this.erro = 0;
    }
    
    public Neuronio(double net, double INet, double erro) {
        this.net = net;
        this.INet = INet;
        this.erro = erro;
    }
    
    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }

    public double getINet() {
        return INet;
    }

    public void setINet(double INet) {
        this.INet = INet;
    }

    public double getErro() {
        return erro;
    }

    public void setErro(double erro) {
        this.erro = erro;
    }   
}
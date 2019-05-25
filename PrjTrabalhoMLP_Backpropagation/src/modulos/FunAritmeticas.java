package modulos;

public class FunAritmeticas
{
    public static double linear(double net){
        return net/10.0;
    }
    
    public static double logistica(double net){
        return (1.0/(1.0+(Math.pow(Math.E, -net))));
    }
    
    public static double tangenteHip(double net){
        return Math.tanh(net);
    }
    
    public static double linearDer(){
        return 1.0/10.0;
    }
    
    public static double logisticaDer(double net){
        double vl = logistica(net);
        return (vl * (1.0 - vl));
    }
    
    public static double tangenteHipDer(double net){
        return (1.0 - (Math.pow(tangenteHip(net), 2)));
    }
}
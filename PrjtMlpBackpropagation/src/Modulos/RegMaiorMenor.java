package Modulos;

public class RegMaiorMenor
{
    private double maior, menor;

    public RegMaiorMenor() {
        this.maior = 0;
        this.menor = 0;
    }

    public RegMaiorMenor(double maior, double menor) {
        this.maior = maior;
        this.menor = menor;
    }
    
    public double getMaior() {
        return maior;
    }

    public void setMaior(double maior) {
        this.maior = maior;
    }

    public double getMenor() {
        return menor;
    }

    public void setMenor(double menor) {
        this.menor = menor;
    }
    
}

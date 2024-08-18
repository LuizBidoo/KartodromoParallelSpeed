import java.util.Random;

public class Pilot extends Thread {
    private Random gerador = new Random();
    private int idade;
    private String nome;
    private RaceTrack raceTrack;
    private boolean hasKart;
    private boolean hasCapacete;

    public Pilot(String nome, RaceTrack raceTrack) {
        this.nome = nome;
        this.idade = gerador.nextInt(5, 61); // 5 <= IDADE <= 60
        this.raceTrack = raceTrack;
        this.hasCapacete = false;
        this.hasKart = false;
    }

    public void setHasCapacete(boolean hasCapacete) {
        this.hasCapacete = hasCapacete;
    }

    public void setHasKart(boolean hasKart) {
        this.hasKart = hasKart;
    }

    @Override
    public void run() {
        pilotoAdquirindoRecursos();
    }

    public void pilotoAdquirindoRecursos() {
        long startWaitTime = System.currentTimeMillis();

        try {
            if (idade <= 14) { // prioridade no capacete
                if (raceTrack.adquirirCapacete(this)) {
                    System.out.println("Corredor " + this.nome + " pegou um capacete");
                    if (raceTrack.adquirirKart(this)) {
                        System.out.println("Corredor " + this.nome + " pegou um kart");
                        // Simular o tempo de corrida
                        Thread.sleep(1000); // Simula a corrida por 2 segundos
                    } else {
                        raceTrack.releaseHelmet(this);
                        System.out.println("Corredor " + this.nome + " não pegou um kart");
                    }
                }
            } else if (idade >= 18) { // prioridade no kart
                if (raceTrack.adquirirKart(this)) {
                    System.out.println("Corredor " + this.nome + " pegou um kart");
                    if (raceTrack.adquirirCapacete(this)) {
                        System.out.println("Corredor " + this.nome + " pegou um capacete");
                        // Simular o tempo de corrida
                        Thread.sleep(1000); // Simula a corrida por 2 segundos
                    } else {
                        raceTrack.releaseKart(this);
                        System.out.println("Corredor " + this.nome + " não pegou um capacete");
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long endWaitTime = System.currentTimeMillis();
            long waitTime = endWaitTime - startWaitTime;
            // Liberar os recursos após a corrida
            if (hasCapacete) {
                raceTrack.releaseHelmet(this);
            }
            if (hasKart) {
                raceTrack.releaseKart(this);
            }
            // Atualizar tempo de espera e outros dados, se necessário
        }
    }
}

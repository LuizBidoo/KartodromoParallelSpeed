import java.util.Random;

public class Pilot extends Thread {
    private Random gerador = new Random();
    private int idade;
    private String nome;
    private RaceTrack raceTrack;
    private boolean hasKart;
    private boolean hasCapacete;
    private long waitStartTime;

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
        waitStartTime = System.currentTimeMillis();
        pilotoAdquirindoRecursos();
    }

    public void pilotoAdquirindoRecursos() {
        try {
            if (idade <= 14) { // prioridade no capacete
                if (raceTrack.adquirirCapacete(this)) {
                    if (raceTrack.adquirirKart(this)) {
                        // Simular o tempo de corrida
                        Thread.sleep(gerador.nextInt(2000, 5000));
                    } else {
                        raceTrack.releaseHelmet(this);
                    }
                }
            } else if (idade >= 18) { // prioridade no kart
                if (raceTrack.adquirirKart(this)) {
                    if (raceTrack.adquirirCapacete(this)) {
                        // Simular o tempo de corrida
                        Thread.sleep(gerador.nextInt(2000, 5000));
                    } else {
                        raceTrack.releaseKart(this);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long waitEndTime = System.currentTimeMillis();
            long waitTime = waitEndTime - waitStartTime;
            if (hasCapacete) {
                raceTrack.releaseHelmet(this);
            }
            if (hasKart) {
                raceTrack.releaseKart(this);
            }
            raceTrack.updateWaitTime(waitTime);
        }
    }
}
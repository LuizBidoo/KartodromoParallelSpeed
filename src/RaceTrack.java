import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    private List<Kart> karts;
    private List<Helmet> capacetes;
    private ReentrantLock kartLock;
    private ReentrantLock capaceteLock;
    private Condition kartCondition;
    private Condition capaceteCondition;

    // Estatísticas
    private int totalClientesAtendidos;
    private long tempoTotalDeEspera;
    private int clientesNaFila;
    private int totalKartsUsados;
    private int totalCapacetesUsados;

    public RaceTrack(int numeroDeKarts, int numeroDeCapacetes) {
        karts = new ArrayList<>(numeroDeKarts);
        capacetes = new ArrayList<>(numeroDeCapacetes);
        kartLock = new ReentrantLock();
        capaceteLock = new ReentrantLock();
        kartCondition = kartLock.newCondition();
        capaceteCondition = capaceteLock.newCondition();

        for (int i = 0; i < numeroDeKarts; i++) {
            karts.add(new Kart());
        }
        for (int i = 0; i < numeroDeCapacetes; i++) {
            capacetes.add(new Helmet());
        }

        // Inicializa estatísticas
        totalClientesAtendidos = 0;
        tempoTotalDeEspera = 0;
        clientesNaFila = 0;
        totalKartsUsados = 0;
        totalCapacetesUsados = 0;
    }

    public boolean adquirirKart(Pilot pilot) {
        kartLock.lock();
        try {
            while (karts.stream().noneMatch(Kart::acquireKart)) {
                clientesNaFila++;
                kartCondition.await(); // Espera por um kart disponível
            }
            pilot.setHasKart(true);
            totalKartsUsados++;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            kartLock.unlock();
        }
    }

    public boolean adquirirCapacete(Pilot pilot) {
        capaceteLock.lock();
        try {
            while (capacetes.stream().noneMatch(Helmet::acquireHelmet)) {
                clientesNaFila++;
                capaceteCondition.await(); // Espera por um capacete disponível
            }
            pilot.setHasCapacete(true);
            totalCapacetesUsados++;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            capaceteLock.unlock();
        }
    }

    public void releaseKart(Pilot pilot) {
        kartLock.lock();
        try {
            karts.stream().filter(kart -> !kart.getIsAvailable()).findFirst().ifPresent(Kart::releaseKart);
            pilot.setHasKart(false);
            totalClientesAtendidos++;
            kartCondition.signal(); // Notifica que um kart foi liberado
        } finally {
            kartLock.unlock();
        }
    }

    public void releaseHelmet(Pilot pilot) {
        capaceteLock.lock();
        try {
            capacetes.stream().filter(capacete -> !capacete.getIsAvailable()).findFirst().ifPresent(Helmet::releaseHelmet);
            pilot.setHasCapacete(false);
            totalClientesAtendidos++;
            capaceteCondition.signal(); // Notifica que um capacete foi liberado
        } finally {
            capaceteLock.unlock();
        }
    }

    public synchronized void updateWaitTime(long waitTime) {
        tempoTotalDeEspera += waitTime;
    }

    public void simulaDia(long tempoSimulacao) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + tempoSimulacao;

        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(200); // Ajuste o tempo conforme a necessidade
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        geraReport();
    }

    public void geraReport() {
        System.out.println("Relatório do Dia:");
        System.out.println("Total de Clientes Atendidos: " + totalClientesAtendidos);
        long tempoMedioDeEspera = totalClientesAtendidos > 0 ? tempoTotalDeEspera / totalClientesAtendidos : 0;
        System.out.println("Tempo Médio de Espera: " + tempoMedioDeEspera + " ms");
        System.out.println("Clientes na Fila Não Atendidos: " + clientesNaFila);
        System.out.println("Quantidade de Karts Utilizados: " + totalKartsUsados);
        System.out.println("Quantidade de Capacetes Utilizados: " + totalCapacetesUsados);
    }
}

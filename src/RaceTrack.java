import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    private List<Kart> karts;
    private List<Helmet> capacetes;
    private ReentrantLock kartLock;
    private ReentrantLock capaceteLock;

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
            for (Kart kart : karts) {
                if (kart.acquireKart()) {
                    pilot.setHasKart(true);
                    totalKartsUsados++;
                    return true;
                }
            }
            // Atualizar a fila de clientes não atendidos
            clientesNaFila++;
            return false;
        } finally {
            kartLock.unlock();
        }
    }

    public boolean adquirirCapacete(Pilot pilot) {
        capaceteLock.lock();
        try {
            for (Helmet capacete : capacetes) {
                if (capacete.acquireHelmet()) {
                    pilot.setHasCapacete(true);
                    totalCapacetesUsados++;
                    return true;
                }
            }
            // Atualizar a fila de clientes não atendidos
            clientesNaFila++;
            return false;
        } finally {
            capaceteLock.unlock();
        }
    }

    public void releaseKart(Pilot pilot) {
        kartLock.lock();
        try {
            for (Kart kart : karts) {
                if (!kart.getIsAvailable()) {
                    kart.releaseKart();
                    pilot.setHasKart(false);
                    totalClientesAtendidos++;
                    return;
                }
            }
        } finally {
            kartLock.unlock();
        }
    }

    public void releaseHelmet(Pilot pilot) {
        capaceteLock.lock();
        try {
            for (Helmet capacete : capacetes) {
                if (!capacete.getIsAvailable()) {
                    capacete.releaseHelmet();
                    pilot.setHasCapacete(false);
                    totalClientesAtendidos++;
                    return;
                }
            }
        } finally {
            capaceteLock.unlock();
        }
    }

    public void simulaDia(long tempoSimulacao) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + tempoSimulacao;

        while (System.currentTimeMillis() < endTime) {
            // Simula o funcionamento do dia
            try {
                Thread.sleep(100); // Ajustar o tempo conforme necessário
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        geraReport();
    }

    public void geraReport() {
        System.out.println("Relatório do Dia:");
        System.out.println("Total de Clientes Atendidos: " + totalClientesAtendidos);
        long tempoMedioDeEspera = totalClientesAtendidos > 0 ? (tempoTotalDeEspera / totalClientesAtendidos) : 0;
        System.out.println("Tempo Médio de Espera: " + tempoMedioDeEspera + " ms");
        System.out.println("Clientes na Fila Não Atendidos: " + clientesNaFila);
        System.out.println("Quantidade de Karts Utilizados: " + totalKartsUsados);
        System.out.println("Quantidade de Capacetes Utilizados: " + totalCapacetesUsados);
    }
}


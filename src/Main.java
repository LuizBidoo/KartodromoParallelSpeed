import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String[] names = {
                "Alfredo", "Ana", "Bruno", "Carlos", "Cecilia", "Diana", "Eduardo",
                "Fernanda", "Gabriel", "Helena", "Igor", "Julia", "Junior",
                "Kaique", "Laura", "Lucas", "Luiz", "Marcelo", "Mateus", "Nina", "Otavio",
                "Patricia", "Quiteria", "Raimundo", "Roberval", "Rodrigo", "Sofia",
                "Thiago", "Ursula", "Vitoria", "Vinicius", "Wellington", "Xuxa", "Yara",
                "Zé"
        };

        Random geradorNome = new Random();

        // Inicializar recursos (karts e capacetes)
        RaceTrack raceTrack = new RaceTrack(10, 10); // 10 karts e 10 capacetes

        // Cria uma lista para armazenar os pilotos
        List<Pilot> pilots = new ArrayList<>();

        int numberOfPilots = 25;

        // Cria e inicia as threads para cada piloto
        for (int i = 0; i < numberOfPilots; i++) {
            int numAleatorio = geradorNome.nextInt(names.length);
            String pilotName = names[numAleatorio];
            Pilot pilot = new Pilot(pilotName, raceTrack);
            pilots.add(pilot);
            pilot.start();
        }

        // Aguarda a conclusão de todas as threads
        for (Pilot pilot : pilots) {
            try {
                pilot.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Simula o funcionamento do dia
        raceTrack.simulaDia(60_000); // Simula 1 minuto para testes
    }
}

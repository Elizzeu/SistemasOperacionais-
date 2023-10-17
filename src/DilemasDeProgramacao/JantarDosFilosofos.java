package DilemasDeProgramacao;
import java.util.concurrent.Semaphore;

public class JantarDosFilosofos {

    private static final int NUM_FILOSOFOS = 5;
    private final Semaphore[] garfos;

    public JantarDosFilosofos() {
        garfos = new Semaphore[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Semaphore(1);
        }
    }
    //O filosofo tenta pegar garfo direito/esquedo se n, fica bloqueado
    private void pegarGarfos(int id) throws InterruptedException {
        garfos[id].acquire(); // Pega garfo esquerdo
        garfos[(id + 1) % garfos.length].acquire(); // Pega garfo direito
    }

    private void liberarGarfos(int id) {
        garfos[id].release(); // Libera garfo esquerdo
        garfos[(id + 1) % garfos.length].release(); // Libera garfo direito
    }
    //comportamento do filosofo 
    private void filosofoAcao(int id) {
        try {
            while (true) {
                System.out.println("Filósofo " + id + " está pensando.");
                pegarGarfos(id);
                System.out.println("Filósofo " + id + " está comendo.");
                liberarGarfos(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void iniciarJantar() {
        Thread[] filosofos = new Thread[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            final int id = i;
            filosofos[i] = new Thread(() -> filosofoAcao(id));
            filosofos[i].start();
        }

        // Aguardar a conclusão das threads
        for (Thread filosofo : filosofos) {
            try {
                filosofo.join();//espera o termini da thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        JantarDosFilosofos jantar = new JantarDosFilosofos();
        jantar.iniciarJantar();
    }
}


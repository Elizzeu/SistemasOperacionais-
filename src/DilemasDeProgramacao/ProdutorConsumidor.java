package DilemasDeProgramacao;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProdutorConsumidor {

    private static final int BUFFER_SIZE = 10;
    private static int[] buffer = new int[BUFFER_SIZE]; //representa o buffer
    private static int id = 0;

    private static Semaphore empty = new Semaphore(BUFFER_SIZE); //Tamanho do buffer
    private static Semaphore full = new Semaphore(0); //espaços preenchidos
    private static Semaphore mutex = new Semaphore(1); //acesso ao buffer

    private static boolean correndo = true;

    public static void main(String[] args) {
        Thread produtorThread = new Thread(ProdutorConsumidor::produtor);
        Thread consumidorThread = new Thread(ProdutorConsumidor::consumidor);

        produtorThread.start();
        consumidorThread.start();

        // Aguarda um tempo antes de interromper as threads (apenas para demonstração)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrompe as threads e encerra o programa
        correndo = false;
        produtorThread.interrupt();
        consumidorThread.interrupt();
    }

    public static void produtor() {
        Random rand = new Random();

        while (correndo) {
            try {
                // Produz um item
                int data = rand.nextInt(100);

                // Aguarda até que o buffer esteja vazio
                empty.acquire();

                // Entra na seção crítica (buffer)
                mutex.acquire();

                // Insere o item no buffer
                buffer[id] = data;
                id++;

                // Sai da seção crítica (buffer)
                mutex.release();

                // Avisa que o buffer está cheio
                full.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void consumidor() {
        while (correndo) {
            try {
                // Aguarda até que o buffer esteja cheio
                full.acquire();

                // Entra na seção crítica (buffer)
                mutex.acquire();

                // Remove o item mais antigo do buffer
                id--;
                int data = buffer[id];

                // Sai da seção crítica (buffer)
                mutex.release();

                // Avisa que o buffer está vazio
                empty.release();

                // Consome o item
                System.out.println("Item consumido: " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}





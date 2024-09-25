package u3c15_ej11;
class proceso implements Runnable {
    private String nameHilo; // Almacena el nombre del hilo para identificarlo
    public Thread hilo; // Objeto de tipo Thread, que representa el hilo
    private boolean suspendFlag; // Bandera que indica si el hilo está suspendido

    // Método estático que crea y comienza un hilo
    public static proceso createAndStart(String nameHilo) {
        proceso myHilo = new proceso(nameHilo); // Crea un nuevo objeto proceso
        myHilo.hilo.start(); // Inicia el hilo automáticamente
        return myHilo; // Retorna la referencia al hilo creado
    }

    // Constructor que inicializa el nombre del hilo y crea el Thread
    public proceso(String nameHilo_) {
        this.nameHilo = nameHilo_; // Asigna el nombre del hilo
        this.hilo = new Thread(this, nameHilo); // Crea un nuevo Thread con este objeto y el nombre
        System.out.println("Nuevo Hilo: " + hilo); // Imprime el nombre del hilo creado
        this.suspendFlag = false; // Inicialmente el hilo no está suspendido
    }

    // Método que define el comportamiento del hilo cuando se ejecuta
    public void run() {
        try {
            // Bucle que ejecuta 15 iteraciones, simulando trabajo en el hilo
            for (int i = 15; i > 0; i--) {
                System.out.println(nameHilo + ": " + i); // Imprime el contador del hilo
                Thread.sleep(200); // Detiene el hilo por 200ms simulando tiempo de procesamiento

                // Sincronización para manejar la suspensión del hilo
                synchronized(this) {
                    while (suspendFlag) { // Si la bandera está en true, el hilo espera
                        wait(); // El hilo entra en espera hasta que se le notifique que puede continuar
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(nameHilo + " Interrumpido"); // Mensaje en caso de interrupción
        }
        System.out.println(nameHilo + " Finalizado"); // Mensaje cuando el hilo termina
    }

    // Método sincronizado para suspender el hilo
    public synchronized void suspend() {
        this.suspendFlag = true; // Cambia la bandera a true, lo que suspende el hilo
    }

    // Método sincronizado para reanudar el hilo
    public synchronized void resume() {
        this.suspendFlag = false; // Cambia la bandera a false para continuar la ejecución
        notify(); // Notifica al hilo que puede salir de la espera
    }
}

public class ejemplo11 {
    public static void main(String[] args) throws InterruptedException {
        // Creación y arranque de dos hilos
        proceso hilo1 = proceso.createAndStart("Hilo 1"); // Crea e inicia "Hilo 1"
        proceso hilo2 = proceso.createAndStart("Hilo 2"); // Crea e inicia "Hilo 2"
        
        try {
            Thread.sleep(1000); // Pausa el hilo principal por 1 segundo
            
            hilo1.suspend(); // Suspende "Hilo 1"
            System.out.println("Hilo 1 Suspendido");
            
            Thread.sleep(1000); // Pausa antes de reanudar el hilo
            
            hilo1.resume(); // Reanuda "Hilo 1"
            System.out.println("Hilo 1 Reanimado");
            
            hilo2.suspend(); // Suspende "Hilo 2"
            System.out.println("Hilo 2 suspendido");
            
            Thread.sleep(1000); // Pausa antes de reanudar el hilo
            
            hilo2.resume(); // Reanuda "Hilo 2"
            System.out.println("Hilo 2 Reanimado");
            
            System.out.println("Esperando que todos los hilos finalicen");
            
            // Espera que los hilos terminen su ejecución
            hilo1.hilo.join(); // Espera la terminación de "Hilo 1"
            hilo2.hilo.join(); // Espera la terminación de "Hilo 2"
            
        } catch (InterruptedException e) {
            e.printStackTrace(); // Maneja la excepción en caso de interrupción
        }

        System.out.println("Hilo principal Finalizado"); // Mensaje final del hilo principal
    }
}
			
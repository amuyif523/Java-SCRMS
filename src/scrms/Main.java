package scrms;

import scrms.controller.ConsoleController;
import scrms.service.ServiceRegistry;

/**
 * Application entry point for SCRMS.
 */
public class Main {

    public static void main(String[] args) {
        ServiceRegistry registry = new ServiceRegistry();
        ConsoleController controller = new ConsoleController(registry);
        controller.start();
    }
}

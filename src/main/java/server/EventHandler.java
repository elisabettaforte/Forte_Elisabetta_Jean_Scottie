package server;

import java.io.IOException;

/**
 * Ceci est une interface qui définit la méthode handle() qui sera implémentée par les gestionnaires d'événements.
 */
@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg) throws IOException, ClassNotFoundException;
}

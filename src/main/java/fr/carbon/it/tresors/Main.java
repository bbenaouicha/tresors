package fr.carbon.it.tresors;

import fr.carbon.it.tresors.io.InputParser;
import fr.carbon.it.tresors.io.OutputWriter;
import fr.carbon.it.tresors.model.DonneesCarte;
import fr.carbon.it.tresors.service.SimulationService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class Main {

    private static final String FICHIER_ENTREE = "/carte.txt";
    private static final Path FICHIER_SORTIE = Path.of("resultat.txt");

    public static void main(String[] args) {
        try {
            executerSimulation();
            System.out.println(
                    "Simulation terminée. Résultat écrit dans "
                            + FICHIER_SORTIE.toAbsolutePath()
            );
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println(
                    "Erreur lors de la simulation : " + exception.getMessage()
            );
        }
    }

    private static void executerSimulation() throws IOException {
        DonneesCarte donnees = lireDonnees();
        simuler(donnees);
        ecrireResultat(donnees);
    }

    private static DonneesCarte lireDonnees() throws IOException {
        InputParser parser = new InputParser();

        try (InputStream input =
                     Main.class.getResourceAsStream(FICHIER_ENTREE)) {

            if (input == null) {
                throw new IllegalArgumentException(
                        "Le fichier carte.txt est introuvable dans les ressources"
                );
            }

            return parser.parser(input);
        }
    }

    private static void simuler(DonneesCarte donnees) {
        SimulationService simulation = new SimulationService();
        simulation.simuler(
                donnees.carte(),
                donnees.aventuriers()
        );
    }

    private static void ecrireResultat(DonneesCarte donnees)
            throws IOException {
        OutputWriter writer = new OutputWriter();
        writer.ecrire(
                FICHIER_SORTIE,
                donnees.carte(),
                donnees.aventuriers()
        );
    }
}
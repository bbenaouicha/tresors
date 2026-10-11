package fr.carbon.it.tresors;

import fr.carbon.it.tresors.io.InputParser;
import fr.carbon.it.tresors.io.OutputWriter;
import fr.carbon.it.tresors.model.DonneesCarte;
import fr.carbon.it.tresors.service.SimulationService;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimulationIntegrationTest {

    @Test
    void GIVEN_CarteSimple_WHEN_SimulationEstExecutee_THEN_AventurierAvanceVersLeNord() throws Exception {
        // Given : un fichier contenant une carte simple
        List<String> resultatAttendu = List.of(
                "C - 3 - 3",
                "A - Indiana - 1 - 0 - N - 0"
        );

        // When : le fichier est lu et la simulation est exécutée
        List<String> resultat = executerSimulation("carte-simple.txt");

        // Then : l'aventurier a avancé vers le nord
        assertEquals(resultatAttendu, resultat);
    }

    @Test
    void GIVEN_CarteAvecMontagne_WHEN_AventurierAvance_THEN_IlResteSurPlace() throws Exception {
        // Given : une montagne bloque le déplacement vers le nord
        List<String> resultatAttendu = List.of(
                "C - 3 - 3",
                "M - 1 - 0",
                "A - Indiana - 1 - 1 - N - 0"
        );

        // When : le fichier est lu et la simulation est exécutée
        List<String> resultat = executerSimulation("carte-avec-montagne.txt");

        // Then : l'aventurier reste sur sa position initiale
        assertEquals(resultatAttendu, resultat);
    }

    @Test
    void GIVEN_CarteAvecTresor_WHEN_AventurierAvance_THEN_IlCollecteUnTresor() throws Exception {
        // Given : deux trésors sont présents sur la case suivante
        List<String> resultatAttendu = List.of(
                "C - 3 - 3",
                "T - 2 - 1 - 1",
                "A - Indiana - 2 - 1 - E - 1"
        );

        // When : le fichier est lu et la simulation est exécutée
        List<String> resultat = executerSimulation("carte-avec-tresor.txt");

        // Then : un trésor est collecté et un seul reste sur la carte
        assertEquals(resultatAttendu, resultat);
    }

    private List<String> executerSimulation(String nomFichier) throws Exception {
        // Lecture du fichier d'entrée réel depuis src/test/resources
        String chemin = "/integration/" + nomFichier;

        try (InputStream input = getClass().getResourceAsStream(chemin)) {
            assertNotNull(input, "Fichier introuvable : " + chemin);

            InputParser parser = new InputParser();
            DonneesCarte donnees = parser.parser(input);

            SimulationService simulation = new SimulationService();
            simulation.simuler(
                    donnees.carte(),
                    donnees.aventuriers()
            );

            Path fichierSortie = Path.of(
                    "target",
                    "integration",
                    "resultat-" + nomFichier
            );

            Files.createDirectories(fichierSortie.getParent());

            OutputWriter writer = new OutputWriter();
            writer.ecrire(
                    fichierSortie,
                    donnees.carte(),
                    donnees.aventuriers()
            );

            // Lecture du résultat réellement écrit par le writer
            return Files.readAllLines(fichierSortie);
        }
    }

    @Test
    void GIVEN_CarteComplexe_WHEN_SimulationEstExecutee_THEN_EtatFinalEstCorrect() throws Exception {
        // Given : une carte avec deux aventuriers, des montagnes et des trésors
        List<String> resultatAttendu = List.of(
                "C - 5 - 5",
                "M - 1 - 2",
                "M - 3 - 3",
                "T - 2 - 0 - 1",
                "T - 2 - 2 - 3",
                "A - Indiana - 0 - 1 - O - 1",
                "A - Lara - 3 - 2 - S - 1"
        );

        // When : la simulation est exécutée à partir du fichier réel
        List<String> resultat = executerSimulation("carte-complexe.txt");

        // Then : l'état final correspond au résultat attendu
        assertEquals(resultatAttendu, resultat);
    }
}
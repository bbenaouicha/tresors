package fr.carbon.it.tresors.io;

import fr.carbon.it.tresors.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputWriterTest {

    private final OutputWriter writer = new OutputWriter();

    @TempDir
    Path dossierTemporaire;

    @Test
    void GIVEN_CarteAvecMontagneTresorEtAventurier_WHEN_ResultatEstEcrit_THEN_FichierContientEtatFinal() throws Exception {
        // Given : une carte avec une montagne, un trésor et un aventurier
        Carte carte = new Carte(3, 4);
        carte.ajouterMontagne(new Montagne(new Position(1, 2)));
        carte.ajouterTresor(new Tresor(new Position(2, 1), 3));

        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(0, 0),
                Orientation.NORD,
                List.of(Mouvement.AVANCER)
        );

        Path fichierSortie = dossierTemporaire.resolve("resultat.txt");

        // When : le résultat est écrit dans le fichier
        writer.ecrire(fichierSortie, carte, List.of(aventurier));

        // Then : le fichier contient la carte et l'état final de l'aventurier
        assertTrue(Files.exists(fichierSortie));

        List<String> lignes = Files.readAllLines(fichierSortie);

        assertEquals(
                List.of(
                        "C - 3 - 4",
                        "M - 1 - 2",
                        "T - 2 - 1 - 3",
                        "A - Indiana - 0 - 0 - N - 0"
                ),
                lignes
        );
    }

    @Test
    void GIVEN_TresorEntierementCollecte_WHEN_ResultatEstEcrit_THEN_TresorAbsentDuFichier() throws Exception {
        // Given : une carte dont le trésor a été entièrement collecté
        Carte carte = new Carte(3, 3);
        carte.ajouterTresor(new Tresor(new Position(1, 1), 1));
        carte.collecterTresor(new Position(1, 1));

        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(0, 0),
                Orientation.NORD,
                List.of()
        );

        Path fichierSortie = dossierTemporaire.resolve("resultat-sans-tresor.txt");

        // When : le résultat est écrit
        writer.ecrire(fichierSortie, carte, List.of(aventurier));

        // Then : aucun trésor épuisé n'apparaît dans le fichier
        List<String> lignes = Files.readAllLines(fichierSortie);

        assertTrue(lignes.stream().noneMatch(ligne -> ligne.startsWith("T - ")));
        assertTrue(lignes.contains("A - Indiana - 0 - 0 - N - 0"));
    }
}
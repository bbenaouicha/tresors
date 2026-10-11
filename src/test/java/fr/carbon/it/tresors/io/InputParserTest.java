package fr.carbon.it.tresors.io;

import fr.carbon.it.tresors.model.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    private final InputParser parser = new InputParser();

    @Test
    void GIVEN_FichierEntreeValide_WHEN_ParserEstAppele_THEN_CarteEtAventurierSontCrees() throws Exception {
        // Given : un fichier d'entrée contenant une carte complète
        String contenu = """
                # Carte aux trésors
                C - 3 - 4
                M - 1 - 2
                T - 2 - 1 - 3
                A - Indiana - 1 - 1 - S - AADADA
                """;

        ByteArrayInputStream input = new ByteArrayInputStream(
                contenu.getBytes(StandardCharsets.UTF_8)
        );

        // When : le fichier est analysé
        DonneesCarte donnees = parser.parser(input);

        // Then : la carte, la montagne, le trésor et l'aventurier sont créés
        assertEquals(3, donnees.carte().getLargeur());
        assertEquals(4, donnees.carte().getHauteur());

        assertEquals(1, donnees.carte().getMontagnes().size());
        assertEquals(
                new Position(1, 2),
                donnees.carte().getMontagnes().getFirst().position()
        );

        assertEquals(1, donnees.carte().getTresors().size());
        Tresor tresor = donnees.carte().getTresors().getFirst();
        assertEquals(new Position(2, 1), tresor.getPosition());
        assertEquals(3, tresor.getQuantite());

        assertEquals(1, donnees.aventuriers().size());
        Aventurier aventurier = donnees.aventuriers().getFirst();
        assertEquals("Indiana", aventurier.getNom());
        assertEquals(new Position(1, 1), aventurier.getPosition());
        assertEquals(Orientation.SUD, aventurier.getOrientation());
        assertEquals(List.of(Mouvement.AVANCER,Mouvement.AVANCER,Mouvement.DROITE,Mouvement.AVANCER,Mouvement.DROITE,Mouvement.AVANCER), aventurier.getMouvements());
    }

    @Test
    void GIVEN_FichierSansCarte_WHEN_ParserEstAppele_THEN_UneExceptionEstLevee() {
        // Given : un fichier contenant uniquement un commentaire
        String contenu = "# Aucun élément dans ce fichier";

        ByteArrayInputStream input = new ByteArrayInputStream(
                contenu.getBytes(StandardCharsets.UTF_8)
        );

        // When / Then : le parser signale l'absence de carte
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parser(input)
        );

        assertEquals("Carte absente du fichier", exception.getMessage());
    }
}
package fr.carbon.it.tresors.service;


import fr.carbon.it.tresors.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimulationServiceTest {

    private final SimulationService simulation = new SimulationService();

    @Test
    void GIVEN_AventurierOrienteVersLeNord_WHEN_IlAvance_THEN_IlSeDeplaceVersLeNord() {
        // Given : une carte et un aventurier orienté vers le nord
        Carte carte = new Carte(3, 3);
        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(1, 1),
                Orientation.NORD,
                List.of(Mouvement.AVANCER)
        );

        // When : la simulation est exécutée
        simulation.simuler(carte, List.of(aventurier));

        // Then : l'aventurier avance d'une case vers le nord
        assertEquals(new Position(1, 0), aventurier.getPosition());
        assertEquals(Orientation.NORD, aventurier.getOrientation());
    }

    @Test
    void GIVEN_AventurierOrienteVersLeNord_WHEN_IlTourneADroite_THEN_IlEstOrienteVersLEst() {
        // Given : un aventurier orienté vers le nord
        Carte carte = new Carte(3, 3);
        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(1, 1),
                Orientation.NORD,
                List.of(Mouvement.DROITE)
        );

        // When : l'aventurier tourne à droite
        simulation.simuler(carte, List.of(aventurier));

        // Then : il est orienté vers l'est sans changer de position
        assertEquals(new Position(1, 1), aventurier.getPosition());
        assertEquals(Orientation.EST, aventurier.getOrientation());
    }

    @Test
    void GIVEN_AventurierAuBordNord_WHEN_IlAvance_THEN_IlResteSurPlace() {
        // Given : un aventurier placé au bord nord de la carte
        Carte carte = new Carte(3, 3);
        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(0, 0),
                Orientation.NORD,
                List.of(Mouvement.AVANCER)
        );

        // When : l'aventurier tente d'avancer hors de la carte
        simulation.simuler(carte, List.of(aventurier));

        // Then : sa position et son orientation restent inchangées
        assertEquals(new Position(0, 0), aventurier.getPosition());
        assertEquals(Orientation.NORD, aventurier.getOrientation());
    }

    @Test
    void GIVEN_MontagneDevantAventurier_WHEN_IlAvance_THEN_IlResteSurPlace() {
        // Given : une montagne se trouve devant l'aventurier
        Carte carte = new Carte(3, 3);
        carte.ajouterMontagne(
                new Montagne(new Position(1, 0))
        );

        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(1, 1),
                Orientation.NORD,
                List.of(Mouvement.AVANCER)
        );

        // When : l'aventurier tente d'avancer
        simulation.simuler(carte, List.of(aventurier));

        // Then : il ne peut pas franchir la montagne
        assertEquals(new Position(1, 1), aventurier.getPosition());
    }

    @Test
    void GIVEN_TresorSurCaseSuivante_WHEN_AventurierAvance_THEN_IlCollecteUnTresor() {
        // Given : deux trésors se trouvent sur la case suivante
        Carte carte = new Carte(3, 3);
        Tresor tresor = new Tresor(new Position(1, 1), 2);
        carte.ajouterTresor(tresor);

        Aventurier aventurier = new Aventurier(
                "Indiana",
                new Position(0, 1),
                Orientation.EST,
                List.of(Mouvement.AVANCER)
        );

        // When : l'aventurier entre sur la case contenant les trésors
        simulation.simuler(carte, List.of(aventurier));

        // Then : un seul trésor est collecté et il en reste un
        assertEquals(1, aventurier.getTresorsCollectes());
        assertEquals(1, tresor.getQuantite());
    }

    @Test
    void GIVEN_DeuxAventuriersVisantLaMemeCase_WHEN_IlsAvancent_THEN_LePremierEstPrioritaire() {
        // Given : deux aventuriers visent la case centrale
        Carte carte = new Carte(3, 1);

        Aventurier premier = new Aventurier(
                "Indiana",
                new Position(0, 0),
                Orientation.EST,
                List.of(Mouvement.AVANCER)
        );

        Aventurier second = new Aventurier(
                "Lara",
                new Position(2, 0),
                Orientation.OUEST,
                List.of(Mouvement.AVANCER)
        );

        // When : la simulation traite les aventuriers dans l'ordre du fichier
        simulation.simuler(carte, List.of(premier, second));

        // Then : le premier occupe la case centrale, le second reste sur place
        assertEquals(new Position(1, 0), premier.getPosition());
        assertEquals(new Position(2, 0), second.getPosition());
    }
}
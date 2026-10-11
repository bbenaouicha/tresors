package fr.carbon.it.tresors.service;

import fr.carbon.it.tresors.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulationService {

    public void simuler(Carte carte, List<Aventurier> aventuriers) {
        verifierParametres(carte, aventuriers);

        List<Aventurier> participants = List.copyOf(aventuriers);
        verifierPositionsInitiales(carte, participants);

        int nombreMaxMouvements = participants.stream()
                .mapToInt(aventurier -> aventurier.getMouvements().size())
                .max()
                .orElse(0);

        for (int tour = 0; tour < nombreMaxMouvements; tour++) {
            for (Aventurier aventurier : participants) {
                executerTour(carte, participants, aventurier, tour);
            }
        }
    }

    private void verifierParametres(
            Carte carte,
            List<Aventurier> aventuriers
    ) {
        if (carte == null || aventuriers == null) {
            throw new IllegalArgumentException(
                    "La carte et les aventuriers sont obligatoires"
            );
        }

        if (aventuriers.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException(
                    "La liste ne peut pas contenir d'aventurier null"
            );
        }
    }

    private void executerTour(
            Carte carte,
            List<Aventurier> aventuriers,
            Aventurier aventurier,
            int indexMouvement
    ) {
        List<Mouvement> mouvements = aventurier.getMouvements();

        if (indexMouvement >= mouvements.size()) {
            return;
        }

        Mouvement mouvement = mouvements.get(indexMouvement);

        switch (mouvement) {
            case GAUCHE ->
                    aventurier.tournerVers(
                            tournerAGauche(aventurier.getOrientation())
                    );

            case DROITE ->
                    aventurier.tournerVers(
                            tournerADroite(aventurier.getOrientation())
                    );

            case AVANCER ->
                    avancer(carte, aventuriers, aventurier);
        }
    }

    private void avancer(
            Carte carte,
            List<Aventurier> aventuriers,
            Aventurier aventurier
    ) {
        Position prochainePosition = calculerProchainePosition(
                aventurier.getPosition(),
                aventurier.getOrientation()
        );

        if (!carte.estAccessible(prochainePosition)
                || estOccupee(prochainePosition, aventuriers, aventurier)) {
            return;
        }

        aventurier.deplacerVers(prochainePosition);

        if (carte.collecterTresor(prochainePosition)) {
            aventurier.ajouterTresor();
        }
    }

    private boolean estOccupee(
            Position position,
            List<Aventurier> aventuriers,
            Aventurier aventurierActuel
    ) {
        return aventuriers.stream()
                .filter(autre -> autre != aventurierActuel)
                .anyMatch(autre -> autre.getPosition().equals(position));
    }

    private Position calculerProchainePosition(
            Position position,
            Orientation orientation
    ) {
        return switch (orientation) {
            case NORD -> new Position(position.x(), position.y() - 1);
            case SUD -> new Position(position.x(), position.y() + 1);
            case EST -> new Position(position.x() + 1, position.y());
            case OUEST -> new Position(position.x() - 1, position.y());
        };
    }

    private Orientation tournerAGauche(Orientation orientation) {
        return switch (orientation) {
            case NORD -> Orientation.OUEST;
            case OUEST -> Orientation.SUD;
            case SUD -> Orientation.EST;
            case EST -> Orientation.NORD;
        };
    }

    private Orientation tournerADroite(Orientation orientation) {
        return switch (orientation) {
            case NORD -> Orientation.EST;
            case EST -> Orientation.SUD;
            case SUD -> Orientation.OUEST;
            case OUEST -> Orientation.NORD;
        };
    }

    private void verifierPositionsInitiales(
            Carte carte,
            List<Aventurier> aventuriers
    ) {
        Set<Position> positions = new HashSet<>();

        for (Aventurier aventurier : aventuriers) {
            Position position = aventurier.getPosition();

            if (!carte.estAccessible(position)) {
                throw new IllegalArgumentException(
                        "Position initiale invalide pour "
                                + aventurier.getNom()
                );
            }

            if (!positions.add(position)) {
                throw new IllegalArgumentException(
                        "Deux aventuriers occupent la même case : "
                                + position
                );
            }
        }
    }
}
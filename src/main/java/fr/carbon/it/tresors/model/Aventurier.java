package fr.carbon.it.tresors.model;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Aventurier {

    private final String nom;
    private Position position;
    private Orientation orientation;
    private final List<Mouvement> mouvements;
    private int tresorsCollectes;

    public Aventurier(
            String nom,
            Position position,
            Orientation orientation,
            List<Mouvement> mouvements
    ) {
        if (nom == null || nom.isBlank()
                || position == null
                || orientation == null
                || mouvements == null) {
            throw new IllegalArgumentException(
                    "Les informations de l'aventurier sont obligatoires"
            );
        }

        this.nom = nom;
        this.position = position;
        this.orientation = orientation;
        this.mouvements = List.copyOf(mouvements);
    }

    public void deplacerVers(Position nouvellePosition) {
        this.position = Objects.requireNonNull(
                nouvellePosition,
                "La nouvelle position ne peut pas être nulle"
        );
    }

    public void tournerVers(Orientation nouvelleOrientation) {
        this.orientation = Objects.requireNonNull(
                nouvelleOrientation,
                "La nouvelle orientation ne peut pas être nulle"
        );
    }

    public void ajouterTresor() {
        tresorsCollectes++;
    }
}
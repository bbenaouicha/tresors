package fr.carbon.it.tresors.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Tresor {

    private final Position position;
    private int quantite;

    public Tresor(Position position, int quantite) {
        validerPosition(position);
        validerQuantite(quantite);

        this.position = position;
        this.quantite = quantite;
    }

    private void validerPosition(Position position) {
        Objects.requireNonNull(
                position,
                "La position du trésor ne peut pas être nulle"
        );
    }

    private void validerQuantite(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException(
                    "La quantité de trésors ne peut pas être négative"
            );
        }
    }

    public boolean estDisponible() {
        return quantite > 0;
    }

    public boolean collecter() {
        if (!estDisponible()) {
            return false;
        }

        quantite--;
        return true;
    }
}
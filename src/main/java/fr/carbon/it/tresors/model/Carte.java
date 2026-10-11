package fr.carbon.it.tresors.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Carte {

    private final int largeur;
    private final int hauteur;

    private final List<Montagne> montagnes;
    private final List<Tresor> tresors;

    public Carte(int largeur, int hauteur) {
        validerDimensions(largeur, hauteur);

        this.largeur = largeur;
        this.hauteur = hauteur;
        this.montagnes = new ArrayList<>();
        this.tresors = new ArrayList<>();
    }

    private void validerDimensions(int largeur, int hauteur) {
        if (largeur <= 0 || hauteur <= 0) {
            throw new IllegalArgumentException(
                    "La largeur et la hauteur doivent être positives"
            );
        }
    }

    public void ajouterMontagne(Montagne montagne) {
        Objects.requireNonNull(montagne, "La montagne ne peut pas être nulle");

        Position position = montagne.position();

        if (!estDansLesLimites(position)) {
            throw new IllegalArgumentException(
                    "La montagne est en dehors de la carte"
            );
        }

        if (estUneMontagne(position)) {
            throw new IllegalArgumentException(
                    "Une montagne existe déjà sur cette case"
            );
        }

        montagnes.add(montagne);
    }

    public void ajouterTresor(Tresor tresor) {
        Objects.requireNonNull(tresor, "Le trésor ne peut pas être nul");

        Position position = tresor.getPosition();

        if (!estDansLesLimites(position)) {
            throw new IllegalArgumentException(
                    "Le trésor est en dehors de la carte"
            );
        }

        if (estUneMontagne(position)) {
            throw new IllegalArgumentException(
                    "Un trésor ne peut pas être placé sur une montagne"
            );
        }

        tresors.add(tresor);
    }

    public boolean estDansLesLimites(Position position) {
        return position != null
                && position.x() >= 0
                && position.x() < largeur
                && position.y() >= 0
                && position.y() < hauteur;
    }

    public boolean estUneMontagne(Position position) {
        return position != null
                && montagnes.stream()
                .anyMatch(montagne -> montagne.position().equals(position));
    }

    public boolean estAccessible(Position position) {
        return estDansLesLimites(position)
                && !estUneMontagne(position);
    }

    public boolean collecterTresor(Position position) {
        if (position == null) {
            return false;
        }

        return tresors.stream()
                .filter(tresor -> tresor.getPosition().equals(position))
                .filter(Tresor::estDisponible)
                .findFirst()
                .map(Tresor::collecter)
                .orElse(false);
    }
}
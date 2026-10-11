package fr.carbon.it.tresors.io;

import fr.carbon.it.tresors.model.Aventurier;
import fr.carbon.it.tresors.model.Carte;
import fr.carbon.it.tresors.model.DonneesCarte;
import fr.carbon.it.tresors.model.Montagne;
import fr.carbon.it.tresors.model.Mouvement;
import fr.carbon.it.tresors.model.Orientation;
import fr.carbon.it.tresors.model.Position;
import fr.carbon.it.tresors.model.Tresor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputParser {

    public DonneesCarte parser(InputStream input) throws IOException {
        Objects.requireNonNull(input, "Le fichier d'entrée est obligatoire");

        Carte carte = null;
        List<Aventurier> aventuriers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {

            String ligne;
            int numeroLigne = 0;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                try {
                    String[] champs = ligne.split("\\s*-\\s*");

                    switch (champs[0]) {
                        case "C" -> carte = parserCarte(champs, carte);
                        case "M" -> ajouterMontagne(carte, champs, ligne);
                        case "T" -> ajouterTresor(carte, champs, ligne);
                        case "A" -> ajouterAventurier(
                                carte, champs, ligne, aventuriers
                        );
                        default -> throw new IllegalArgumentException(
                                "Type de ligne inconnu : " + ligne
                        );
                    }
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException(
                            "Erreur à la ligne " + numeroLigne + " : "
                                    + exception.getMessage(),
                            exception
                    );
                }
            }
        }

        if (carte == null) {
            throw new IllegalArgumentException("Carte absente du fichier");
        }

        return new DonneesCarte(carte, aventuriers);
    }

    private Carte parserCarte(String[] champs, Carte carte) {
        verifierNombreChamps(champs, 3, "carte");

        if (carte != null) {
            throw new IllegalArgumentException("La carte est déjà définie");
        }

        int largeur = Integer.parseInt(champs[1]);
        int hauteur = Integer.parseInt(champs[2]);

        return new Carte(largeur, hauteur);
    }

    private void ajouterMontagne(
            Carte carte,
            String[] champs,
            String ligne
    ) {
        verifierCarteExiste(carte, ligne);
        verifierNombreChamps(champs, 3, "montagne");

        Position position = creerPosition(champs[1], champs[2]);
        carte.ajouterMontagne(new Montagne(position));
    }

    private void ajouterTresor(
            Carte carte,
            String[] champs,
            String ligne
    ) {
        verifierCarteExiste(carte, ligne);
        verifierNombreChamps(champs, 4, "trésor");

        Position position = creerPosition(champs[1], champs[2]);
        int quantite = Integer.parseInt(champs[3]);

        carte.ajouterTresor(new Tresor(position, quantite));
    }

    private void ajouterAventurier(
            Carte carte,
            String[] champs,
            String ligne,
            List<Aventurier> aventuriers
    ) {
        verifierCarteExiste(carte, ligne);
        verifierNombreChamps(champs, 6, "aventurier");

        String nom = champs[1];
        Position position = creerPosition(champs[2], champs[3]);
        Orientation orientation = convertirOrientation(champs[4]);
        List<Mouvement> mouvements = convertirMouvements(champs[5]);

        aventuriers.add(
                new Aventurier(nom, position, orientation, mouvements)
        );
    }

    private Position creerPosition(String x, String y) {
        return new Position(
                Integer.parseInt(x),
                Integer.parseInt(y)
        );
    }

    private Orientation convertirOrientation(String code) {
        return switch (code) {
            case "N" -> Orientation.NORD;
            case "S" -> Orientation.SUD;
            case "E" -> Orientation.EST;
            case "O" -> Orientation.OUEST;
            default -> throw new IllegalArgumentException(
                    "Orientation invalide : " + code
            );
        };
    }

    private List<Mouvement> convertirMouvements(String instructions) {
        List<Mouvement> mouvements = new ArrayList<>();

        for (char instruction : instructions.toCharArray()) {
            mouvements.add(Mouvement.depuisCode(instruction));
        }

        return List.copyOf(mouvements);
    }

    private void verifierCarteExiste(Carte carte, String ligne) {
        if (carte == null) {
            throw new IllegalArgumentException(
                    "La carte doit être définie avant les autres éléments : "
                            + ligne
            );
        }
    }

    private void verifierNombreChamps(
            String[] champs,
            int nombreAttendu,
            String type
    ) {
        if (champs.length != nombreAttendu) {
            throw new IllegalArgumentException(
                    "Format de " + type + " invalide"
            );
        }
    }
}
package fr.carbon.it.tresors.io;

import fr.carbon.it.tresors.model.Aventurier;
import fr.carbon.it.tresors.model.Carte;
import fr.carbon.it.tresors.model.Montagne;
import fr.carbon.it.tresors.model.Orientation;
import fr.carbon.it.tresors.model.Position;
import fr.carbon.it.tresors.model.Tresor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OutputWriter {

    public void ecrire(
            Path fichier,
            Carte carte,
            List<Aventurier> aventuriers
    ) throws IOException {

        List<String> lignes = new ArrayList<>();

        lignes.add("C - " + carte.getLargeur() + " - " + carte.getHauteur());

        for (Montagne montagne : carte.getMontagnes()) {
            Position position = montagne.position();

            lignes.add("M - " + position.x() + " - " + position.y());
        }

        for (Tresor tresor : carte.getTresors()) {
            if (tresor.getQuantite() > 0) {
                Position position = tresor.getPosition();

                lignes.add(
                        "T - " + position.x()
                                + " - " + position.y()
                                + " - " + tresor.getQuantite()
                );
            }
        }

        for (Aventurier aventurier : aventuriers) {
            lignes.add(
                    "A - " + aventurier.getNom()
                            + " - " + aventurier.getPosition().x()
                            + " - " + aventurier.getPosition().y()
                            + " - " + orientationCode(aventurier.getOrientation())
                            + " - " + aventurier.getTresorsCollectes()
            );
        }

        Files.write(fichier, lignes);
    }

    private String orientationCode(Orientation orientation) {
        return switch (orientation) {
            case NORD -> "N";
            case SUD -> "S";
            case EST -> "E";
            case OUEST -> "O";
        };
    }
}
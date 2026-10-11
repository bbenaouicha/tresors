package fr.carbon.it.tresors.model;

import lombok.Getter;

@Getter
public enum Mouvement {
    AVANCER('A'),
    GAUCHE('G'),
    DROITE('D');

    private final char code;

    Mouvement(char code) {
        this.code = code;
    }

    public static Mouvement depuisCode(char code) {
        return switch (code) {
            case 'A' -> AVANCER;
            case 'G' -> GAUCHE;
            case 'D' -> DROITE;
            default -> throw new IllegalArgumentException(
                    "Mouvement inconnu : " + code
            );
        };
    }
}

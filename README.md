# Carte aux trésors

## 1. Présentation du projet

Carte aux trésors est une application Java permettant de simuler les déplacements d'aventuriers sur une carte composée de cases. La carte contient des montagnes et des trésors. Les aventuriers suivent une séquence d'instructions, se déplacent selon leur orientation, évitent les obstacles et collectent les trésors disponibles.

Au lancement de l'application, le fichier carte.txt, situé dans le répertoire des ressources (src/main/resources), est lu et analysé afin de récupérer les informations de la carte, les montagnes, les trésors et les aventuriers. Une fois la simulation terminée, les résultats sont générés dans un fichier resultat.txt à la racine du projet.

Le projet est développé en Java 21, avec Maven pour la gestion du build et JUnit 5 pour les tests automatisés.

## 2. Fonctionnalités

- Lecture et analyse d'un fichier d'entrée.
- Création de la carte, des montagnes, des trésors et des aventuriers.
- Validation des données d'entrée et des positions initiales.
- Déplacement dans les quatre directions : nord, sud, est et ouest.
- Rotation à gauche ou à droite.
- Gestion des obstacles et des limites de la carte.
- Gestion des déplacements de plusieurs aventuriers avec une priorité selon leur ordre d'apparition.
- Collecte des trésors disponibles.
- Génération d'un fichier contenant l'état final de la carte.
- Tests unitaires et tests d'intégration.

## 3. Prérequis

- JDK 21
- Maven ou le Maven Wrapper fourni avec le projet
- IntelliJ IDEA (facultatif)


## 4. Format du fichier d'entrée

Le fichier d'entrée est composé de lignes décrivant la carte et ses éléments. Les lignes vides et les lignes commençant par `#` sont ignorées.

| Type | Format | Description |
|---|---|---|
| Carte | `C - largeur - hauteur` | Définit les dimensions de la carte. |
| Montagne | `M - x - y` | Place une montagne aux coordonnées indiquées. |
| Trésor | `T - x - y - quantité` | Place une quantité de trésors sur une case. |
| Aventurier | `A - nom - x - y - orientation - mouvements` | Définit un aventurier, sa position, son orientation et ses mouvements. |

### Coordonnées

- L'origine `(0, 0)` se trouve en haut à gauche.
- L'axe `x` augmente vers l'est.
- L'axe `y` augmente vers le sud.
- La largeur et la hauteur doivent être positives.
- Les positions initiales des aventuriers doivent se trouver dans les limites de la carte, hors des montagnes et sur des cases distinctes.

### Orientations

| Code | Direction |
|---|---|
| `N` | Nord |
| `S` | Sud |
| `E` | Est |
| `O` | Ouest |

### Instructions

| Code | Action |
|---|---|
| `A` | Avancer d'une case dans la direction actuelle. |
| `G` | Tourner à gauche sans changer de position. |
| `D` | Tourner à droite sans changer de position. |

Un déplacement bloqué est ignoré : l'aventurier conserve sa position et son orientation. Une rotation ne change que l'orientation.

## 5. Exemple de fichier d'entrée

```text
# Carte de 3 colonnes et 4 lignes
C - 3 - 4
M - 1 - 2
T - 2 - 1 - 3
A - Indiana - 1 - 1 - S - AADADA
```

## 6. Règles de simulation

- Les aventuriers sont traités dans l'ordre où ils apparaissent dans le fichier.
- La simulation se déroule par tours : chaque aventurier exécute son prochain mouvement avant de passer au tour suivant.
- Un aventurier ayant terminé ses instructions ne bouge plus.
- Un déplacement hors de la carte est ignoré.
- Un déplacement vers une montagne est ignoré.
- Un aventurier ne peut pas entrer sur une case occupée par un autre aventurier.
- Un déplacement bloqué ne modifie ni la position ni l'orientation de l'aventurier.
- Lorsqu'un aventurier arrive sur une case contenant des trésors, il en collecte un si la quantité restante est supérieure à zéro.
- Un trésor entièrement collecté n'apparaît plus dans le fichier de sortie.

## 7. Format du fichier de sortie

Le fichier `resultat.txt` représente l'état final de la carte.

Il contient :
- `C` : les dimensions de la carte ;
- `M` : les positions des montagnes ;
- `T` : les trésors restants ;
- `A` : le nom, la position finale, l'orientation finale et le nombre de trésors collectés par chaque aventurier.

Exemple :

```text
C - 3 - 4
M - 1 - 2
T - 2 - 1 - 2
A - Indiana - 0 - 0 - N - 1
```

## 8. Architecture du projet

Le code est organisé dans le package `fr.carbon.it.tresors`.

```text
src/
├── main/
│   ├── java/fr/carbon/it/tresors/
│   │   ├── Main.java
│   │   ├── model/
│   │   │   ├── Aventurier.java
│   │   │   ├── Carte.java
│   │   │   ├── DonneesCarte.java
│   │   │   ├── Montagne.java
│   │   │   ├── Mouvement.java
│   │   │   ├── Orientation.java
│   │   │   ├── Position.java
│   │   │   └── Tresor.java
│   │   ├── service/
│   │   │   └── SimulationService.java
│   │   └── io/
│   │       ├── InputParser.java
│   │       └── OutputWriter.java
│   └── resources/
│       └── carte.txt
└── test/
    ├── java/fr/carbon/it/tresors/
    │   ├── SimulationIntegrationTest.java
    │   ├── service/
    │   │   └── SimulationServiceTest.java
    │   └── io/
    │       ├── InputParserTest.java
    │       └── OutputWriterTest.java
    └── resources/integration/
        ├── carte-simple.txt
        ├── carte-avec-montagne.txt
        ├── carte-avec-tresor.txt
        └── carte-complexe.txt
```

### Responsabilités des composants

- **`model`** : représente les éléments métier de la carte.
- **`InputParser`** : lit le fichier d'entrée et construit les objets métier.
- **`SimulationService`** : exécute les mouvements et applique les règles de simulation.
- **`OutputWriter`** : écrit l'état final dans un fichier.
- **`Main`** : orchestre la lecture, la simulation et l'écriture.

## 9. Tests automatisés

Le projet utilise JUnit 5.

### Tests unitaires

Ils vérifient notamment :
- les déplacements et les rotations ;
- les limites de la carte ;
- les déplacements bloqués par les montagnes ;
- la collecte des trésors ;
- la priorité entre aventuriers visant une même case.

### Tests du parser et du writer

Ils vérifient :
- la lecture d'un fichier valide ;
- la gestion d'un fichier sans carte ;
- l'écriture correcte de l'état final ;
- l'omission des trésors entièrement collectés.

### Tests d'intégration

Chaque scénario d'intégration dispose de son propre fichier dans `src/test/resources/integration/`.

Un test d'intégration :
1. Lit le fichier d'entrée.
2. Construit la carte et les aventuriers.
3. Exécute la simulation.
4. Écrit le résultat dans un fichier.
5. Compare le résultat obtenu au résultat attendu.

Pour exécuter l'ensemble des tests :

```powershell
.\mvnw.cmd clean test
```

Les rapports Maven sont disponibles dans :

```text
target/surefire-reports/
```

Les résultats des tests d'intégration sont écrits dans :

```text
target/integration/
```

## 10. Gestion des erreurs

Le programme contrôle notamment :
- les dimensions invalides de la carte ;
- les lignes d'entrée mal formées ;
- les orientations inconnues ;
- les instructions autres que `A`, `G` et `D` ;
- les éléments placés hors de la carte ;
- les montagnes dupliquées ;
- les trésors placés sur une montagne ;
- les positions initiales invalides ou partagées entre plusieurs aventuriers.
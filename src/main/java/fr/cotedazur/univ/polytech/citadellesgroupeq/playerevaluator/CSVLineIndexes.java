package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

/**
 * Regroupe les index dans les lignes de stat du CSV détaillé. Permet d'éviter d'utiliser des magic numbers.
 * Pour accéder à l'index lié à une de ces constantes, il faut utiliser son ordinal.
 */
public enum CSVLineIndexes {
    PLAYER_NAME,
    WIN_NUMBER,
    WIN_PERCENTAGE,
    LOOSE_NUMBER,
    LOOSE_PERCENTAGE,
    TIE_NUMBER,
    TIE_PERCENTAGE,
    GAME_NUMBER;
}

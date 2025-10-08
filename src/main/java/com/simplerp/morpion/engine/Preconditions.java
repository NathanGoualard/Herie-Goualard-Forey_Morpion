package com.simplerp.morpion.engine;

public class Preconditions {
  /**
   * Vérifie si un argument d'une fonction est valide
   * @param expression l'expression à laquelle la valeur doit répondre
   * @param message le message d'erreur si invalide
   */
  public static void checkArgument(boolean expression, String message) {
    if(!expression) throw new IllegalArgumentException(message);
  }
}

package com.simplerp.morpion.engine;

import org.jetbrains.annotations.NotNull;
import static com.simplerp.morpion.engine.Preconditions.checkArgument;

public class MorpionPlateau {

  private final Pion[][] plateau;
  private final int columns;
  private final int rows;
  private final int countToWin;

  public MorpionPlateau(int columns, int rows, int countToWin) {
    this.columns = columns;
    this.rows = rows;
    this.countToWin = countToWin;
    plateau = new Pion[rows][columns];
  }

  public MorpionPlateau(int columns, int rows, int countToWin, Pion[][] plateau) {
    this.columns = columns;
    this.rows = rows;
    this.countToWin = countToWin;
    this.plateau = plateau;
  }

  public void placerPion(int row, int column, @NotNull Pion pion) {
    checkArgument(row >= 0 && row < rows, "Ligne doit être comprise entre 0 et " + (rows-1));
    checkArgument(column >= 0 && column < columns , "La colonne doit être comprise entre 0 et " + (columns-1));
    checkArgument(plateau[row][column] == null, "Pion placé dans une case déjà remplie");

    plateau[row][column] = pion;
  }

  public boolean verifierVictoire(int row, int column, Pion pion) {
      if(verifHorizontale(row, column, pion)) return true;
      if(verifVerticale(row, column, pion)) return true;
      if(verifDiagonaleDroite(row, column, pion)) return true;
      if(verifDiagonaleGauche(row, column, pion)) return true;
    return false;
  }

  public boolean verifHorizontale(int row, int column, Pion pion) {
    int count = 0;
    for(int i = column - countToWin - 1; i < column + countToWin; i++){
      if(i < 0 || i >= rows) continue;
      if(plateau[row][i] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }

  public boolean verifVerticale(int row, int column, Pion pion) {
    int count = 0;
    for(int i = row - countToWin - 1; i < row + countToWin; i++){
      if(i < 0 || i >= rows) continue;
      if(plateau[i][column] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }

  public boolean verifDiagonaleDroite(int row, int column, Pion pion) {
    int count = 0;
    for(int i = row - countToWin - 1; i < row + countToWin; i++){
      if(i < 0 || i >= rows) continue;
      if(plateau[i][i] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }

  public boolean verifDiagonaleGauche(int row, int column, Pion pion) {
    int count = 0;
    for(int i = row + countToWin - 1; i >= 0; i--){
      if(i >= rows) continue;
      if(plateau[i][i] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }
}

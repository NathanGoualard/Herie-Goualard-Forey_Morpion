package com.simplerp.morpion.engine;

import org.jetbrains.annotations.NotNull;

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
    if(row < 0 || row > rows || column < 0 || column > columns) throw new RuntimeException("Pion placé en dehors du plateau");
    if(plateau[row][column] != null) throw new RuntimeException("Pion placé dans une case déjà remplie");

    plateau[row-1][column-1] = pion;
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
    for(int i = row - countToWin-1; i <= row + countToWin-1; i++){
      if(i-1 < 0 || i > rows) continue;
      if(plateau[i-1][column-1] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }

  public boolean verifVerticale(int row, int column, Pion pion) {
    int count = 0;
    for(int i = column - countToWin - 1; i <= column + countToWin - 1; i++){
      if(i-1 < 0 || i > rows) continue;
      if(plateau[row-1][i-1] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }

  public boolean verifDiagonaleDroite(int row, int column, Pion pion) {
    int count = 0;
    int posY = column - countToWin - 1;
    for(int i = row - countToWin - 1; i <= row + countToWin - 1; i++, posY++){
      if(i-1 < 0 || i > rows) continue;
      if(plateau[row-1][posY-1] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }

  public boolean verifDiagonaleGauche(int row, int column, Pion pion) {
    int count = 0;
    int posY = column - countToWin - 1;
    for(int i = row + countToWin - 1; i <= row - countToWin - 1; i--, posY++){
      if(i < 0 || i > rows) continue;
      if(plateau[row-1][posY-1] == pion) ++count;
      if(count == countToWin) return true;
    }
    return false;
  }
}

package com.simplerp.morpion.engine;

import org.junit.jupiter.api.Test;
import static com.simplerp.morpion.engine.Pion.CROIX;
import static com.simplerp.morpion.engine.Pion.ROND;

public class PlateauTest {



  @Test
  public void PlateauVictoireVerticale() {
    Pion[][] pions = new Pion[][] {
            {CROIX, ROND , CROIX},
            {CROIX, null , ROND},
            {CROIX , null , null}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    plateau.verifVerticale(1, 1, CROIX);
  }

  @Test
  public void PlateauVictoireHorizontaleNull() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , ROND},
            {null, CROIX , ROND},
            {null , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    plateau.verifierVictoire(3, 1, ROND);
  }

  @Test
  public void PlateauVictoireHorizontale() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , ROND},
            {null, CROIX , ROND},
            {null , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    plateau.verifHorizontale(1, 3, ROND);
  }

  @Test
  public void PlateauVictoireDiagonaleDroite() {
    Pion[][] pions = new Pion[][] {
            {CROIX, ROND , CROIX},
            {null, CROIX , ROND},
            {null , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    plateau.verifDiagonaleDroite(2, 2, CROIX);
  }

  @Test
  public void PlateauVictoireDiagonaleGauche() {
    Pion[][] pions = new Pion[][] {
            {CROIX, ROND , ROND},
            {null, ROND , CROIX},
            {ROND , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    plateau.verifDiagonaleGauche(2, 2, ROND);
  }
}

package com.simplerp.morpion.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.simplerp.morpion.engine.Pion.CROIX;
import static com.simplerp.morpion.engine.Pion.ROND;

public class PlateauTest {


  @Test
  public void PlateauVictoire() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , CROIX},
            {ROND, CROIX , CROIX},
            {ROND , CROIX , null}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertTrue(plateau.verifierVictoire(1, 0, ROND));
  }


  @Test
  public void PlateauPerdu() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , CROIX},
            {CROIX, ROND , CROIX},
            {ROND , CROIX , null}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertFalse(plateau.verifierVictoire(1, 0, ROND));
  }


  @Test
  public void PlateauVictoireVerticale() {
    Pion[][] pions = new Pion[][] {
            {CROIX, ROND , CROIX},
            {CROIX, null , ROND},
            {CROIX , null , null}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertTrue(plateau.verifVerticale(0, 0, CROIX));
  }

  @Test
  public void PlateauNonVictoireHorizontale() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , ROND},
            {null, CROIX , ROND},
            {null , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertFalse(plateau.verifHorizontale(2, 0, ROND));
  }

  @Test
  public void PlateauVictoireHorizontale() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , ROND},
            {null, CROIX , ROND},
            {null , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertTrue(plateau.verifHorizontale(0, 2, ROND));
  }

  @Test
  public void PlateauVictoireDiagonaleDroite() {
    Pion[][] pions = new Pion[][] {
            {CROIX, ROND , CROIX},
            {null, CROIX , ROND},
            {null , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertTrue(plateau.verifDiagonaleDroite(1, 1, CROIX));
  }

  @Test
  public void PlateauPerduDiagonaleGauche() {
    Pion[][] pions = new Pion[][] {
            {CROIX, ROND , ROND},
            {null, ROND , CROIX},
            {ROND , null , CROIX}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertFalse(plateau.verifDiagonaleGauche(1, 1, ROND));
  }

  @Test
  public void PlateauVictoireDiagonaleGauche() {
    Pion[][] pions = new Pion[][] {
            {ROND, ROND , CROIX},
            {null, ROND , CROIX},
            {ROND , null , ROND}
    };
    MorpionPlateau plateau = new MorpionPlateau(3, 3, 3, pions);
    Assertions.assertTrue(plateau.verifDiagonaleGauche(1, 1, ROND));
  }
}

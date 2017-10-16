/**
 * Type énuméré pour les erreurs contextuelles.
 * Ce type énuméré définit toutes les erreurs contextuelles possibles et 
 * permet l'affichage des messages d'erreurs pour la passe 2.
 */

// -------------------------------------------------------------------------
// A COMPLETER, avec les différents types d'erreur et les messages d'erreurs 
// correspondants
// -------------------------------------------------------------------------

package fr.esisar.compilation.verif;

public enum ErreurContext {
   
    ErreurNonRepertoriee,
	ErreurVariableRedeclaree,
	ErreurVariableNonDeclaree,
	ErreurTypageNonCompatible,
	ErreurIdentNomReserve;

   void leverErreurContext(String s, int numLigne) throws ErreurVerif {
      System.err.print("Erreur contextuelle : ");
      switch (this) {
      case ErreurVariableRedeclaree:
    	  System.err.println("Variable redéclarée");
    	  System.err.print(s);
    	  break;
      case ErreurVariableNonDeclaree:
    	  System.err.println("Variable non déclarée");
    	  System.err.print(s);
    	  break;
      case ErreurTypageNonCompatible:
    	  System.err.println("Opération non compatible :");
    	  System.err.print(s);
    	  break;
      case ErreurIdentNomReserve:
    	  System.err.println("Identificateur déclaré avec un nom réservé");
    	  System.err.print(s+" est un nom réservé");
    	  break;
      case ErreurNonRepertoriee:
    	  System.err.println("Erreur non répertoriée");
    	  System.err.print(s);
    	  break;
      default:
    	  System.err.print(s);
      }
      System.err.println(" ... ligne " + numLigne);
      throw new ErreurVerif();
   }
}



package fr.esisar.compilation.gencode;

import fr.esisar.compilation.global.src.*;
import fr.esisar.compilation.global.src3.*;

/**
 * Génération de code pour un programme JCas à partir d'un arbre décoré.
 */

class Generation {
	
	/**
    * Fonction qui regarde si tous les registres sont utilisés
    * Si false on parcourt de gauche à droite
    * Si true on parcourt de droite à gauche
    */
	private static boolean sensParcours(Arbre a){
		boolean regFull = true;
		for(Registre r: Registre.values()){
			if(Operande.opDirect(r).getNature() == null){
				regFull = false;
				break;
			}
		}
		return regFull;
	}
   
   /**
    * Méthode principale de génération de code.
    * Génère du code pour l'arbre décoré a.
    */
   static Prog coder(Arbre a) {
	  Inst inst;
      Prog.ajouterGrosComment("Programme généré par JCasc");

      

      // Fin du programme
      // L'instruction "HALT"
      inst = Inst.creation0(Operation.HALT);
      // On ajoute l'instruction à la fin du programme
      Prog.ajouter(inst);

      // On retourne le programme assembleur généré
      return Prog.instance(); 
   }
}




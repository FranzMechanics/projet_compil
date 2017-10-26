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
   
   
   
   static void generer_LIGNE(){
	   Prog.ajouter(Inst.creation0(Operation.WNL));
   }  
   
   
   static void generer_ECRITURE(Arbre a) {
	   
	   Inst inst ; 
	   
	   switch (a.getDecor().getType().getNature()) {
	   
	   case String : 
		   inst = Inst.creation1(Operation.WSTR, Operande.creationOpChaine(a.getChaine()));
		   Prog.ajouter(inst);
		   break ; 
		   
	   case Interval : 
		   inst = Inst.creation0(Operation.WINT);
		   Prog.ajouter(inst);
		   break; 
		   
	   case Real : 
		   inst = Inst.creation0(Operation.WFLOAT);
		   Prog.ajouter(inst);
		   break ; 
		   
	   default:
		   break;
		   
	   }
   }
   
   static void generer_AFFECT(){
	   
   }
   
   static void generer_CHAINE(){
	   
   }
   
   static void generer_CONVERSION(){
	   
   }
   
   static void generer_DECL(){
	   
   }
   
   static void generer_DECREMENT(){
	   
   }
   
   static void generer_DIV_REEL(){
	   
   }
   
   static void generer_EGAL(){
	   
   }
   
   static void generer_ENTIER(){
	   
   }
   
   static void generer_ET(){
	   
   }
   
   static void generer_IDENT(){
	   
   }
   
   static void generer_INCREMENT(){
	   
   }
   
   static void generer_INDEX(){
	   
   }
   
   static void generer_INF(){
	   
   }
   
   static void generer_INF_EGAL(){
	   
   }
   
   static void generer_INTERVALLE(){
	   
   }
   
   static void generer_LECTURE(){
	   
   }
   
   static void generer_LISTE_DECL(){
	   
   }
   
   static void generer_LISTE_IDENT(){
	   
   }
   
   static void generer_LISTE_INST(){
	   
   }
   
   static void generer_LISTE_EXP(){
	   
   }
   
   static void generer_MOINS(){
	   
   }
   
   static void generer_MOINS_UNAIRE(){
	   
   }
   
   static void generer_MULT(){
	   
   }
   
   static void generer_NON(){
	   
   }
   
   static void generer_NON_EGAL(){
	   
   }
   
   static void generer_NOP(){
	   
   }
   
   static void generer_OU(){
	   
   }
   
   static void generer_PLUS(){
	   
   }
   
   static void generer_PLUS_UNAIRE(){
	   
   }
   
   static void generer_POUR(){
	   
   }
   
   static void generer_PROGRAMME(){
	   
   }
   
   static void generer_QUOTIENT(){
	   
   }
   
   static void generer_REEL(){
	   
   }
   
   static void generer_RESTE(){
	   
   }
   
   static void generer_SI(){
	   
   }

   static void generer_SUP(){
	   
   }
   
   static void generer_SUP_EGAL(){
	   
   }
   
   static void generer_TABLEAU(){
	   
   }
   
   static void generer_TANT_QUE(){
	   
   }
   
   static void generer_VIDE(){
	   
   }
   
}




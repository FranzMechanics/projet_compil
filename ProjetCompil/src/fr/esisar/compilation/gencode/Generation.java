package fr.esisar.compilation.gencode;

import fr.esisar.compilation.global.src.*;
import fr.esisar.compilation.global.src3.*;
import fr.esisar.compilation.verif.ErreurInterneVerif;

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

      int nbVariables = generer_LISTE_DECL(a.getFils1());
      Prog.ajouter(Inst.creation1(Operation.ADDSP, Operande.creationOpEntier(nbVariables)));
      
      generer_LISTE_INST(a.getFils2());


      // Fin du programme
      // L'instruction "HALT"
      inst = Inst.creation0(Operation.HALT);
      // On ajoute l'instruction à la fin du programme
      Prog.ajouter(inst);

      // On retourne le programme assembleur généré
      return Prog.instance(); 
   }
   
   
   
   private static void generer_LIGNE(){
	   Prog.ajouter(Inst.creation0(Operation.WNL));
   }  
   
   
   private static void generer_ECRITURE(Arbre a) {
	   
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
   
   private static void generer_AFFECT(Arbre a){
	   
   }
   
   private static void generer_CHAINE(Arbre a){
	   
   }
   
   private static void generer_CONVERSION(Arbre a){
	   
   }
   
   private static int generer_LISTE_DECL(Arbre a){
	   int count = 0;
	   switch (a.getNoeud()){
		case Vide : break ;
		case ListeDecl:
			count += generer_LISTE_DECL(a.getFils1());
			count += generer_DECL(a.getFils2());
			return count;
	   }
	   return 0;
   }
   
   private static int generer_DECL(Arbre a){
	   int count = 0;
	   switch(a.getNoeud()){
	   	case Decl :
	   		count += generer_LISTE_IDENT(a.getFils1());
	   		return count;
	   }
	   return 0;
   }
   
   private static void generer_DECREMENT(Arbre a){
	   
   }
   
   private static void generer_DIV_REEL(Arbre a){
	   
   }
   
   private static void generer_EGAL(Arbre a){
	   
   }
   
   private static void generer_ENTIER(Arbre a){
	   
   }
   
   private static void generer_ET(Arbre a){
	   
   }
   
   private static void generer_IDENT(Arbre a){
	   
   }
   
   private static void generer_INCREMENT(Arbre a){
	   
   }
   
   private static void generer_INDEX(Arbre a){
	   
   }
   
   private static void generer_INF(Arbre a){
	   
   }
   
   private static void generer_INF_EGAL(Arbre a){
	   
   }
   
   private static void generer_INTERVALLE(Arbre a){
	   
   }
   
   private static void generer_LECTURE(Arbre a){
	   
   }
   
   
   
   private static int generer_LISTE_IDENT(Arbre a){
	   int count = 0;
	   switch(a.getNoeud()){
	   	case Vide : break ;
	   	case ListeIdent :
	   		count += generer_LISTE_IDENT(a.getFils1());
	   		count += generer_IDENT_DECL(a.getFils2());
	   		return count;
	   }
	return 0;
   }
   
   private static int generer_IDENT_DECL(Arbre a){
	   return 1;
   }
   
   private static void generer_LISTE_INST(Arbre a){
	   
   }
   
   private static void generer_LISTE_EXP(Arbre a){
	   
   }
   
   private static void generer_MOINS(Arbre a){
	   
   }
   
   private static void generer_MOINS_UNAIRE(Arbre a){
	   
   }
   
   private static void generer_MULT(Arbre a){
	   
   }
   
   private static void generer_NON(Arbre a){
	   
   }
   
   private static void generer_NON_EGAL(Arbre a){
	   
   }
   
   private static void generer_NOP(Arbre a){
	   
   }
   
   private static void generer_OU(Arbre a){
	   
   }
   
   private static void generer_PLUS(Arbre a){
	   
   }
   
   private static void generer_PLUS_UNAIRE(Arbre a){
	   
   }
   
   private static void generer_POUR(Arbre a){
	   
   }
   
   private static void generer_PROGRAMME(Arbre a){
	   
   }
   
   private static void generer_QUOTIENT(Arbre a){
	   
   }
   
   private static void generer_REEL(Arbre a){
	   
   }
   
   private static void generer_RESTE(Arbre a){
	   
   }
   
   private static void generer_SI(Arbre a){
	   
   }

   private static void generer_SUP(Arbre a){
	   
   }
   
   private static void generer_SUP_EGAL(Arbre a){
	   
   }
   
   private static void generer_TABLEAU(Arbre a){
	   
   }
   
   private static void generer_TANT_QUE(Arbre a){
	   
   }
   
   private static void generer_VIDE(Arbre a){
	   
   }
   
}




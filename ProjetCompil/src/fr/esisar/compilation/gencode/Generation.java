package fr.esisar.compilation.gencode;

import fr.esisar.compilation.global.src.*;
import fr.esisar.compilation.global.src3.*;

/**
 * Génération de code pour un programme JCas à partir d'un arbre décoré.
 */

class Generation {
	private static boolean[] tabRegAlloue = new boolean[16];
	
   /**
	* Fonction qui regarde si il y a assez de registres libres pour exécuter une opération.
	* @param a le noeud de l'arbre correspondant à l'opération
	* @param nbRegNecessaires le nombre de registres nécessaires à l'opération
	* @return true on parcourt de gauche à droite; false on parcourt de droite à gauche.
    */
	private static boolean sensParcours(Arbre a, int nbRegNecessaires){
		int count = 0;
		for(boolean i: tabRegAlloue){
			if(i == true){
				count++;
			}
		}
		return (count >= nbRegNecessaires);
	}
	
	private static void initTabReg(){
		for(boolean i: tabRegAlloue){
			i = false;
		}
	}
	
	private static void allouerReg(int i){
		tabRegAlloue[i] = true;
	}
	private static void libererReg(int i){
		tabRegAlloue[i] = false;
	}
	
	private static Registre premierRegLibre(){
		int count = 0;
		for(boolean i: tabRegAlloue){
			if(i == true){
				Registre[] tabReg = Registre.values();
				return tabReg[count];
			}
			count++;
		}
		return null;
	}
	
   
   /**
    * Méthode principale de génération de code.
    * Génère du code pour l'arbre décoré a.
    */
   public static Prog coder(Arbre a) {
	  
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
   
   
   private static void generer_ECRITURE(Arbre a) {
	   
	   Inst inst;
	   
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
	   		count += generer_LISTE_IDENT(a.getFils1(), 1);
	   		return count;
	   }
	   return 0;
   }   
   
   /**************************************************************************
    * DECL
    **************************************************************************
   private void generer_DECL(Arbre a)  {
	   switch(a.getNoeud()){
	   	case Decl :
	   		generer_TYPE(a.getFils2());
	   		a.getFils1().setDecor(new Decor(a.getFils2().getDecor().getType()));
	   		generer_LISTE_IDENT(a.getFils1());
	   		break ;
	   }
   }*/


   /**************************************************************************
    * LISTE_IDENT
    **************************************************************************/
   private static int generer_LISTE_IDENT(Arbre a, int adresse)  {
	   int count = 0;
	   switch(a.getNoeud()){
	   	case Vide : return count;
	   	case ListeIdent :
	   		count += generer_LISTE_IDENT(a.getFils1(), adresse+1);
 	   		count += generer_IDENT_DECL(a.getFils2(), adresse);
 	   		return count;
	   }
	   return count;
   }

   /**************************************************************************
    * IDENT_DECL
    **************************************************************************/
   private static int generer_IDENT_DECL(Arbre a, int adresse)  {
	   switch(a.getNoeud()){
	   	case Ident :
	   		a.getDecor().setInfoCode(adresse);
	   		a.getDecor().getDefn().setOperande(Operande.creationOpIndirect(adresse, Registre.GB));
	   		break;
	   }
	   return 1;
   }



   /**************************************************************************
    * IDENT_UTIL
    **************************************************************************/
   private static void generer_IDENT_UTIL(Arbre a)  {
	   Inst inst = Inst.creation2(Operation.LOAD, a.getDecor().getDefn().getOperande(), Operande.opDirect(Registre.R0));
	   Prog.ajouter(inst);
   }

   /**************************************************************************
    * TYPE
    **************************************************************************/
   private static void generer_TYPE(Arbre a)  {
	   switch(a.getNoeud()){
	   	case Ident : 
	   		
	   	case Intervalle :
		    generer_EXP_CONST(a.getFils1());
		    generer_EXP_CONST(a.getFils2());
			a.setDecor(new Decor(Type.creationInterval(a.getFils1().getDecor().getDefn().getValeurInteger(), a.getFils2().getDecor().getDefn().getValeurInteger())));
		    break ;
	   	case Tableau :
	   		generer_TYPE_INTERVALLE(a.getFils1());
	   		generer_TYPE(a.getFils2());
	   		a.setDecor(new Decor(Type.creationArray(a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType())));
	   		break ;
	   }
   }


   /**************************************************************************
    * TYPE_INTERVALLE
    **************************************************************************/
   private static void generer_TYPE_INTERVALLE(Arbre a)  {
	   switch(a.getNoeud()){
	   	case Intervalle :
		   generer_EXP_CONST(a.getFils1());
		   generer_EXP_CONST(a.getFils2());
		   a.setDecor(new Decor(Type.creationInterval(a.getFils1().getDecor().getDefn().getValeurInteger(), a.getFils2().getDecor().getDefn().getValeurInteger())));
		   break ;
	   }
   }


   /**************************************************************************
    * EXP_CONST
    **************************************************************************/
   private static void generer_EXP_CONST(Arbre a)  {
	   
	   switch(a.getNoeud()){
	   	case Ident :
	   		generer_IDENT_UTIL(a);
	   		break ;
	   	case Entier :
	   		a.setDecor(new Decor(Defn.creationConstInteger(a.getEntier()), Type.Integer));
		   break ;
	   	case PlusUnaire :
	   		generer_EXP_CONST(a.getFils1());
			
	   		break ;
	   	case MoinsUnaire :
	   		generer_EXP_CONST(a.getFils1());
	   		
	   		break;
	   }
   }

   /**************************************************************************
    * LISTE_INST
    **************************************************************************/
   private static void generer_LISTE_INST(Arbre a)  {
	   switch (a.getNoeud()){
		case Vide : break ;
		case ListeInst:
		  generer_LISTE_INST(a.getFils1());
	      generer_INST(a.getFils2());
	      break ;
	}
   }

   /**************************************************************************
    * INST
    **************************************************************************/
   private static void generer_INST(Arbre a)  {
	   switch (a.getNoeud()){
		case Nop :
			break ;
		case Affect:
			generer_PLACE(a.getFils1());
			Operande valeur = generer_EXP(a.getFils2());
			
			Inst inst = Inst.creation2(Operation.STORE, valeur, a.getFils1().getDecor().getDefn().getOperande());
			Prog.ajouter(inst, "Affectation de "+valeur+" à "+a.getFils1().getChaine());
			
			break ;
		case Pour :
			generer_PAS(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			break ;
		case TantQue:
			generer_EXP(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			
			break ;
		case Si :
			generer_EXP(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			generer_LISTE_INST(a.getFils3());
			
			break ;
		case Lecture:
			
			break ;
		case Ecriture :
			generer_ECRITURE(a.getFils1());
			break ;
		case Ligne:
			Prog.ajouter(Inst.creation0(Operation.WNL));
			break ;
	}
   }


   /**************************************************************************
    * PAS
    **************************************************************************/
   private static void generer_PAS(Arbre a)  {
	   switch (a.getNoeud()){
		case Increment :
			generer_IDENT_UTIL(a.getFils1());
			generer_EXP(a.getFils2());
			generer_EXP(a.getFils3());
			
			break ;
		case Decrement:
			generer_IDENT_UTIL(a.getFils1());
			generer_EXP(a.getFils2());
			generer_EXP(a.getFils3());
			
			break ;
	   }
   }

   /**************************************************************************
    * PLACE
    **************************************************************************/
   private static void generer_PLACE(Arbre a)  {
	  
	   switch (a.getNoeud()){
		case Ident :
			generer_IDENT_UTIL(a);
			break;
		case Index:
			generer_PLACE(a.getFils1());
			generer_EXP(a.getFils2());
			
			
			break ;
	}
   }


   /**************************************************************************
    * LISTE_EXP
    **************************************************************************/
   private static void generer_LISTE_EXP(Arbre a)  {
	   switch (a.getNoeud()){
		case Vide :
			break ;
		case ListeExp:
			generer_LISTE_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
	      break ;
	   }
   }


   /**************************************************************************
    * EXP
    **************************************************************************/
   private static Operande generer_EXP(Arbre a)  {
	   
	   Type type;
	   switch (a.getNoeud()){
		case Et :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Ou:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Egal :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case InfEgal:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case SupEgal :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case NonEgal:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Inf :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Sup:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Plus :
			boolean sens;
			if((a.getFils1().getNoeud() == Noeud.Ident)||(a.getFils2().getNoeud() == Noeud.Ident)){
				sens = sensParcours(a, 1);
			}
			else{
				sens = sensParcours(a, 2);
			}
			
			System.out.println("***************************0"+sens);
			
			if(sens){
				Operande op1 = generer_EXP(a.getFils1());
				System.out.println("****************************"+op1.getNature());
				if(op1.getNature() != NatureOperande.OpDirect){
					Inst inst = Inst.creation2(Operation.STORE, op1, Operande.opDirect(Registre.R0));
					Prog.ajouter(inst);
					op1 = Operande.opDirect(Registre.R0);
				}
				Operande op2 = generer_EXP(a.getFils2());
				Inst inst = Inst.creation2(Operation.ADD, op2, op1);
				Prog.ajouter(inst, "Ajout de "+op1+" et "+op2);
			} else {
				Operande op1 = generer_EXP(a.getFils2());
				System.out.println("****************************"+op1.getNature());
				if(op1.getNature() != NatureOperande.OpDirect){
					Inst inst = Inst.creation2(Operation.STORE, op1, Operande.opDirect(Registre.R0));
					Prog.ajouter(inst);
					op1 = Operande.opDirect(Registre.R0);
				}
				Operande op2 = generer_EXP(a.getFils1());
				Inst inst = Inst.creation2(Operation.ADD, op2, op1);
				Prog.ajouter(inst, "Ajout de "+op1+" et "+op2);
			}
			
			return Operande.opDirect(Registre.R0);
		case Moins:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Mult :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case DivReel:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Reste :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Quotient:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
		case Index :
			generer_PLACE(a.getFils1());
			generer_EXP(a.getFils2());
	   		
			break ;
		case PlusUnaire:
			generer_EXP(a.getFils1());
			
			break ;
		case MoinsUnaire :
			generer_EXP(a.getFils1());
			
			break ;
		case Non:
			generer_EXP(a.getFils1());
			
			break ;
		case Conversion :
			generer_EXP(a.getFils1());
			break ;
		case Entier:			
			return Operande.creationOpEntier(a.getEntier());
		case Reel :
			return Operande.creationOpReel(a.getReel());
		case Chaine:
			return Operande.creationOpChaine(a.getChaine());
		case Ident :
			generer_IDENT_UTIL(a);
			return a.getDecor().getDefn().getOperande();
	   }
	return null;

   }
   
}




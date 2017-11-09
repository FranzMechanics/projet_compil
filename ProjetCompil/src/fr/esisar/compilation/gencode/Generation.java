package fr.esisar.compilation.gencode;

import fr.esisar.compilation.global.src.*;
import fr.esisar.compilation.global.src3.*;
import fr.esisar.compilation.verif.ErreurContext;
import fr.esisar.compilation.verif.ErreurInterneVerif;
import fr.esisar.compilation.verif.ErreurVerif;
import fr.esisar.compilation.verif.ReglesTypage;
import fr.esisar.compilation.verif.ResultatAffectCompatible;
import fr.esisar.compilation.verif.ResultatBinaireCompatible;
import fr.esisar.compilation.verif.ResultatUnaireCompatible;

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
	   generer_PLACE(a.getFils1());
	   generer_EXP(a.getFils2());
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
   
   /**************************************************************************
    * DECL
    **************************************************************************
   private void generer_DECL(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Decl :
	   		generer_TYPE(a.getFils2());
	   		a.getFils1().setDecor(new Decor(a.getFils2().getDecor().getType()));
	   		generer_LISTE_IDENT(a.getFils1());
	   		break ;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_DECL");
	   }
   }*/


   /**************************************************************************
    * LISTE_IDENT
    **************************************************************************/
   private int generer_LISTE_IDENT(Arbre a) throws ErreurVerif {
	   int count = 0;
	   switch(a.getNoeud()){
	   	case Vide : break ;
	   	case ListeIdent :
	   		a.getFils1().setDecor(new Decor(a.getDecor().getType()));
	   		a.getFils2().setDecor(new Decor(a.getDecor().getType()));
	   		count += generer_LISTE_IDENT(a.getFils1());
 	   		count += generer_IDENT_DECL(a.getFils2());
 	   		return count;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_LISTE_IDENT");
	   }
   }

   /**************************************************************************
    * IDENT_DECL
    **************************************************************************/
   private int generer_IDENT_DECL(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident :
	   		Defn d = env.chercher(a.getChaine());
	   		if(d == null){
	   			Defn defn = Defn.creationVar(a.getDecor().getType());
	   			a.setDecor(new Decor(defn));
	   			env.enrichir(a.getChaine(), defn);
	   		}
	   		else{
	   			if(d.getGenre() != Genre.NonPredefini){
	   				ErreurContext.ErreurIdentNomReserve.leverErreurContext(a.getChaine(), a.getNumLigne());
	   			}
	   			ErreurContext.ErreurVariableRedeclaree.leverErreurContext("Variable "+a.getChaine()+" déjà déclarée", a.getNumLigne());
	   		}
	   		break;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_IDENT_DECL");
	   }
	   return 1;
   }



   /**************************************************************************
    * IDENT_UTIL
    **************************************************************************/
   private void generer_IDENT_UTIL(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident :
	   		Defn d;
	   		if((d = env.chercher(a.getChaine())) != null){
	   			a.setDecor(new Decor(d, d.getType()));
		    }
		    else{
			   ErreurContext.ErreurVariableNonDeclaree.leverErreurContext("Variable "+a.getChaine()+" non déclarée", a.getNumLigne());
		    }
	   		break;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_IDENT_UTIL");
	   }
   }

   /**************************************************************************
    * TYPE
    **************************************************************************/
   private void generer_TYPE(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident : 
	   		Defn d = env.chercher(a.getChaine());
	   		if(d != null){
		   		Decor dec = new Decor(d, d.getType());
		   		a.setDecor(dec);
	   		}
	   		else{
	   			ErreurContext.ErreurTypeIndefini.leverErreurContext(a.getChaine(), a.getNumLigne());
	   		}
	   		break ;
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
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_TYPE");
	   }
   }


   /**************************************************************************
    * TYPE_INTERVALLE
    **************************************************************************/
   private void generer_TYPE_INTERVALLE(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Intervalle :
		   generer_EXP_CONST(a.getFils1());
		   generer_EXP_CONST(a.getFils2());
		   a.setDecor(new Decor(Type.creationInterval(a.getFils1().getDecor().getDefn().getValeurInteger(), a.getFils2().getDecor().getDefn().getValeurInteger())));
		   break ;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_TYPE_INTERVALLE");
	   }
   }


   /**************************************************************************
    * EXP_CONST
    **************************************************************************/
   private void generer_EXP_CONST(Arbre a) throws ErreurVerif {
	   ResultatUnaireCompatible reg_un;
	   switch(a.getNoeud()){
	   	case Ident :
	   		generer_IDENT_UTIL(a);
	   		break ;
	   	case Entier :
	   		a.setDecor(new Decor(Defn.creationConstInteger(a.getEntier()), Type.Integer));
		   break ;
	   	case PlusUnaire :
	   		generer_EXP_CONST(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			a.setDecor(new Decor(Defn.creationConstInteger(+a.getFils1().getEntier()), Type.Integer));
	   		break ;
	   	case MoinsUnaire :
	   		generer_EXP_CONST(a.getFils1());
	   		reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			a.setDecor(new Decor(Defn.creationConstInteger(-a.getFils1().getEntier()), Type.Integer));
	   		break;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans generer_EXP_CONST");
	   }
   }

   /**************************************************************************
    * LISTE_INST
    **************************************************************************/
   private void generer_LISTE_INST(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Vide : break ;
		case ListeInst:
		  generer_LISTE_INST(a.getFils1());
	      generer_INST(a.getFils2());
	      break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans generer_LISTE_INST");
	}
   }

   /**************************************************************************
    * INST
    **************************************************************************/
   private void generer_INST(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Nop :
			break ;
		case Affect:
			generer_PLACE(a.getFils1());
			generer_EXP(a.getFils2());
			ResultatAffectCompatible reg_aff = ReglesTypage.affectCompatible(a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_aff.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_aff.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			break ;
		case Pour :
			generer_PAS(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			break ;
		case TantQue:
			generer_EXP(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			if(a.getFils1().getDecor().getType().getNature() != NatureType.Boolean){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			break ;
		case Si :
			generer_EXP(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			generer_LISTE_INST(a.getFils3());
			if(a.getFils1().getDecor().getType().getNature() != NatureType.Boolean){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			break ;
		case Lecture:
			generer_PLACE(a.getFils1());
			if((a.getFils1().getDecor().getType() != Type.Real)&&(a.getFils1().getDecor().getType().getNature() != NatureType.Interval)){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			break ;
		case Ecriture :
			generer_LISTE_EXP(a.getFils1());
			break ;
		case Ligne:
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans generer_INST");
	}
   }


   /**************************************************************************
    * PAS
    **************************************************************************/
   private void generer_PAS(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Increment :
			generer_IDENT_UTIL(a.getFils1());
			generer_EXP(a.getFils2());
			generer_EXP(a.getFils3());
			if(a.getFils1().getDecor().getType().getNature() != NatureType.Interval){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Pour : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			if(a.getFils2().getDecor().getType().getNature() != NatureType.Interval){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Pour : Type "+ a.getFils2().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			if(a.getFils3().getDecor().getType().getNature() != NatureType.Interval){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Pour : Type "+ a.getFils3().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			break ;
		case Decrement:
			generer_IDENT_UTIL(a.getFils1());
			generer_EXP(a.getFils2());
			generer_EXP(a.getFils3());
			if(a.getFils1().getDecor().getType().getNature() != NatureType.Interval){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Pour : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			if(a.getFils2().getDecor().getType().getNature() != NatureType.Interval){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Pour : Type "+ a.getFils2().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			if(a.getFils3().getDecor().getType().getNature() != NatureType.Interval){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Pour : Type "+ a.getFils3().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans generer_PAS");
	}
   }

   /**************************************************************************
    * PLACE
    **************************************************************************/
   private void generer_PLACE(Arbre a) throws ErreurVerif {
	   ResultatBinaireCompatible reg_bin;
	   switch (a.getNoeud()){
		case Ident :
			generer_IDENT_UTIL(a);
			break;
		case Index:
			generer_PLACE(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			a.setDecor(new Decor(reg_bin.getTypeRes()));
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans generer_PLACE");
	}
   }


   /**************************************************************************
    * LISTE_EXP
    **************************************************************************/
   private void generer_LISTE_EXP(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Vide :
			break ;
		case ListeExp:
			generer_LISTE_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			if((a.getFils2().getDecor().getType() != Type.Real)&&(a.getFils2().getDecor().getType().getNature() != NatureType.Interval)&&(a.getFils2().getDecor().getType().getNature() != NatureType.String)){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération Ecriture : Type "+ a.getFils2().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
	      break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans generer_LISTE_EXP");
	   }
   }


   /**************************************************************************
    * EXP
    **************************************************************************/
   private void generer_EXP(Arbre a) throws ErreurVerif {
	   ResultatBinaireCompatible reg_bin;
	   ResultatUnaireCompatible reg_un;
	   Type type;
	   switch (a.getNoeud()){
		case Et :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Ou:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Egal :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case InfEgal:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case SupEgal :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case NonEgal:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Inf :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Sup:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Plus :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Moins:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Mult :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case DivReel:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			else if(reg_bin.getConv1() == true){
				Arbre tmp;
				tmp = a.getFils1();
				a.setFils1(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils1().setDecor(new Decor(Type.Real));
			}
			else if(reg_bin.getConv2() == true){
				Arbre tmp;
				tmp = a.getFils2();
				a.setFils2(Arbre.creation1(Noeud.Conversion, tmp, a.getNumLigne()));
				a.getFils2().setDecor(new Decor(Type.Real));
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Reste :
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Quotient:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Index :
			generer_PLACE(a.getFils1());
			generer_EXP(a.getFils2());
	   		reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			a.setDecor(new Decor(reg_bin.getTypeRes()));
			break ;
		case PlusUnaire:
			generer_EXP(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case MoinsUnaire :
			generer_EXP(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Non:
			generer_EXP(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Conversion :
			generer_EXP(a.getFils1());
			break ;
		case Entier:			
			a.setDecor(new Decor(Defn.creationConstInteger(a.getEntier()), Type.Integer));
			break ;
		case Reel :
			a.setDecor(new Decor(Type.Real));
			break ;
		case Chaine:
			a.setDecor(new Decor(Type.String));
			break ;
		case Ident :
			generer_IDENT_UTIL(a);
			break;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans generer_EXP");
	   }
   }
   
}




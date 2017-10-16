package fr.esisar.compilation.verif;

import fr.esisar.compilation.global.src.*;

/**
 * Cette classe permet de réaliser la vérification et la décoration
 * de l'arbre abstrait d'un programme.
 */
public class Verif {

   private Environ env; // L'environnement des identificateurs

   /**
    * Constructeur.
    */
   public Verif() {
      env = new Environ();
   }

   /**
    * Vérifie les contraintes contextuelles du programme correspondant à
    * l'arbre abstrait a, qui est décoré et enrichi.
    * Les contraintes contextuelles sont décrites
    * dans Context.txt.
    * En cas d'erreur contextuelle, un message d'erreur est affiché et
    * l'exception ErreurVerif est levée.
    */
   public void verifierDecorer(Arbre a) throws ErreurVerif {
      verifier_PROGRAMME(a);
   }

   /**
    * Initialisation de l'environnement avec les identificateurs prédéfinis.
    */
   private void initialiserEnv() {
      Defn def;
      // integer
      def = Defn.creationType(Type.Integer);
      def.setGenre(Genre.PredefInteger);
      env.enrichir("integer", def);

      // boolean
      def = Defn.creationType(Type.Boolean);
      def.setGenre(Genre.PredefBoolean);
      env.enrichir("boolean", def);

      // false
      def = Defn.creationConstBoolean(false);
      def.setGenre(Genre.PredefFalse);
      env.enrichir("false", def);

      // true
      def = Defn.creationConstBoolean(true);
      def.setGenre(Genre.PredefTrue);
      env.enrichir("true", def);

      // max_int
      def = Defn.creationConstInteger(java.lang.Integer.MAX_VALUE);
      def.setGenre(Genre.PredefMaxInt);
      env.enrichir("max_int", def);

      // real
      def = Defn.creationType(Type.Real);
      def.setGenre(Genre.PredefReal);
      env.enrichir("real", def);

      /*System.out.println(env.chercher("false"));
      System.out.println(env.chercher("max_int"));
      System.out.println(env.chercher("real"));
      System.out.println(env.chercher("integer"));
      System.out.println(env.chercher("boolean"));*/
   }

   /**************************************************************************
    * PROGRAMME
    **************************************************************************/
   private void verifier_PROGRAMME(Arbre a) throws ErreurVerif {
      initialiserEnv();
      verifier_LISTE_DECL(a.getFils1());
      verifier_LISTE_INST(a.getFils2());
   }

   /**************************************************************************
    * LISTE_DECL
    **************************************************************************/
   private void verifier_LISTE_DECL(Arbre a) throws ErreurVerif {
	switch (a.getNoeud()){
		case Vide : break ;
		case ListeDecl:
		  verifier_LISTE_DECL(a.getFils1());
	      verifier_DECL(a.getFils2());
	      break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_LISTE_DECL");
	}
   }



   /**************************************************************************
    * DECL
    **************************************************************************/
   private void verifier_DECL(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Decl :
	   		verifier_TYPE(a.getFils2());
	   		a.getFils1().setDecor(new Decor(a.getFils2().getDecor().getType()));
	   		verifier_LISTE_IDENT(a.getFils1());
	   		break ;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_DECL");
	   }
   }


   /**************************************************************************
    * LISTE_IDENT
    **************************************************************************/
   private void verifier_LISTE_IDENT(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Vide : break ;
	   	case ListeIdent :
	   		a.getFils1().setDecor(new Decor(a.getDecor().getType()));
	   		a.getFils2().setDecor(new Decor(a.getDecor().getType()));
	   		verifier_LISTE_IDENT(a.getFils1());
	   		verifier_IDENT_DECL(a.getFils2());
	   		break ;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_LISTE_IDENT");
	   }
   }

   /**************************************************************************
    * IDENT_DECL
    **************************************************************************/
   private void verifier_IDENT_DECL(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident :
		   if(env.chercher(a.getChaine()) == null){
			   Defn defn = Defn.creationVar(a.getDecor().getType());
			   a.setDecor(new Decor(defn));
			   env.enrichir(a.getChaine(), defn);
		   }
		   else{
			   String[] predef = new String[6];
			   predef[0] = "integer";
			   predef[1] = "boolean";
			   predef[2] = "real";
			   predef[3] = "true";
			   predef[4] = "false";
			   predef[5] = "max_int";
			   for(String i: predef){
				   if (i.equals(a.getChaine())){
					   ErreurContext.ErreurIdentNomReserve.leverErreurContext(a.getChaine(), a.getNumLigne());
				   }
			   }
			   ErreurContext.ErreurVariableRedeclaree.leverErreurContext("Variable "+a.getChaine()+" déjà déclarée", a.getNumLigne());
		   }
		   break;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_IDENT_DECL");
	   }
   }



   /**************************************************************************
    * IDENT_UTIL
    **************************************************************************/
   private void verifier_IDENT_UTIL(Arbre a) throws ErreurVerif {
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
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_IDENT_UTIL");
	   }
   }

   /**************************************************************************
    * TYPE
    **************************************************************************/
   private void verifier_TYPE(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident : 
	   		Defn d = env.chercher(a.getChaine());
	   		if(d != null){
		   		Decor dec = new Decor(d, d.getType());
		   		a.setDecor(dec);
	   		}
	   		else{
	   			throw new ErreurType(a.getChaine()+" : Type non défini");
	   		}
	   		break ;
	   	case Intervalle :
		    verifier_EXP_CONST(a.getFils1());
		    verifier_EXP_CONST(a.getFils2());
		    a.setDecor(new Decor(Type.creationInterval(a.getFils1().getEntier(), a.getFils2().getEntier())));
		    break ;
	   	case Tableau :
	   		verifier_TYPE_INTERVALLE(a.getFils1());
	   		verifier_TYPE(a.getFils2());
	   		a.setDecor(new Decor(Type.creationArray(a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType())));
	   		break ;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_TYPE");
	   }
   }


   /**************************************************************************
    * TYPE_INTERVALLE
    **************************************************************************/
   private void verifier_TYPE_INTERVALLE(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Intervalle :
		   verifier_EXP_CONST(a.getFils1());
		   verifier_EXP_CONST(a.getFils2());
		   a.setDecor(new Decor(Type.creationInterval(a.getFils1().getEntier(), a.getFils2().getEntier())));
		   break ;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_TYPE_INTERVALLE");
	   }
   }


   /**************************************************************************
    * EXP_CONST
    **************************************************************************/
   private void verifier_EXP_CONST(Arbre a) throws ErreurVerif {
	   ResultatUnaireCompatible reg_un;
	   Type type;
	   switch(a.getNoeud()){
	   	case Ident :
	   		verifier_IDENT_UTIL(a);
	   		break ;
	   	case Entier :
	   		a.setDecor(new Decor(Defn.creationConstInteger(a.getEntier()), Type.Integer));
		   break ;
	   	case PlusUnaire :
	   		verifier_EXP_CONST(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(Defn.creationConstInteger(+a.getEntier()), Type.Integer));
	   		break ;
	   	case MoinsUnaire :
	   		verifier_EXP_CONST(a.getFils1());
	   		reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(Defn.creationConstInteger(-a.getEntier()), Type.Integer));
	   		break;
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_EXP_CONST");
	   }
   }

   /**************************************************************************
    * LISTE_INST
    **************************************************************************/
   private void verifier_LISTE_INST(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Vide : break ;
		case ListeInst:
		  verifier_LISTE_INST(a.getFils1());
	      verifier_INST(a.getFils2());
	      break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_LISTE_INST");
	}
   }

   /**************************************************************************
    * INST
    **************************************************************************/
   private void verifier_INST(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Nop :
			break ;
		case Affect:
			verifier_PLACE(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_PAS(a.getFils1());
			verifier_LISTE_INST(a.getFils2());
			break ;
		case TantQue:
			verifier_EXP(a.getFils1());
			verifier_LISTE_INST(a.getFils2());
			break ;
		case Si :
			verifier_EXP(a.getFils1());
			verifier_LISTE_INST(a.getFils2());
			verifier_LISTE_INST(a.getFils3());
			break ;
		case Lecture:
			verifier_PLACE(a.getFils1());
			break ;
		case Ecriture :
			verifier_LISTE_EXP(a.getFils1());
			break ;
		case Ligne:
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_INST");
	}
   }


   /**************************************************************************
    * PAS
    **************************************************************************/
   private void verifier_PAS(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Increment :
			verifier_IDENT_UTIL(a.getFils1());
			verifier_EXP(a.getFils2());
			verifier_EXP(a.getFils3());
			break ;
		case Decrement:
			verifier_IDENT_UTIL(a.getFils1());
			verifier_EXP(a.getFils2());
			verifier_EXP(a.getFils3());
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_PAS");
	}
   }

   /**************************************************************************
    * PLACE
    **************************************************************************/
   private void verifier_PLACE(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Ident :
			verifier_IDENT_UTIL(a);
			break;
		case Index:
			verifier_PLACE(a.getFils1());
			verifier_EXP(a.getFils2());
			a.setDecor(new Decor(env.chercher(a.getFils1().getChaine()).getType().getElement()));
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_PLACE");
	}
   }


   /**************************************************************************
    * LISTE_EXP
    **************************************************************************/
   private void verifier_LISTE_EXP(Arbre a) throws ErreurVerif {
	   switch (a.getNoeud()){
		case Vide :
			break ;
		case ListeExp:
			verifier_LISTE_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
	      break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_LISTE_EXP");
	   }
   }


   /**************************************************************************
    * EXP
    **************************************************************************/
   private void verifier_EXP(Arbre a) throws ErreurVerif {
	   ResultatBinaireCompatible reg_bin;
	   ResultatUnaireCompatible reg_un;
	   Type type;
	   switch (a.getNoeud()){
		case Et :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Ou:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Egal :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
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
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Quotient:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			reg_bin = ReglesTypage.binaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType(), a.getFils2().getDecor().getType());
			if(reg_bin.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Types "+ a.getFils1().getDecor().getType().getNature().name()+" et "+ a.getFils2().getDecor().getType().getNature().name()+" incompatibles", a.getNumLigne());
			}
			type = reg_bin.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Index :
			verifier_PLACE(a.getFils1());
			verifier_EXP(a.getFils2());
			a.setDecor(new Decor(env.chercher(a.getFils1().getChaine()).getType().getElement()));
			break ;
		case PlusUnaire:
			verifier_EXP(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case MoinsUnaire :
			verifier_EXP(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Non:
			verifier_EXP(a.getFils1());
			reg_un = ReglesTypage.unaireCompatible(a.getNoeud(), a.getFils1().getDecor().getType());
			if(reg_un.getOk() == false){
				ErreurContext.ErreurTypageNonCompatible.leverErreurContext("Opération "+a.getNoeud().name()+" : Type "+ a.getFils1().getDecor().getType().getNature().name()+" incompatible", a.getNumLigne());
			}
			type = reg_un.getTypeRes();
			a.setDecor(new Decor(type));
			break ;
		case Conversion :
			verifier_EXP(a.getFils1());
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
			verifier_IDENT_UTIL(a);
			break;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_EXP");
	   }
   }
}

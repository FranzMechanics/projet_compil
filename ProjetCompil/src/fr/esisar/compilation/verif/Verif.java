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
      
      // ------------
      // A COMPLETER
      // ------------
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
		   verifier_LISTE_IDENT(a.getFils1());
		   verifier_TYPE(a.getFils2());
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
		   break ; 
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
		   break ; 
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_IDENT_UTIL");
	   }
   }
   
   /**************************************************************************
    * TYPE
    **************************************************************************/
   private void verifier_TYPE(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident : break ; 
	   	case Intervalle :
		   verifier_EXP_CONST(a.getFils1());
		   verifier_EXP_CONST(a.getFils2());
		   break ; 
	   	case Tableau :
	   		verifier_TYPE_INTERVALLE(a.getFils1());
	   		verifier_TYPE(a.getFils2());
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
		   break ; 
	   default :
		   throw new ErreurInterneVerif("Arbre incorrect dans verifier_TYPE_INTERVALLE");
	   }
   }
   
   
   /**************************************************************************
    * EXP_CONST
    **************************************************************************/
   private void verifier_EXP_CONST(Arbre a) throws ErreurVerif {
	   switch(a.getNoeud()){
	   	case Ident :
	   		break ;
	   	case Entier :
		   break ; 
	   	case PlusUnaire :
	   		verifier_EXP_CONST(a.getFils1());
	   		break ;
	   	case MoinsUnaire :
	   		verifier_EXP_CONST(a.getFils1());
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
			break ;
		case Index:
			verifier_PLACE(a.getFils1());
			verifier_EXP(a.getFils2());
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
	   switch (a.getNoeud()){
		case Et :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case Ou:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case Egal :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case InfEgal:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case SupEgal :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case NonEgal:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case Inf :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case Sup:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case Plus :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case Moins:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case Mult :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case DivReel:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case Reste :
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case Quotient:
			verifier_EXP(a.getFils1());
			verifier_EXP(a.getFils2());
			break ; 
		case Index :
			verifier_PLACE(a.getFils1());
			verifier_EXP(a.getFils2());
			break ;
		case PlusUnaire:
			verifier_EXP(a.getFils1());
			break ; 
		case MoinsUnaire :
			verifier_EXP(a.getFils1());
			break ;
		case Non:
			verifier_EXP(a.getFils1());
			break ; 
		case Conversion :
			verifier_EXP(a.getFils1());
			break ;
		case Entier:
			break ; 
		case Reel :
			break ;
		case Chaine:
			break ; 
		case Ident :
			break ;
		default :
			throw new ErreurInterneVerif("Arbre incorrect dans verifier_EXP");
	}
   }
   
   
   
   // ------------------------------------------------------------------------
   // COMPLETER les operations de vérifications et de décoration pour toutes 
   // les constructions d'arbres
   // ------------------------------------------------------------------------

}

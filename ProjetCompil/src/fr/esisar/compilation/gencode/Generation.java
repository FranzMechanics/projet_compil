package fr.esisar.compilation.gencode;

import fr.esisar.compilation.global.src.*;
import fr.esisar.compilation.global.src3.*;
import fr.esisar.compilation.global.src3.ErreurOperande;

/**
 * Génération de code pour un programme JCas à partir d'un arbre décoré.
 */

class Generation {
	private static boolean[] tabRegAlloue = new boolean[16];
	private static int variableTemp;
	
   /**
	* Fonction qui regarde si il y a assez de registres libres pour exécuter une opération.
	* @param a le noeud de l'arbre correspondant à l'opération
	* @param nbRegNecessaires le nombre de registres nécessaires à l'opération
	* @return true on parcourt de gauche à droite; false on parcourt de droite à gauche.
    */
	private static boolean sensParcours(Arbre a, int nbRegNecessaires){
		int count = 0;
		for(boolean i: tabRegAlloue){
			if(i == false){
				count++;
			}
		}
		return (count >= nbRegNecessaires);
	}
	
	private static void printReg(){
		int count = 0;
//		System.out.println("*********** Registres *************");
		for(boolean i: tabRegAlloue){
//			System.out.println("R"+count+" : "+i);
			count++;
		}
	}
	
	private static void initTabReg(){
		for(boolean i: tabRegAlloue){
			i = false;
		}
	}
	
	private static void allouerReg(Operande op){
		if(op.getNature() != NatureOperande.OpDirect){
			throw new ErreurOperande(op.getNature()+"; Opérande à adressage direct attendue");
		}
		tabRegAlloue[op.getRegistre().ordinal()] = true;
	}
	private static void libererReg(Operande reg){
		if(reg.getNature() != NatureOperande.OpDirect){
			throw new ErreurOperande(reg.getNature()+"; Opérande à adressage direct attendue");
		}
		tabRegAlloue[reg.getRegistre().ordinal()] = false;
	}
	
	private static Registre premierRegLibre(){
		int count = 0;
		for(boolean i: tabRegAlloue){
			if(i == false){
				Registre[] tabReg = Registre.values();
				allouerReg(Operande.opDirect(tabReg[count]));
				return tabReg[count];
			}
			count++;
		}
		return null;
	}
	
   
   /**
    * Méthode principale de génération de code.
    * <p>
    * Génère du code pour l'arbre décoré a.
    */
   public static Prog coder(Arbre a) {
	  initTabReg();
	  Inst inst;
      Prog.ajouterGrosComment("Programme généré par JCasc");

      /*
       * On va compter le nombre de variables.
       * nbVariables contient le nombre de variables du programme + une qu'on va
       * réserver comme variable temporaire.
       */
      int nbVariables = generer_LISTE_DECL(a.getFils1());
      variableTemp = nbVariables;
      Prog.ajouterComment("Allocation de l'espace mémoire");
      Prog.ajouter(Inst.creation1(Operation.ADDSP, Operande.creationOpEntier(nbVariables+1)));
      
      generer_LISTE_INST(a.getFils2());


      // Fin du programme
      // L'instruction "HALT"
      Prog.ajouterComment("Fin du programme");
      inst = Inst.creation0(Operation.HALT);
      // On ajoute l'instruction à la fin du programme
      Prog.ajouter(inst);

      // On retourne le programme assembleur généré
      return Prog.instance(); 
   }
   
   
   private static void generer_ECRITURE(Arbre a) {
	   
	   Inst inst ;
	   //System.out.println(a.getNoeud());
	   switch(a.getNoeud()){
	   
	   case Vide :
		   break;
		   
	   case ListeExp :
		   generer_ECRITURE(a.getFils1());
		   generer_ECRITURE(a.getFils2());
		   break ;
		   
	   case Entier :
		   Prog.ajouterComment("Ecriture d'un entier, ligne "+ a.getNumLigne());
		   inst = Inst.creation2(Operation.LOAD,Operande.creationOpEntier(a.getEntier()),Operande.R1);
		   Prog.ajouter(inst);
		   inst = Inst.creation0(Operation.WINT);
		   Prog.ajouter(inst); 
		   break ;
		   
	   case Reel :
		   Prog.ajouterComment("Ecriture d'un réel, ligne "+ a.getNumLigne());
		   inst = Inst.creation2(Operation.LOAD,Operande.creationOpReel(a.getReel()),Operande.R1);
		   Prog.ajouter(inst);
		   inst = Inst.creation0(Operation.WFLOAT);
		   Prog.ajouter(inst); 
		   break ;
		   
	   case Chaine :
		   Prog.ajouterComment("Ecriture d'une chaîne, ligne "+ a.getNumLigne());
		   inst = Inst.creation1(Operation.WSTR,Operande.creationOpChaine(a.getChaine()));
		   Prog.ajouter(inst);
		   break ;
		   
	   case Ident : 
		   Prog.ajouterComment("Ecriture de variable, ligne "+ a.getNumLigne());
		   generer_ECRITURE_IDENT(a);
		   break ;
		   
	   case Index:
		   Prog.ajouterComment("Ecriture de variable issue d'un tableau, ligne "+ a.getNumLigne());
		   Operande reg = Operande.opDirect(premierRegLibre());
		   Operande op = generer_PLACE(a, reg);
		   libererReg(reg);
			
		   inst = Inst.creation2(Operation.LOAD, op, Operande.R1);
		   Prog.ajouter(inst);
		   System.out.println(a.getDecor().getType().getNature());
		   switch(a.getDecor().getType().getNature()){
		   	case Interval:
		   		inst = Inst.creation0(Operation.WINT); 
				Prog.ajouter(inst);
				break;
		   	case Real:
		   		inst = Inst.creation0(Operation.WFLOAT);
				Prog.ajouter(inst);
				break;
		   }
		   
		   break ;
		   
	   default :
		   break ; 
	   
	   }
	   
   }

   private static void generer_ECRITURE_PLACE(Arbre a){
	   Inst inst ;
	   Operande reg = Operande.opDirect(premierRegLibre());
	   generer_EXP(a, reg);
	   switch (a.getDecor().getType().getNature()) {
	   
	   case Interval:
		   inst = Inst.creation2(Operation.LOAD, reg, Operande.R1);
		   Prog.ajouter(inst);
		   inst = Inst.creation0(Operation.WINT); 
		   Prog.ajouter(inst);
		   break ;
		   
	   case Real :
		   inst = Inst.creation2(Operation.LOAD, reg, Operande.R1);
		   Prog.ajouter(inst);
		   inst = Inst.creation0(Operation.WFLOAT); 
		   Prog.ajouter(inst);
		   break ;
		  
	   default:
			 break;
		   
	   }
	   libererReg(reg);
   }
   
   private static void generer_ECRITURE_IDENT(Arbre a) {
	   Inst inst ;
	   switch (a.getDecor().getType().getNature()) {
	   
	   case Interval:
		   inst = Inst.creation2(Operation.LOAD, a.getDecor().getDefn().getOperande(),Operande.R1);
		   Prog.ajouter(inst);
		   inst = Inst.creation0(Operation.WINT); 
		   Prog.ajouter(inst);
		   break ;
		   
	   case Real :
		   inst = Inst.creation2(Operation.LOAD, a.getDecor().getDefn().getOperande(),Operande.R1);
		   Prog.ajouter(inst);
		   inst = Inst.creation0(Operation.WFLOAT); 
		   Prog.ajouter(inst);
		   break ;
		   
	
	   default:
			 break;
		   
	   }
	   
   }
   

   /**
    * Fonction qui génère le noeud ListeDecl.
    * <p>
    * La fonction appelle generer_DECL et instancie le comptage des variables du programme.
    * @param a le noeud ListeDecl
    * @return le nombre de variables trouvées dans le programme
    */
   private static int generer_LISTE_DECL(Arbre a){
	   int count = 0;
	   switch (a.getNoeud()){
		case Vide : break ;
		case ListeDecl:
			count += generer_LISTE_DECL(a.getFils1());
			count += generer_DECL(a.getFils2(), count);
			return count;
	   }
	   return 0;
   }
   
   /**
    * Fonction qui génère le noeud Decl.
    * <p>
    * La fonction appelle generer_LISTE_IDENT et lui passe en argument le nombre de variables
    * qui ont déjà été trouvées dans le programme par la fonction generer_LISTE_DECL.
    * @param a le noeud Decl
    * @param debut le nombre de variables déjà trouvées
    * @return le nombre de variables trouvées dans les fils de <b>a
    */
   private static int generer_DECL(Arbre a, int debut){
	   int count = 0;
	   switch(a.getNoeud()){
	   	case Decl :
	   		count += generer_LISTE_IDENT(a.getFils1(), debut);
	   		System.out.println("*************"+count+a.getFils1().getFils2().getChaine());
	   		
	   		count += generer_TYPE(a.getFils2());
	   		System.out.println("*************"+count+a.getFils2());
	  
	   		return count;
	   }
	   return 0;
   }   

   /**
    * Fonction qui génère le noeud ListeIdent.
    * <p>
    * Cette fonction incrémente le compteur de variables lorsqu'elle appelle generer_IDENT_DECL.
    * Elle passe donc en argument <b>adresse</b>+1 lorsqu'elle s'appelle elle-même.
    * @param a le noeud ListeIdent
    * @param adresse l'adresse que l'on va attribuer à l'identifiant trouvé
    * @return le nombre de variables trouvées dans les fils de <b>a
    */
   private static int generer_LISTE_IDENT(Arbre a, int adresse)  {
	   int count = 0;
	   switch(a.getNoeud()){
	   	case Vide : return count;
	   	case ListeIdent :
//	   		System.out.println("Decl "+a.getFils2().getChaine()+" "+adresse);
	   		count += generer_LISTE_IDENT(a.getFils1(), adresse+1);
	   		
	   		/*
	   		 *  On passe adresse+1 comme adresse à attribuer à la variable
	   		 *  pour compenser le fait qu'on commence à compter à 0 et que les adresses
	   		 *  en mémoire commencent à GB+1
	   		 */
	   		count += generer_IDENT_DECL(a.getFils2(), adresse+1);
 	   		return count;
	   }
	   return count;
   }

   /**
    * Fonction qui alloue une adresse mémoire à un identifiant lors de sa déclaration.
    * <p>
    * Cette fonction a pour but d'associer un identifiant à son adresse mémoire. On va donc 
    * créer une opérande à adressage indirect avec déplacement décalée d'autant de mots que de 
    * variables trouvées dans le programme par rapport au registre GB.
    * @param a le noeud Ident
    * @param adresse l'adresse que l'on va attribuer à l'identifiant trouvé
    * @return l'entier 1 qui va incrémenter le compteur de variables du programme
    * @see	Operande
    */
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
   private static Operande generer_IDENT_UTIL(Arbre a)  {
	   return null;
   }

  
   /**************************************************************************
    * TYPE
    **************************************************************************/
   private static int generer_TYPE(Arbre a)  {
	   switch(a.getNoeud()){
	   	case Ident : 
	   		a.getDecor().getType().setTaille(1);
	   		break;
	   	case Intervalle :
		    break ;
	   	case Tableau :
	   		//System.out.println("*****************"+a.getDecor().getType());
	   		
	   		int taille = calculerTailleTab(a.getFils1());
	   		// Si la taille du tableau vaut, le tableau est vide.
	   		if(taille == 0){
	   			throw new ErreurOperande("Tableau de taille nulle");
	   		}
	   		generer_TYPE(a.getFils2());
	   		taille *= a.getFils2().getDecor().getType().getTaille();
	   		a.getDecor().getType().setTaille(taille);
	   		return taille;
	   }
	   return 0;
   }


   /**************************************************************************
    * TYPE_INTERVALLE
    **************************************************************************/
   private static int calculerTailleTab(Arbre a)  {
	   switch(a.getNoeud()){
	   	case Intervalle :
	   		return a.getDecor().getType().getBorneSup() - a.getDecor().getType().getBorneInf() +1;
	   		
	   	case Index:
	   		System.out.println(a.getDecor().getType().getIndice());
	   		return a.getDecor().getType().getIndice().getBorneSup() - a.getDecor().getType().getIndice().getBorneInf() +1;
	   		
	   	default:
	   		//throw new ErreurType(a.getNoeud()+" : Noeud Intervalle attendu");
	   		return 0;
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
			Prog.ajouterComment("Affectation, ligne "+a.getNumLigne());
			
			Operande valeur = Operande.opDirect(premierRegLibre());
			Operande reg = Operande.opDirect(premierRegLibre());
			Operande variable = generer_PLACE(a.getFils1(), reg);
			libererReg(reg);
			generer_EXP(a.getFils2(), valeur);
			
			Inst inst = Inst.creation2(Operation.STORE, valeur, variable);
			Prog.ajouter(inst);
						
			libererReg(valeur);
			break ;
		case Pour :
			//generer_PAS(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			break ;
		case TantQue:
			//generer_EXP(a.getFils1());
			generer_LISTE_INST(a.getFils2());
			
			break ;
		case Si :
			//generer_EXP(a.getFils1());
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
   /*private static void generer_PAS(Arbre a)  {
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
   }*/

   /**************************************************************************
    * PLACE
    **************************************************************************/
   private static Operande generer_PLACE(Arbre a, Operande reg)  {
	   switch (a.getNoeud()){
		case Ident :
			System.out.println(a.getChaine()+" "+a.getDecor().getDefn().getOperande());
			return a.getDecor().getDefn().getOperande();
		case Index:
			
			Operande reg2 = Operande.opDirect(premierRegLibre());
		
			Type cur = a.getDecor().getType();

			int taille = cur.getTaille();
			//System.out.println(a.getFils2().getEntier()+" "+taille);
			generer_EXP(a.getFils2(), reg);
			if(taille != 1){
				Prog.ajouter(Inst.creation2(Operation.MUL, Operande.creationOpEntier(taille), reg));
			}
			
			Operande place = generer_PLACE(a.getFils1(), reg2);
			if(place.getNature() != NatureOperande.OpIndirect){
				Prog.ajouter(Inst.creation2(Operation.ADD, reg2, reg));
			}
			int deplacement = place.getDeplacement();
			Registre regBase = place.getRegistreBase();
			
			libererReg(reg2);
			libererReg(reg);
			
			return Operande.creationOpIndexe(deplacement, regBase, reg.getRegistre());
		}
		return null;
   }
   
   /**
    * Fonction qui retourne le registre contenant l'indice d'un tableau.
    * <p>
    * Cette fonction va récupérer la valeur de l'indice, ou de l'identifiant qui contient la valeur de l'indice
    * et retourne une opérande à adressage direct contenant cette valeur.
    * @param a le noeud correspondant à l'indice d'un tableau
    * @return Operande de type opDirect qui contient la valeur de l'indice
    *
   private static Operande generer_EXP_CONST(Arbre a)  {
	   Operande reg;
	   switch(a.getNoeud()){
	   	case Ident :
	   		if(a.getDecor().getType() != Type.Integer){
	   			throw new ErreurOperande("Index du tableau non entier");
	   		}
	   		Inst inst;
	   		reg = Operande.opDirect(premierRegLibre());
			inst = Inst.creation2(Operation.LOAD, Operande.creationOpEntier(a.getDecor().getDefn().getValeurInteger()), reg);
			Prog.ajouter(inst);
			return reg;
	   	case Entier :
	   		reg = Operande.opDirect(premierRegLibre());
	   		Prog.ajouter(Inst.creation2(Operation.LOAD, Operande.creationOpEntier(a.getEntier()), reg),"Chargement de l'indice du tableau");
	   		return reg;
	   	case PlusUnaire :
	   		reg = Operande.opDirect(premierRegLibre());
	   		Prog.ajouter(Inst.creation2(Operation.LOAD, Operande.creationOpEntier(generer_EXP(a.getFils1()).getEntier()), reg),"Chargement de l'indice du tableau");
	   		return reg;

	   	case MoinsUnaire :
	   		reg = Operande.opDirect(premierRegLibre());
	   		Prog.ajouter(Inst.creation2(Operation.LOAD, Operande.creationOpEntier(-generer_EXP(a.getFils1()).getEntier()), reg),"Chargement de l'indice du tableau");
	   		return reg;
	   }
	return null;
   }*/


   /**************************************************************************
    * LISTE_EXP
    **************************************************************************/
   private static void generer_LISTE_EXP(Arbre a)  {
	   switch (a.getNoeud()){
		case Vide :
			break ;
		case ListeExp:
			generer_LISTE_EXP(a.getFils1());
			
			Operande reg = Operande.opDirect(premierRegLibre());
			generer_EXP(a.getFils2(), reg);
			libererReg(reg);
			
	      break ;
	   }
   }


   /**************************************************************************
    * EXP
    **************************************************************************/
   private static void generer_EXP(Arbre a, Operande reg)  {
	   Inst inst;
	   Type type;
	   Operande op;
	   switch (a.getNoeud()){
		/*case Et :
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
			
			break ;*/
		case Plus :
			operationArith(a, Operation.ADD, "Addition", reg);
			break;
			
		case Moins:
			operationArith(a, Operation.SUB, "Soustraction", reg);
			break;
			
		case Mult :
			operationArith(a, Operation.MUL, "Multiplication", reg);
			break;
			
		case DivReel:
			operationArith(a, Operation.DIV, "Division réelle", reg);
			break;
			
		case Reste :
			operationArith(a, Operation.MOD, "Reste", reg);
			break;
			
		/*case Quotient:
			generer_EXP(a.getFils1());
			generer_EXP(a.getFils2());
			
			break ;
			*/

		case Index :
			generer_EXP(a.getFils2(), reg);
			reg = Operande.opDirect(premierRegLibre());
			System.out.println("c nul");
			Operande place = generer_PLACE(a.getFils1(), reg);
			libererReg(reg);
			//return Operande.creationOpIndexe(0, place.getRegistreBase(), op.getRegistre());
			break;
			
		case PlusUnaire:
			generer_EXP(a.getFils1(), reg);
			break ;
			
		case MoinsUnaire :
			generer_EXP(a.getFils1(), reg);
			break ;
			
		case Non:
			generer_EXP(a.getFils1(), reg);
			break ;
			
		case Conversion :
			op = Operande.opDirect(premierRegLibre());
			generer_EXP(a.getFils1(), reg);
			Prog.ajouter(Inst.creation2(Operation.FLOAT, reg, op));
			break;
			
		case Entier:
			op = Operande.creationOpEntier(a.getEntier());
			Prog.ajouter(Inst.creation2(Operation.LOAD, op, reg), "Chargement entier");
			break;
			
		case Reel :
			op = Operande.creationOpReel(a.getReel());
			Prog.ajouter(Inst.creation2(Operation.LOAD, op, reg));
			break;
			
		case Chaine:
			op = Operande.creationOpChaine(a.getChaine());
			Prog.ajouter(Inst.creation2(Operation.LOAD, op, reg));
			break;
			
		case Ident :
			inst = Inst.creation2(Operation.LOAD, a.getDecor().getDefn().getOperande(), reg);
			
			Prog.ajouter(inst, "2.Chargement de la variable "+a.getChaine());
			break;
	   }
   }
   
   private static void operationArith(Arbre a, Operation op, String comment, Operande reg){
	   	boolean sens;
	   	//printReg();
		if((a.getFils1().getNoeud() == Noeud.Ident)||(a.getFils2().getNoeud() == Noeud.Ident)){
			sens = sensParcours(a, 1);
		}
		else{
			sens = sensParcours(a, 2);
		}
		Prog.ajouterComment(comment+", ligne "+a.getNumLigne());
		Inst inst;
		if(sens == true){
			generer_EXP(a.getFils1(), reg);
			
			Operande reg2 = Operande.opDirect(premierRegLibre());
			generer_EXP(a.getFils2(), reg2);

			inst = Inst.creation2(op, reg2, reg);
			libererReg(reg2);
			Prog.ajouter(inst, "gauche a droite");
			
		} else {
			generer_EXP(a.getFils2(), reg);
			
			Operande temp = Operande.creationOpIndirect(variableTemp, Registre.GB);
			Prog.ajouter(Inst.creation2(Operation.STORE, reg, temp), "droite a gauche");
			
			generer_EXP(a.getFils1(), reg);

			inst = Inst.creation2(op, temp, reg);
			Prog.ajouter(inst, "droite a gauche");
		}
   }
   
}




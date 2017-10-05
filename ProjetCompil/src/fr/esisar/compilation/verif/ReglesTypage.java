package fr.esisar.compilation.verif;

import fr.esisar.compilation.global.src.*;

/**
 * La classe ReglesTypage permet de définir les différentes règles
 * de typage du langage JCas.
 */

public class ReglesTypage {

   /**
    * Teste si le type t1 et le type t2 sont compatibles pour l'affectation,
    * c'est à dire si on peut affecter un objet de t2 à un objet de type t1.
    */

   static ResultatAffectCompatible affectCompatible(Type t1, Type t2) {
	   ResultatAffectCompatible res = null;
	   if(t1.getNature().equals(t2.getNature())){
		   res = new ResultatAffectCompatible();
		   res.setOk(true);
		   res.setConv2(false);
	   }
	   else if((t1.equals(Type.Real))&&(t2.equals(Type.Integer))){
		   res = new ResultatAffectCompatible();
		   res.setOk(true);
		   res.setConv2(true);
	   }
	   else if((t1.getNature() == NatureType.Real)&&(t2.getNature() == NatureType.Interval)){
		   res = new ResultatAffectCompatible();
		   res.setOk(true);
		   res.setConv2(false);
	   }
	   else if((t1.getNature() == NatureType.Array)&&(t2.getNature() == NatureType.Array)){
		   if(t1.getIndice().equals(t2.getIndice())){
			   res = new ResultatAffectCompatible();
			   res.setOk(true);
			   res.setConv2(false);
		   }
	   }
	   else{
		   res = new ResultatAffectCompatible();
		   res.setOk(false);
		   res.setConv2(false);
	   }
	   
	   return res;
   }

   /**
    * Teste si le type t1 et le type t2 sont compatible pour l'opération
    * binaire représentée dans noeud.
    */

   static ResultatBinaireCompatible binaireCompatible(Noeud noeud, Type t1, Type t2) {
      return null;
   }

   /**
    * Teste si le type t est compatible pour l'opération binaire représentée
    * dans noeud.
    */
   static ResultatUnaireCompatible unaireCompatible(Noeud noeud, Type t) {
      return null;
   }

}

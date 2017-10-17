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
	   res = new ResultatAffectCompatible();

	   if((t1.getNature() == NatureType.Interval)&&(t2.getNature() == NatureType.Interval)){
		   res.setOk(true);
		   res.setConv2(false);
	   }
	   else if((t1.getNature() == NatureType.Real)&&(t2.getNature() == NatureType.Real)){
		   res.setOk(true);
		   res.setConv2(false);
	   }
	   else if((t1.getNature() == NatureType.Boolean)&&(t2.getNature() == NatureType.Boolean)){
		   res.setOk(true);
		   res.setConv2(false);
	   }
	   else if((t1.getNature() == NatureType.Real)&&(t2.getNature() == NatureType.Interval)){
		   res.setOk(true);
		   res.setConv2(true);
	   }
	   else if((t1.getNature() == NatureType.Array)&&(t2.getNature() == NatureType.Array)){
		   if(t1.getIndice().getNature()==NatureType.Interval && t2.getIndice().getNature()==NatureType.Interval){
			   if(t1.getIndice().getBorneInf()==t2.getIndice().getBorneInf() && t1.getIndice().getBorneSup()==t2.getIndice().getBorneSup()){
				   if(affectCompatible(t1.getElement(),t2.getElement()).getOk()){
					   res.setOk(true);
					   res.setConv2(affectCompatible(t1.getElement(),t2.getElement()).getConv2());
				   }
				   else{
					   res.setOk(false);
					   res.setConv2(false);
				   }
			   }
			   else{
				   res.setOk(false);
				   res.setConv2(false);
			   }
		   }
		   else{
			   res.setOk(false);
			   res.setConv2(false);
		   }
	   }
	   else{
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
	   ResultatBinaireCompatible res = null;
	   res = new ResultatBinaireCompatible();

	   switch(noeud){
		case Et:
		case Ou:
				if(t1.getNature()==NatureType.Boolean && t2.getNature()==NatureType.Boolean){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Boolean);
				}
				else{
					res.setOk(false);
				}
			  break;
	   	case Egal:
	   	case Inf:
	   	case Sup:
	   	case NonEgal:
	   	case InfEgal:
	   	case SupEgal:
			if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Boolean);
				}
			else if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Real){
				   res.setOk(true);
				   res.setConv1(true);
				   res.setConv2(false);
				   res.setTypeRes(Type.Boolean);
			}
			else if(t1.getNature()==NatureType.Real && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(true);
				   res.setTypeRes(Type.Boolean);
			}
			else if(t1.getNature()==NatureType.Real && t2.getNature()==NatureType.Real){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Boolean);
			}
			else{
				res.setOk(false);
				   res.setConv1(false);
				   res.setConv2(false);
			}
		 break;
	   	case Plus:
	   	case Moins:
	   	case Mult:
			if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Integer);
				}
			else if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Real){
				   res.setOk(true);
				   res.setConv1(true);
				   res.setConv2(false);
				   res.setTypeRes(Type.Real);
			}
			else if(t1.getNature()==NatureType.Real && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(true);
				   res.setTypeRes(Type.Real);
			}
			else if(t1.getNature()==NatureType.Real && t2.getNature()==NatureType.Real){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Real);
			}
			else{
				   res.setOk(false);
				   res.setConv1(false);
				   res.setConv2(false);
			}
		 break;
	   	case Quotient:
	   	case Reste:
			if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Integer);
				}
			else{
					res.setOk(false);
				   res.setConv1(false);
				   res.setConv2(false);
			}
		 break;
	   	case DivReel:
			if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Real);
				}
			else if(t1.getNature()==NatureType.Interval && t2.getNature()==NatureType.Real){
				   res.setOk(true);
				   res.setConv1(true);
				   res.setConv2(false);
				   res.setTypeRes(Type.Real);
			}
			else if(t1.getNature()==NatureType.Real && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(true);
				   res.setTypeRes(Type.Real);
			}
			else if(t1.getNature()==NatureType.Real && t2.getNature()==NatureType.Real){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(Type.Real);
			}
			else{
					res.setOk(false);
				   res.setConv1(false);
				   res.setConv2(false);
			}
		 break;
	   	case Index:
			if(t1.getNature()==NatureType.Array && t1.getIndice().getNature()==NatureType.Interval && t2.getNature()==NatureType.Interval){
				   res.setOk(true);
				   res.setConv1(false);
				   res.setConv2(false);
				   res.setTypeRes(t1.getElement());
				}
			else{
					res.setOk(false);
				   res.setConv1(false);
				   res.setConv2(false);
			}
		 break;
	   	default:
	   		res.setOk(false);
	   		res.setConv1(false);
			res.setConv2(false);
		break;

	   }
      return res;
   }

   /**
    * Teste si le type t est compatible pour l'opération binaire représentée
    * dans noeud.
    */
   static ResultatUnaireCompatible unaireCompatible(Noeud noeud, Type t) {
	   ResultatUnaireCompatible res = null;
	   res = new ResultatUnaireCompatible();

	   switch(noeud){
		   	case Non:
			   switch(t.getNature()){
				   case Boolean:
					   res.setOk(true);
					   res.setTypeRes(Type.Boolean);
					   break;
				   default:
					   res.setOk(false);
					   break;
			   }
			break;
	   	case MoinsUnaire:
	   	case PlusUnaire:
			   switch(t.getNature()){
			   case Interval:
				   res.setOk(true);
				   res.setTypeRes(Type.Integer);
				   break;
			   case Real:
				   res.setOk(true);
				   res.setTypeRes(Type.Real);
				   break;
			   default:
				   res.setOk(false);
				   break;
			   }
		 break;
	   	default:
	   		res.setOk(false);
		break;

	   }
      return res;
   }

}

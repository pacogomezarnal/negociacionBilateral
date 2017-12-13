package agenteGuiado;

import java.util.Vector;

import negoUPV.UPVAgent;
import negotiator.Bid;

public class pacoAgent extends UPVAgent{

	Bid last_moment_offer;
	double S;
	double beta;
	double RU;
	//Almacenamiento de la oferta más ventajosa para mí
	Bid best_offer_for_me=null;
	//Calculo de las diferencias de ofertas del contrario
	double last_offer_S=-1;
	double just_offer=-1;
	double offers_acumulated;
	//Calculo de los incrementos de concesion
	double incr=0.25;
	double incr_part=1;
	//Tiempo de decision final
	double final_dec=0.95;
	
	public void initialize() {
		last_moment_offer = null;
		beta = 5;
		RU = 0.8;
		S = 0.99;
		update();
	}

	public boolean acceptOffer(Bid offer) {
		
		update();
		//Cambiamos las estrategias al pasar determinados intervalos de tiempo
		if(getTime()>(incr_part*incr)) {
			System.out.println(">>>ACUMULADO>>>>"+offers_acumulated);
			//Solo somos mas concesores si las ofertas no bajan
			if(offers_acumulated<0.2) {
				beta=beta*2;
				RU=RU-0.1;
				incr_part++;
			}
		}
		//Calcular si las ofertas van decreciendo
		just_offer=getUtility(offer);
		if(last_offer_S==-1) {
			offers_acumulated=last_offer_S;
			last_offer_S=just_offer;
		}else{
			offers_acumulated=offers_acumulated+(last_offer_S-just_offer);
			last_offer_S=just_offer;
		}
		//Almacenamos la mejor oferta para mi
		if(best_offer_for_me==null) {
			best_offer_for_me=offer;
		}else {
			if(getUtility(offer)>getUtility(best_offer_for_me)) best_offer_for_me=offer;
		}

		return  just_offer>= S;
	}

	private void update() {
		
		S = 1 - (1 - RU)*Math.pow(getTime(),1.0/beta);
	
	}

	public Bid proposeOffer() {			
			Bid selected=null;
			if(getTime()>=final_dec) {
				selected = best_offer_for_me;
			}else {
				Vector<Bid> m_bids = getOffers(S , S + 0.1);
		
				selected = m_bids.get(rand.nextInt(m_bids.size()));
			}
		
			return selected;
		
	}

}

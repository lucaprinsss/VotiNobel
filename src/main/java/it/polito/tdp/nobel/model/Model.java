package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {	
		EsameDAO dao=new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}


	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		//ripristino soluzione migliore
		migliore = new HashSet<Esame>();
		mediaMigliore = 0.0;
		
		Set<Esame> parziale=new HashSet<Esame>();
		//cerca1(parziale,0,m);
		cerca2(parziale,0,m);
		
		return migliore;	
	}

	
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//controllare casi terminali
		
		int sommaCrediti=sommaCrediti(parziale);
		
		if(sommaCrediti > m)   //soluzione non valida
			return;
			
		if (sommaCrediti==m){     //soluzione valida, controlliamo se è la migliore
			double mediaVoti=calcolaMedia(parziale);
			if(mediaVoti>mediaMigliore) {
				migliore=new HashSet<Esame>(parziale);       //se mettessi migliore=parziale salverei in migliore il riferimento a parziale che però cambia nel tempo!!
				mediaMigliore=mediaVoti;
			}
					
			return;
		}
		if(L==esami.size())
			return;
		
		//provo a aggiungere esami[L]
		parziale.add(esami.get(L));
		cerca2(parziale,L+1,m);
		
		//provo a non aggiungere esami[L]
		parziale.remove(esami.get(L));
		cerca2(parziale,L+1,m);
		
	}


	private void cerca1(Set<Esame> parziale, int L, int m) {
		//controllare casi terminali
		
		int sommaCrediti=sommaCrediti(parziale);
		
		if(sommaCrediti > m)   //soluzione non valida
			return;
		
		if (sommaCrediti==m){     //soluzione valida, controlliamo se è la migliore
			double mediaVoti=calcolaMedia(parziale);
			if(mediaVoti>mediaMigliore) {
				migliore=new HashSet<Esame>(parziale);       //se mettessi migliore=parziale salverei in migliore il riferimento a parziale che però cambia nel tempo!!
				mediaMigliore=mediaVoti;
			}
			
			return;
		}
		
		if(L==esami.size())
			return;
		
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale,L+1,m);
				parziale.remove(e);        //backtracking.  Se lavorassi con delle liste (che possono avere elementi duplicati) eliminerei solo un duplicato nella lista. Ecco perché con le liste si lavora togliendo l'ultimo elemento inserito: parziale.remove(parziale.size()-1)
				
			}
		}
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}

package it.polito.tdp.crimes.model;

/*
 * classe Model preimpostata questo documento è soggetto ai relativi diritti di
 * ©Copyright giugno 2021
 */

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import it.polito.tdp.crimes.db.EventsDao;

public class Model
{
	private EventsDao dao;
	private List<String> vertici;
	private Graph<String, DefaultWeightedEdge> grafo;

	public Model()
	{
		this.dao = new EventsDao();
	}

	public List<String> getCategories()
	{
		return this.dao.getCategories();
	}

	public List<Integer> getYears()
	{
		return this.dao.getYears();
	}

	public void creaGrafo(String category, int year)
	{
		// ripulisco mappa e grafo
		this.vertici = new ArrayList<>();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); //

		/// vertici
		this.dao.getVertici(vertici, category, year); // riempio la mappa
		Graphs.addAllVertices(this.grafo, this.vertici);

		/// archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(category, year));
		for (Adiacenza a : adiacenze)
		{
			if (this.vertici.contains(a.getTipo1()) && this.vertici.contains(a.getTipo2()))
				Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
		}
	}

	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public Collection<String> getVertici()
	{
		List<String> vertici = new ArrayList<>(this.grafo.vertexSet());
		// vertici.sort((v1,v2)-> );
		return vertici;
	}

	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}

	public List<Arco> getBestArchi()
	{
		List<Arco> list = new ArrayList<>();
		Double bestPeso = Double.MIN_NORMAL;
		for (DefaultWeightedEdge e : this.grafo.edgeSet())
		{
			double peso = grafo.getEdgeWeight(e);
			if (peso > bestPeso) bestPeso = peso;
		}
		for (DefaultWeightedEdge e : this.grafo.edgeSet())
		{
			double peso = grafo.getEdgeWeight(e);
			if (peso == bestPeso) list.add(new Arco(e, peso));

		}
		return list;
	}

	// RICORSIONE
	List<String> percorso;
	Double bestPeso;

	public List<String> calcolaPercorso(Arco arco)
	{
		String partenza = this.grafo.getEdgeSource(arco.e);
		String arrivo = this.grafo.getEdgeTarget(arco.e);

		this.percorso = new ArrayList<>();
		this.bestPeso = Double.MAX_VALUE;
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);

		this.cerca(parziale, arrivo);

		return this.percorso;
	}

	private void cerca(List<String> parziale, String arrivo)
	{
		if (parziale.size() == this.grafo.vertexSet().size()) // ho visitato tutti i vertici
		{
			if (parziale.get(parziale.size() - 1).equals(arrivo)) // ultimo vertice è arrivo
			{
				double peso = calcolaPesoArchi(parziale);
				if (peso < bestPeso)
				{
					bestPeso = peso;
					this.percorso = new ArrayList<>(parziale);
//					return;
				}
			}
		}
		String ultimo = parziale.get(parziale.size() - 1);
		for (String adiacente : Graphs.neighborListOf(this.grafo, ultimo))
		{
			if (!parziale.contains(adiacente))
			{
				parziale.add(adiacente);
				this.cerca(parziale, arrivo);
				parziale.remove(adiacente);
			}
		}
	}

	private Double calcolaPesoArchi(List<String> parziale)
	{
		Double peso = 0.0;
		if (parziale.size() > 1)
			for (int i = 1; i < parziale.size(); i++)
				peso += grafo.getEdgeWeight(grafo.getEdge(parziale.get(i-1), parziale.get(i))); 
		return peso;
	}
}
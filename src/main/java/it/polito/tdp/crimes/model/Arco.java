package it.polito.tdp.crimes.model;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Arco
{
	public DefaultWeightedEdge e ; 
	public Double peso;

	public Arco(DefaultWeightedEdge e, double peso)
	{
		 this.e = e; 
		 this.peso = peso; 
	}

	@Override public String toString()
	{
		return e + " (" + peso + ")";
	}
	
}

/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxCategoria"
	private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

	@FXML // fx:id="boxAnno"
	private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="boxArco"
	private ComboBox<Arco> boxArco; // Value injected by FXMLLoader

	@FXML // fx:id="btnPercorso"
	private Button btnPercorso; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML void doCreaGrafo(ActionEvent event)
	{
		// controlli input
		String category = this.boxCategoria.getValue();
		if (category == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		}
		Integer year = this.boxAnno.getValue();
		if (year == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		}

		// resetto testo
		this.txtResult.clear();
		this.txtResult.appendText("Creo grafo...\n");

		// creo grafo
		this.model.creaGrafo(category, year);
		txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d", this.model.getNumVertici(),
				this.model.getNumArchi()));

		// cliccabili
		this.btnPercorso.setDisable(false);
		this.boxArco.setDisable(false);

		// aggiungo risultati alla combobox
		this.boxArco.getItems().clear();
		this.boxArco.getItems().addAll(this.model.getBestArchi());
	}

	@FXML void doCalcolaPercorso(ActionEvent event)
	{
		// controlli input
		Arco arco = this.boxArco.getValue();
		if (arco == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		}
		
		this.txtResult.appendText("\n\nPERCORSO da " + arco);
		for (String vertice : this.model.calcolaPercorso(arco))
		{
			this.txtResult.appendText("\n" + vertice);
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model;

		this.boxCategoria.getItems().addAll(this.model.getCategories());
		this.boxAnno.getItems().addAll(this.model.getYears());
	}
}

package group11;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;


import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;


public class SceneController {
	private QueryExecution x;
	private ResultSet results;
	private List<String> columnNames;
	    @FXML
	    private ScrollPane table;
	    @FXML
	    private VBox box;
	    @FXML
	    private WebView browser;
	    @SuppressWarnings("deprecation")
		public void display(ActionEvent event) {
	    	try {
	    		  
	    		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
	            String location ="http://dbpedia.org/sparql";
	            queryStr.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	            queryStr.setNsPrefix("yago", "http://dbpedia.org/class/yago/");
	            queryStr.setNsPrefix("dbp", "http://dbpedia.org/property/");
	            queryStr.setNsPrefix("dbo", "http://dbpedia.org/ontology/");
	            queryStr.setNsPrefix("dbr", "http://dbpedia.org/resource/");
	            queryStr.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	            queryStr.append("\n" +
	                    "\n" +
	                    "select distinct ?p ?abstract ?thumb\n" +
	                    "where {\n" +
	                    "       {{?p rdf:type dbo:Cave.} UNION {?p rdf:type dbo:NaturalPlace.}}\n" +
	                    "       {{?p dbo:location dbr:Vietnam.} UNION {?p dbo:country dbr:Vietnam.}}\n" +
	                    "       ?p rdfs:comment ?abstract.\n" +
	                    "       ?p dbo:thumbnail ?thumb\n" +
	                    "       FILTER ( LANG ( ?abstract ) = 'en' )\n" +
	                    "}\n" +
	                    "LIMIT 100");

	            Query query = queryStr.asQuery();
	    		x = QueryExecutionFactory.sparqlService(location, query);
	            results = x.execSelect() ;
	            columnNames = results.getResultVars();
	            //Data get
	            for (; results.hasNext() ; )
	            {
	            	TextFlow displayData = new TextFlow();
	                QuerySolution soln = results.nextSolution() ;
	                for(int i=0;i<columnNames.size();i++)
	                {	
	                    String columnName=columnNames.get(i);
	                    RDFNode rdfNode=soln.get(columnName);
	                    Text text = new Text(rdfNode.toString()+" "+"\n");
	                	text.setFill(Color.BLUE);
	                    text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
	                    displayData.getChildren().add(text);
	                   if(i==0 ||  i ==2) {
	                	   Hyperlink hyperlink = new Hyperlink(rdfNode.toString());
		                    hyperlink.setOnAction(new EventHandler<ActionEvent>() {

		                        @Override
		                        public void handle(ActionEvent event) {
		                            WebEngine webEngine = browser.getEngine();
		                            webEngine.load(rdfNode.toString());
		                        }
		                    });
		                    box.getChildren().add(hyperlink);
	                   }
	                }
	                box.getChildren().add(displayData);
	               
	            }
	        }
	        catch (Exception e) {
	  
	            System.out.println(e.getMessage());
	        }
	}
	    @FXML
	    private Button save_button;
	    @FXML
	    void savefile(ActionEvent event) throws IOException {
	    	EventHandler<ActionEvent> save_event = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e)
	            {
	            	String pathName="D:\\20212\\midtermjava\\";
	    	        File file = new File(pathName+"dataset.nt");
	    	        try {
						file.createNewFile();
						OutputStream outputStream = new FileOutputStream(file);
						Model model =x.execConstruct();
		    	        model.write(outputStream, "TURTLE");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	        };
	    	save_button.setOnAction(save_event);
	    }
}

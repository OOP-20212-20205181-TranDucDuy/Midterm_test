package group11;



import java.io.IOException;
import java.util.List;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import javafx.scene.control.Hyperlink;
import javafx.scene.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class SceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	public void switchtoWorkingScene(ActionEvent event) throws IOException {
		root  = FXMLLoader.load(getClass().getResource("WorkingScene.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

//	    @FXML
//	    void 8d1fb8(ActionEvent event) {
//
//	    }
//
//	    @FXML
//	    void 8d1fb8(ActionEvent event) {
//
//	    }
	    @FXML
	    private ScrollPane table;
	    @FXML
	    private VBox box;
	    @FXML
	    private WebView browser;
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
	    		QueryExecution x = QueryExecutionFactory.sparqlService(location,query);
	            ResultSet results = x.execSelect() ;
	            List<String> columnNames = results.getResultVars();
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
	    
}

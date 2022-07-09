package group11;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Data {
	public static void main(String[] args) throws IOException {
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
            QuerySolution soln = results.nextSolution() ;
            for(int i=0;i<columnNames.size();i++)
            {
                String columnName=columnNames.get(i);
                RDFNode rdfNode=soln.get(columnName);
                rdfNode.toString();
                System.out.println(rdfNode);
            }
            System.out.println("\n");
        }


        ////Save data
        String pathName="D:\\20212\\midtermjava\\testjena\\src\\tess\\";
        Scanner sc =new Scanner(System.in);
        System.out.println("Enter Name of file you want to save:");
        String text =sc.nextLine();
        File file = new File(pathName+text);
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        Model model =x.execConstruct();
        model.write(outputStream, "TURTLE");
        sc.close();
    }
    }
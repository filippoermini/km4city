package Application;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class RDFconnector {

	public RDFconnector(){
		String sparqlEndpoint = "http://servicemap.disit.org/WebAppGrafo/sparql";
		Repository repo = new SPARQLRepository(sparqlEndpoint);
		repo.initialize();
		
		try (RepositoryConnection conn = repo.getConnection()) {
			   String queryString = "SELECT DISTINCT ?id WHERE {"
					   					+"?s a km4c:SensorSite. ?s dcterms:identifier ?id.filter(!strstarts(?id,\"METRO\"))"
										+"} order by ?id limit 100";
			   TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			   try (TupleQueryResult result = tupleQuery.evaluate()) {
			      while (result.hasNext()) {  // iterate over the result
				   BindingSet bindingSet = result.next();
				   Value valueOfX = bindingSet.getValue("id");
				   System.out.println(valueOfX);
				   // do something interesting with the values here...
			      }
			   }
		}
	}
}

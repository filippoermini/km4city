package Application;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class RDFconnector {

	private String endPoint;
	private Repository repo;
	final static Logger logger = Logger.getLogger(RDFconnector.class);
	public RDFconnector(String endPoint){
		
		
		this.endPoint = endPoint;//"http://servicemap.disit.org/WebAppGrafo/sparql";
		repo = new SPARQLRepository(endPoint);
		repo.initialize();
	}
	
	public TupleQueryResult SPARQLExecute(String query){
		try (RepositoryConnection conn = repo.getConnection()) {
//			   String queryString = "SELECT DISTINCT ?id WHERE {"
//					   					+"?s a km4c:SensorSite. ?s dcterms:identifier ?id.filter(!strstarts(?id,\"METRO\"))"
//										+"} order by ?id limit 100";
			   TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
			   TupleQueryResult result = tupleQuery.evaluate();
			   return result;
		}catch(Exception ex){
			logger.error("Error during query execution :"+ex.getMessage());
		}
		return null;
	}
}
	
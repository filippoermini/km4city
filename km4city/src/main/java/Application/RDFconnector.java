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
		repo = new SPARQLRepository(this.endPoint);
		repo.initialize();
	}
	
	public TupleQueryResult SPARQLExecute(String query){
		RepositoryConnection conn = repo.getConnection(); 
//	    String queryString = "SELECT DISTINCT ?id WHERE {"
//					   					+"?s a km4c:SensorSite. ?s dcterms:identifier ?id.filter(!strstarts(?id,\"METRO\"))"
//										+"} order by ?id limit 100";
//			
		TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
		TupleQueryResult result = tupleQuery.evaluate();
		return result;
			
		
			//logger.error("Error during query execution :"+ex.getMessage());
		
		
//	    Repository repo = new SPARQLRepository("http://servicemap.disit.org/WebAppGrafo/sparql");
//	    repo.initialize();
//	    
//	    RepositoryConnection con = this.repo.getConnection();
//	    TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
//	    TupleQueryResult result = tupleQuery.evaluate();
//
//	      while (result.hasNext()) {
//	        BindingSet bindingSet = result.next();
//	        for(String n:result.getBindingNames()) {
//	          Value value = bindingSet.getValue(n);
//	          if(value!=null) {
//	            System.out.println(n+":"+value.stringValue());
//	          }
//	        }
//	      }
	      
	}
}
	
package Application;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	public class RepositoryManager{
		
		private String endPoint;
		private Repository repo;
		
		public RepositoryManager(String endPoint){
			this.endPoint = endPoint;//"http://servicemap.disit.org/WebAppGrafo/sparql";
			repo = new SPARQLRepository(this.endPoint);
			repo.initialize();
		}

		private String getEndPoint() {
			return endPoint;
		}

		private Repository getRepo() {
			return repo;
		}
		
		public TupleQueryResult SPARQLExecute(String query){
			RepositoryConnection conn = repo.getConnection(); 
//		    String queryString = "SELECT DISTINCT ?id WHERE {"
//						   					+"?s a km4c:SensorSite. ?s dcterms:identifier ?id.filter(!strstarts(?id,\"METRO\"))"
//											+"} order by ?id limit 100";
//				
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
			TupleQueryResult result = tupleQuery.evaluate();
			return result;
		}
		
		
	}
	
	private ArrayList<RepositoryManager> repositoryList;
	final static Logger logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	private static RDFconnector instance = new RDFconnector();
	
	private RDFconnector(){
		repositoryList = new ArrayList<>();
	}
	private void add(String endPoint){
		if(getRepository(endPoint)==null){
			RepositoryManager rm = new RepositoryManager(endPoint);
			this.repositoryList.add(rm);
		}
	}
	
	public static RepositoryManager getInstance(String endPoint) {
	      if(instance.getRepository(endPoint)==null){
	    	  instance.add(endPoint);
	      }
	      return instance.getRepository(endPoint);
	}
	
	private RepositoryManager getRepository(String endpoint){
		
		RepositoryManager rm;
		Iterator<RepositoryManager> it = repositoryList.iterator();
		while(it.hasNext()){
			rm = it.next();
			if(rm.getEndPoint().equals(endpoint))
				return rm;
		}
		return null;
	}
}
	
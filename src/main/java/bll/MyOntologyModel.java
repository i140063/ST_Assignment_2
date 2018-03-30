/**
 * 
 */
package bll;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;//ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
//import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

/**
 * @author Kindred Spirits
 * 
 * While un merged ontos allow me to get all classes they dont read individuals from imported ontos
 * so work nis done on unmerged version while queries are run on merged version.
 * 
 * ? above has been resolved by reading the quran.owl as well as myymodel from system directly.
 * 
 * WHERE {?person sm:ParticipatedInAssertion ?assertion.
 ?assertion sm:ParticipatedInEvent ?event.
 ?assertion sm:AssertedBy ?hadith}
 *
 */
public class MyOntologyModel {
	
	public static MyOntologyModel CreateMyOntologyModel() {
		
		if (myOntologyModel==null) {
			MyOntologyModel.myOntologyModel = new MyOntologyModel();
		}
		
		return MyOntologyModel.myOntologyModel;
	}
	
	OntModel model;
	String extension = ".owl";
	String filename ="mymodel";//"annotated_model_imported";//Merged";//Merged";
	String inputfilename=/*path+*/filename+extension;
	
	
	private MyOntologyModel() {
	

		OntDocumentManager mgr = new OntDocumentManager();
			
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		s.setDocumentManager( mgr );
		model = ModelFactory.createOntologyModel( s, null );
		model.setStrictMode(false);
		model.read(inputfilename,"RDF/XML" );
		
		String inputfilename2 = /*path+*/"quran"+extension;
		model.read(inputfilename2,"RDF/XML-ABBREV");	
	};
	
	private static MyOntologyModel myOntologyModel;
	
	static final String prefix = 
		       "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
		       "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
		       "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n" + 
		       "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
		       "PREFIX qur: <http://quranontology.com/Resource/>\r\n" + 
		       "PREFIX sm: <http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#>\r\n";
	
	static final String HadithOnt="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#";
	
	public List<List<String>>  SparqlQuery(String sparqlQuery, String[] columns) throws SQLException, ClassNotFoundException, IOException {
		
		
			String queryString= prefix + sparqlQuery;
			Query query= QueryFactory.create(queryString);
			QueryExecution qexec=QueryExecutionFactory.create(query, model);
			ResultSet results = null;
			List<List<String>> queryResult = new ArrayList<List<String>>();
			try {
			    results =  qexec.execSelect();
			    while ( results.hasNext())
			    {
			    	List<String> list = new ArrayList<String>();
			    	
			    	QuerySolution soln = results.nextSolution();
			        for (String s1: columns) {
			        	if (soln.get(s1) instanceof org.apache.jena.rdf.model.Resource)
			        		list.add(soln.getResource(s1).getLocalName());
			        	else
			        		list.add(soln.getLiteral(s1).getLexicalForm());
			        }
	//		        System.out.println(list);
			        queryResult.add(list);
			    }
			    
			} finally 
			{
				qexec.close();
			}
			return queryResult;
		
		
	}
	
	
	/*
	 * returns 1 for success
	 * returns 0 for problem in writing file, wether its due to malformed rdf or outputstream
	 * returns -1 for incorrect class uri
	 * */
	public int InsertIndvidual(String className, String uri, String label, String comment) {
		//String filename = "mymodel.owl";
		//String inputfilename=/*path+*/filename/*+extension*/;
		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
		s.setDocumentManager( mgr );
		OntModel model = ModelFactory.createOntologyModel( s, null );
		model.read(inputfilename,"RDF/XML-ABBREV" );
		
		String ClassUri =  className;
		System.out.println("ClassUri: "+ClassUri);
		OntClass c = model.getOntClass(ClassUri);
		if (c==null)
			return -1;
		System.out.println("Class"+c.getLocalName());
		String individualUri = this.HadithOnt+uri;
		System.out.println(individualUri);
		
		Individual individual = model.createIndividual(individualUri, c);
		individual.addLabel( label, "en" );
		individual.addComment(comment, "en");
			FileWriter out = null;
		try {
			  // XML format - long and verbose
			  out = new FileWriter(inputfilename /*path+*//*"mymodel.owl"*/ );
		
			  model.write( out,"RDF/XML-ABBREV"  );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
			  if (out != null) {
			   try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
			  }
			}
		
			
		
		
		return 1;
	}
		
	public boolean IndvidualRelations(String domain, String property, String range) {
		//String filename = "mymodel.owl";
		//String inputfilename=/*path+*/filename/*+extension*/;
		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
		s.setDocumentManager( mgr );
		OntModel model = ModelFactory.createOntologyModel( s, null );
		model.read(inputfilename,"RDF/XML-ABBREV" );
		
		String propertyUri = property;
		OntProperty  Property = model.getOntProperty(propertyUri);
		
		String domainUri = domain;
		Individual domainIndividual = model.getIndividual(domainUri);
		
		String rangeUri =  range;
		Individual rangeIndividual = model.getIndividual(rangeUri);
		
		domainIndividual.addProperty(Property, rangeIndividual);
	
		FileWriter out = null;
		try {
			  // XML format - long and verbose
			  out = new FileWriter( /*path+*/"mymodel.owl" );
		
			  model.write( out,"RDF/XML-ABBREV"  );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
			  if (out != null) {
			   try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			  }
			}
		
	return true;
	}
	
	
	List<String> traversedClasses;
	
	public OntologyClass AddSubClasses(OntClass c) {
		ExtendedIterator<OntClass> i = c.listSubClasses();
		OntologyClass ontoclass = new OntologyClass(c.getLocalName(),c.listInstances().toList().size());
		System.out.println(c.getLocalName());
		this.traversedClasses.add(c.getLocalName());
		if (i.hasNext()==false) {
			ontoclass.setSubClasses(null);
			return ontoclass;
		}
		else {
			List<OntologyClass> classes = new ArrayList<OntologyClass>();
			while (i.hasNext()) 
			{
				OntClass c2 = (OntClass) i.next();
				classes.add(AddSubClasses(c2));
							
			}
			ontoclass.setSubClasses(classes);
			return ontoclass;
		}
	}
	
public List<OntologyClass>  BrowseOntology() throws  ClassNotFoundException, IOException {
		
	OntDocumentManager mgr = new OntDocumentManager();
	//mgr.addAltEntry("http://quranontology.com/Resource", "file:C:\\Users\\THELAPTOP-HUT\\eclipse-workspace\\FYP\\quran.owl");
	
	
	OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_DL_MEM_RDFS_INF);
	s.setDocumentManager( mgr );
	OntModel model = ModelFactory.createOntologyModel( s, null );
	model.setStrictMode(false);
	model.read(inputfilename,"RDF/XML" );
	

	String inputfilename2 = /*path+*/"quran.owl"/*+extension*/;
	model.read(inputfilename2,"RDF/XML-ABBREV");
		
		
		traversedClasses = new ArrayList<String>();
		for (ExtendedIterator i = model.listClasses(); i.hasNext(); ) 
		{
			OntClass c = (OntClass) i.next();
			System.out.println();
			if (this.traversedClasses.contains(c.getLocalName())!=true)
				this.AddSubClasses(c);
		}
		
				return null;
		
	}

}


/**
 * 
 */
package bll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author THELAPTOP-HUT
 *
 */
public class SemanticHadithController {
	
	private static SemanticHadithController uniqueController;

	public static SemanticHadithController Instance ()
	{
		if (uniqueController==null)
		{
			uniqueController = new SemanticHadithController();
		}
		return uniqueController;
	}
	
	
	public CustomResult RefineAndExecuteSparqlQuery ( String select, String query) {

		String  arr[] =  select.split(" ");//{"x","y"};
		String completeQuery = "SELECT ";
		for (String s1: arr){
			completeQuery = completeQuery + " ?"+s1;
		}
		completeQuery += "\r\n";
		completeQuery += query;
		CustomResult result = SparqlQuery(completeQuery, arr);
		return result;
	}
	
	public CustomResult SparqlQuery(String sparqlQuery, String[] columns) {
		MyOntologyModel onto = MyOntologyModel.CreateMyOntologyModel();
		CustomResult result = new CustomResult();
		try {
			result.setResultSet(onto.SparqlQuery(sparqlQuery, columns));
			result.setExecutionStatus(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result.setExecutionStatus(false);
		}
		return result;
		
	}
	
	public CustomResult BrowseOntology() {
		MyOntologyModel onto = MyOntologyModel.CreateMyOntologyModel();
		try {
			onto.BrowseOntology();
		} catch (ClassNotFoundException |  IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
		
	}
	
	public int SparqlIndvidualInsert(String className, String uri, String label, String comment) {
		MyOntologyModel onto = MyOntologyModel.CreateMyOntologyModel();
		int success = onto.InsertIndvidual(className, uri, label, comment);
		
		return success;
		
	}
	
	public boolean SparqlIndvidualRelations(String domainUri, String propertyUri, String rangerUri) {
		MyOntologyModel onto = MyOntologyModel.CreateMyOntologyModel();
		boolean success = onto.IndvidualRelations(domainUri,  propertyUri, rangerUri);
		
		return success;
		
	}
	
	public List<PropertyValue> SearchProfile(String uri){

		if (uri!=null)
		{
			String  arr[] = {"property", "range"};
			String completeQuery = "SELECT ?property ?range \r\n WHERE { "+uri+" ?property ?range. FILTER(?property != owl:differentFrom) }";
			CustomResult result = SparqlQuery(completeQuery, arr);
	
			List<List<String>> results = result.getResultSet();
			List<PropertyValue> refinedResult = new ArrayList<PropertyValue>();
			List<String> properties = new ArrayList<String>();
			for (List<String>list: results ) {
				//
				int index = properties.indexOf(list.get(0));
				if (list.get(1)!=null && list.get(1).equalsIgnoreCase("Resource")!=true && list.get(1).equalsIgnoreCase("NamedIndividual")!=true && list.get(1).equalsIgnoreCase("Thing")!=true) {
					if (index == -1 ) {
						refinedResult.add(new PropertyValue(list.get(0), list.get(1)));
						properties.add(list.get(0));
					}
					else {
						refinedResult.get(index).addValue(list.get(1));
					}
				}
			}
	
			
			for(PropertyValue val: refinedResult) { 
				boolean flag = true;
				for (String s1: val.getValues()){
					if (flag==true){ 
						flag=false;
						val.getValues().size();
						val.getPropertyName();
						//System.out.println(val.getValues().size() + val.getPropertyName());
			    }
			   }
			}
			
			
			return refinedResult;
		}
		else
		{
			return null;
		}
		
	}
	
}

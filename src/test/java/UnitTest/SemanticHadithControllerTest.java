/**
 * 
 */
package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import bll.CustomResult;
import bll.PropertyValue;
import bll.SemanticHadithController;
import bll.automatedAnnotationModule.AutomatedAnnotationModule;
import servlets.SelectServlet;

/**
 * @author THELAPTOP-HUT
 *
 */
class SemanticHadithControllerTest extends Mockito{



	static private SemanticHadithController semanticHadithController;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		semanticHadithController = SemanticHadithController.Instance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testProfileValueFromQuranOntology() {
		List<PropertyValue> ff = semanticHadithController.SearchProfile("qur:Aicha");
		assertNotNull(ff);
	}
	
	@Test
	void testProfileValueFromSHOntology() {
		List<PropertyValue> ff = semanticHadithController.SearchProfile("sm:Al-Ushairah");
		assertNotNull(ff);
	}
	
	//uri is passed as null
	@Test
	void testProfileValuewithNull() {
		List<PropertyValue> result = semanticHadithController.SearchProfile(null);
		assertNull(result);
	}
	
	//uri is not present in the system
	@Test
	void testProfileValuewithMissingUri() {
		List<PropertyValue> result = semanticHadithController.SearchProfile("sm:abc");
		List<List<String>> check = new ArrayList<List<String>>();
		assertEquals(check, result);
	}
	
	@Test
	void testSearchQueryWithCorrectValues() {
		CustomResult result = semanticHadithController.RefineAndExecuteSparqlQuery("x", "WHERE {?x a qur:Campaign}");
		assertEquals(10, result.getResultSet().size());
	}
	
	//passing 1 select param while using 2 in where
	@Test
	void testSearchQueryWithLessSelectValues() {
		CustomResult result = semanticHadithController.RefineAndExecuteSparqlQuery("x", "WHERE {?x a ?y}");
		assertEquals(true, result.isExecutionStatus() );
	}
	
	//passing 2 select param while using 1 in where
	@Test
	void testSearchQueryWithMoreSelectValues() {
		CustomResult result = semanticHadithController.RefineAndExecuteSparqlQuery("x y", "WHERE {?x a qur:Campaign}");
		assertEquals(false, result.isExecutionStatus() );
	}
		
	//syntax error in where
		@Test
		void testSearchQueryWithIncorrectWhereValue() {
			CustomResult result = semanticHadithController.RefineAndExecuteSparqlQuery("x", "WHERE {?x qur:Campaign}");
			assertEquals(false, result.isExecutionStatus() );
		}	
		
		@Test
		void testBrowseOntology() {
			CustomResult result = semanticHadithController.BrowseOntology();
			assertNull(result);
		}	
		
		@Test
		void testIndividualInsertionCorrectly() {/*
			<option value="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#">SemanticHadith</option>
        	<option value="http://quranontology.com/Resource/">QuranOntology</option>*/
			String classUri="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#"+"Assertion";
			
			semanticHadithController.SparqlIndvidualInsert(classUri, "testIndividualInsertion", "Test Individual Insertion", "Running a testcase for correct individual Insertion");
			List<PropertyValue> result = semanticHadithController.SearchProfile("sm:testIndividualInsertion");
			List<List<String>> check = new ArrayList<List<String>>();
			assertNotEquals(check, result);
			
		}
		
		//trying to make an individual of a class that doesn't exist
		@Test
		void testIndividualInsertionNonExistentClass() {/*
			<option value="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#">SemanticHadith</option>
        	<option value="http://quranontology.com/Resource/">QuranOntology</option>*/
			String classUri="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#"+"NonExistent";
			
			int check = semanticHadithController.SparqlIndvidualInsert(classUri, "testIndividualInsertion", "Test Individual Insertion", "Running a testcase for correct individual Insertion");
			assertEquals(-1,check);
		}
		
		//trying to make an individual of a class that doesn't exist
		@Test
		void testSparqlIndvidualRelationsCorrectValues() {/*
			<option value="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#">SemanticHadith</option>
        	<option value="http://quranontology.com/Resource/">QuranOntology</option>*/
			String classUri="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#";
			String domainUri =classUri+"Assertion";
			String rangeUri =classUri+"Assertion";
			String propertyUri =classUri+"Has_Object";
			
			
			boolean check = semanticHadithController.SparqlIndvidualRelations(domainUri, propertyUri, rangeUri);//(classUri, "testIndividualInsertion*&(_", "Test Individual Insertion", "Running a testcase for correct individual Insertion");
			assertEquals(true,check);
		}
		
		/*@Test
		void testAnnotationModuleHadithBooks() {
			AutomatedAnnotationModule.AnnotateHadithBooks("Sahih al-Bukhari", 10);
			
		}
		@Test
		void testAnnotationModuleHadithChapters() {
			AutomatedAnnotationModule.AnnotateHadithBooks("Sahih al-Bukhari", 10);
			AutomatedAnnotationModule.AnnotateHadithChapters("Sahih al-Bukhari",64, 10);
			AutomatedAnnotationModule.AnnotateHadithIndividuals("Sahih al-Bukhari",64, 10);
		}*/
		@Test
		void testAnnotationModuleHadithIndividuals() {
			AutomatedAnnotationModule.AnnotateHadithBooks("Sahih al-Bukhari", 0);
			AutomatedAnnotationModule.AnnotateHadithChapters("Sahih al-Bukhari",64, 10);
			AutomatedAnnotationModule.AnnotateHadithIndividuals("Sahih al-Bukhari",64, 10);
		}
		
	    @Test
	    public void testServlet() throws Exception {
	        HttpServletRequest request = mock(HttpServletRequest.class);       
	        HttpServletResponse response = mock(HttpServletResponse.class);    

	        when(request.getParameter("FormName")).thenReturn("random");
	        
	        StringWriter stringWriter = new StringWriter();
	        PrintWriter writer = new PrintWriter(stringWriter);
	        when(response.getWriter()).thenReturn(writer);

	        new SelectServlet().doPost(request, response);

	        verify(request, atLeast(1)).getParameter("WrongForm");// getParameter("username"); // only if you want to verify username was called...
	        writer.flush(); // it may not have been flushed yet...
	       // assertTrue(stringWriter.toString().contains("Exception"));
	    }
	    
	    @Test
	    public void testServletInsertion() throws Exception {
	        HttpServletRequest request = mock(HttpServletRequest.class);       
	        HttpServletResponse response = mock(HttpServletResponse.class);    

	        when(request.getParameter("FormName")).thenReturn("companionInstance");
	        when(request.getParameter("ontoUriClass")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
	        when(request.getParameter("class")).thenReturn("Assertion");
	        when(request.getParameter("uri")).thenReturn("test_Insertion");
	        when(request.getParameter("label")).thenReturn("Test Insertion");
	        when(request.getParameter("comment")).thenReturn("comment test Insertion");
	        	
	        
	        StringWriter stringWriter = new StringWriter();
	        PrintWriter writer = new PrintWriter(stringWriter);
	        when(response.getWriter()).thenReturn(writer);

	        new SelectServlet().doGet(request, response);

	        verify(response, atLeast(1)).sendRedirect("InstanceInsertion.jsp"); // only if you want to verify username was called...
	        writer.flush(); // it may not have been flushed yet...

	    }
	    
	   		@Test
		    public void testServletPropertyn() throws Exception {
	   			
		        HttpServletRequest request = mock(HttpServletRequest.class);       
		        HttpServletResponse response = mock(HttpServletResponse.class);    

		        when(request.getParameter("FormName")).thenReturn("propertyLink");
		        when(request.getParameter("ontoUriDomain")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
		        when(request.getParameter("ontoUriProperty")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
		        when(request.getParameter("ontoUriRange")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
		        when(request.getParameter("domain")).thenReturn("Assertion");
		        when(request.getParameter("property")).thenReturn("Has_Object");
		        when(request.getParameter("range")).thenReturn("Assertion");
		        	
		        
		        StringWriter stringWriter = new StringWriter();
		        PrintWriter writer = new PrintWriter(stringWriter);
		        when(response.getWriter()).thenReturn(writer);

		        new SelectServlet().doGet(request, response);

		        verify(response, atLeast(1)).sendRedirect("PropertyLinks.jsp"); // only if you want to verify username was called...
		        writer.flush(); // it may not have been flushed yet...

		    }	

		    
	   		@Test
		    public void testServletException() throws Exception {
	   			
		        HttpServletRequest request = mock(HttpServletRequest.class);       
		        HttpServletResponse response = mock(HttpServletResponse.class);    

		        when(request.getParameter("FormName")).thenReturn("propertyLink");
		        when(request.getParameter("ontoUriDomain")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
		        when(request.getParameter("ontoUriProperty")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
		        when(request.getParameter("ontoUriRange")).thenReturn("http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#");
		        when(request.getParameter("domain")).thenReturn("Assertion");
		        when(request.getParameter("property")).thenReturn("has_Object");
		        when(request.getParameter("range")).thenReturn("Assertion");
		        	
		        
		        StringWriter stringWriter = new StringWriter();
		        PrintWriter writer = new PrintWriter(stringWriter);
		        when(response.getWriter()).thenReturn(writer);

		        new SelectServlet().doGet(request, response);

		        verify(response, atLeast(1)).sendRedirect("GenericError.jsp"); // only if you want to verify username was called...
		        writer.flush(); // it may not have been flushed yet...

		    }	

}

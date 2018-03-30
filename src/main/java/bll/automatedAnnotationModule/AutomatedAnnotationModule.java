package bll.automatedAnnotationModule;


import bll.structuredConcepts.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.sql.Connection;




public class AutomatedAnnotationModule {
	
	static String prefix = 
		       "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
		       "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
		       "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n" + 
		       "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
		       "PREFIX qur: <http://quranontology.com/Resource/>\r\n" + 
		       "PREFIX sm: <http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#>\r\n";
	static String url ;//="jdbc:sqlserver://SweetMercy;databaseName=HadithOntology;integratedSecurity=true";
	
	static final String inputfilename="test.owl";
	static final String readInputFilename = "SemanticHadith_finalSkeleton_unmerged.owl";//"SemanticHadith_finalSkeleton_unmerged.owl";//"SemanticHadithCorrectedVersion_imported.owl";//"SemanticHadith_correctedMerged2.owl";//HadithOntologyVersion-1.owl";
	static String HadithOnt="http://www.semanticweb.org/ontologies/2017/9/SemanticHadith#";//"http://www.semanticweb.org/awaisali/ontologies/2017/10/hadithOntologyFinalized#";
	
	/*
	private static Connection getConnection() {
		String StrURL, DBName, DBConnectionDriver,DBUserName, DBPassword;
		StrURL = "jdbc:mysql://localhost:3306/";
        DBName = "fyp";
        DBUserName = "fypfyp";
        DBPassword = "fypfyp";
		url = StrURL + DBName;//, DBUserName, DBPassword;
		
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, DBUserName, DBPassword);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return conn;
	}*/
	
/*	
	static OntModel prepareOntModel() {
		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
		s.setDocumentManager( mgr );
		return ModelFactory.createOntologyModel( s, null );
	}*/
	/*
	static void writeModel(OntModel model) {
		
		FileWriter out = null;
		try 
		{
			out = new FileWriter( inputfilename );
			model.write( out,"RDF/XML-ABBREV"  );
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}*/
	
	
	
	public static void main(String[] args) {
		
	/*
		AutomatedAnnotationModule.AnnotateHadithBooks("Sahih al-Bukhari", 0);
		AutomatedAnnotationModule.AnnotateHadithChapters("Sahih al-Bukhari",64, 0);
		AutomatedAnnotationModule.AnnotateHadithIndividuals("Sahih al-Bukhari",64, 0);
	*/	//get connection with db
				
		/*AutomatedAnnotationModule.WriteFileHadithBooks();
		System.out.println("Books Done");
		AutomatedAnnotationModule.FileHadithChapters();
		System.out.println("Chapters Done");
		AutomatedAnnotationModule.FileHadithIndividuals();
		System.out.println("Hadith Done");
	*/}
	
	
	
public static void AnnotateHadithBooks(String collectionName, int Limit)  {
		
//		Connection conn = getConnection();
		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
		s.setDocumentManager( mgr );
		OntModel model =  ModelFactory.createOntologyModel( s, null );	
		
		model.read(readInputFilename,"RDF/XML-ABBREV" );
		
		int i =0;
		if (Limit==0)
			Limit = 8000;
		Scanner reader;
		try {
			reader = new Scanner(new File("HadithBooks.txt"));
		
		String line = reader.nextLine();
					List<HadithBook> hadithBooks = new ArrayList<HadithBook>();
				if (line!= null)
					while (line!=null && (i<Limit )) 
					{
					 	 String collection = line.split("~")[0]; //resultSet.getString("collectionName");
						 String bookID = line.split("~")[1]; //resultSet.getString("bookID");
						 String bookNameEng =line.split("~")[2]; // resultSet.getString("bookNameE");
						 String bookNameAr = line.split("~")[3]; //resultSet.getString("bookNameA");
						 
						 
						 //System.out.println(collection+"\t"+bookID+"\t"+bookNameEng+"\t"+bookNameAr);
						 if (collection.equalsIgnoreCase(collectionName))
						 {
							 hadithBooks.add(new HadithBook(bookID, bookNameEng, bookNameAr, collection));
							 i+=1;
						 }
						 if (reader.hasNextLine() == true)
							 line = reader.nextLine();
						 else
							 line = null;
					}
				
			//putting data in model
		
		String hadithBookClassUri =  HadithOnt+ "HadithBook";
		//System.out.println("hadithBookClassUri: "+hadithBookClassUri);
		OntClass c = model.getOntClass(hadithBookClassUri);
		//System.out.println("hadithBookClass"+c.getLocalName());
		String collectionUri =  HadithOnt + "Sahih-Al-Bukhari";
		Individual bukhari = model.getIndividual(collectionUri);
		//System.out.println("hadithCollectionIndividualUri: "+collectionUri);
		//System.out.println("hadithCollectionIndividual: "+bukhari.getLocalName());
		
		String objPropertyUri =  HadithOnt + "BookBelongsToCollection";
		OntProperty  bookBelongsToCollectionProperty = model.getOntProperty(objPropertyUri);		
		//System.out.println("hadithCollectionIndividualUri: "+objPropertyUri);
		//System.out.println("hadithCollectionIndividual: "+bookBelongsToCollectionProperty.getLocalName());
		
		String CollectionHasBookUri =  HadithOnt + "CollectionHasBook";
		OntProperty  CollectionHasBookProperty = model.getOntProperty(CollectionHasBookUri);		
		//System.out.println("CollectionHasBookUri: "+CollectionHasBookUri);
		//System.out.println("CollectionHasBookProperty: "+CollectionHasBookProperty.getLocalName());
		
		String dataPropertyUri =  HadithOnt + "HadithBookNumber";
		OntProperty  HadithBookNumberProperty = model.getOntProperty(dataPropertyUri);		
		//System.out.println("HadithBookNumberPropertyUri: "+dataPropertyUri);
		//System.out.println("HadithBookNumberProperty: "+HadithBookNumberProperty.getLocalName());
		
		for (HadithBook hadithBook: hadithBooks) {
		
			String bookUri =  HadithOnt+ "bukhari"+"-"+hadithBook.getBookID();
			//System.out.println(bookUri);
			
			Individual individual = model.createIndividual(bookUri, c);
			individual.addLabel( hadithBook.getBookNameEnglish(), "en" );
			individual.addLabel( hadithBook.getBookNameArabic() ,"ar" );
			individual.addProperty(bookBelongsToCollectionProperty, bukhari);
			individual.addProperty(HadithBookNumberProperty,hadithBook.getBookID());
			bukhari.addProperty(CollectionHasBookProperty, individual);
					
		}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*================================================================================
		 * 
		 * Automated Book Indviduals Population end
		 * 
		 *=================================================================================*/
		//writeModel(model);
		FileWriter out = null;
		try 
		{
			out = new FileWriter( inputfilename );
			model.write( out,"RDF/XML-ABBREV"  );
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


public static void AnnotateHadithChapters(String collectionName, int inbookID, int limit) {
	


	OntDocumentManager mgr = new OntDocumentManager();
	OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
	s.setDocumentManager( mgr );
	OntModel model =  ModelFactory.createOntologyModel( s, null );	
	model.read(inputfilename,"RDF/XML-ABBREV" );

	
	//getting data from db
	String collectionUri =  HadithOnt + "Sahih-Al-Bukhari";
	Individual bukhari = model.getIndividual(collectionUri);
	

	int i =0;
	if (limit==0)
		limit = 8000;
	Scanner reader;
	try {
		reader = new Scanner(new File("HadithChapters.txt"));
	
	String line = reader.nextLine();
	List<HadithChapter> hadithChapters = new ArrayList<HadithChapter>();
		 	if (line!= null)
   		 		while (line!=null && i<limit) 
   		 		{
   		 			//check+=1;
				 	 String collection = line.split("~")[0];//resultSet.getString("collectionName");
				 	 String chapterID = line.split("~")[1];//resultSet.getString("chapterID");
		    		 String bookID = line.split("~")[2];//resultSet.getString("bookId");
		    		 String chapterNameEng = line.split("~")[3];//resultSet.getString("chapterEng");
		    		 String chapterNameAr = line.split("~")[4];//resultSet.getString("chapterArab");
		    		 String chapterIntro = line.split("~")[5];//resultSet.getString("chapterIntro");
		    		 
		    		 if (collection.equalsIgnoreCase(collectionName) && bookID.equals(String.valueOf(inbookID)))
		    		 {
		    			 System.out.println(collection+"\t"+chapterID+"\t"+chapterNameEng+"\t"+chapterNameAr);
			    		 
			    		 hadithChapters.add(new HadithChapter(chapterID, bookID, chapterNameEng, chapterNameAr, chapterIntro));
			    		 i+=1;
		    		 }
		    		 if (reader.hasNextLine() == true)
						 line = reader.nextLine();
					 else
						 line = null;
   		 		}
	


	
	//putting data in model
	
	

	String hadithChapterClassUri =  HadithOnt+ "HadithChapter";
	//System.out.println("hadithChapterClassUri: "+hadithChapterClassUri);
	OntClass hadithChapterClass = model.getOntClass(hadithChapterClassUri);
	//System.out.println("hadithChapterClass"+hadithChapterClass.getLocalName());
	
	//System.out.println("hadithCollectionIndividualUri: "+collectionUri);
	//System.out.println("hadithCollectionIndividual: "+bukhari.getLocalName());
	
	//from collection and book
	String CollectionHasChapterUri =  HadithOnt + "CollectionHasChapter";
	OntProperty  CollectionHasChapterProperty = model.getOntProperty(CollectionHasChapterUri);		
	//System.out.println("ChapterBelongsToCollectionUri: "+CollectionHasChapterUri);
	//System.out.println("ChapterBelongsToCollectionProperty: "+CollectionHasChapterProperty.getLocalName());
	
	String BookHasChapterPropertyUri =  HadithOnt + "BookHasChapter";
	OntProperty  BookHasChapterProperty = model.getOntProperty(BookHasChapterPropertyUri);		
	//System.out.println("ChapterBelongsToBookPropertyUri: "+BookHasChapterPropertyUri);
	//System.out.println("ChapterBelongsToBookProperty: "+BookHasChapterProperty.getLocalName());
	
	//from chapter
	String ChapterBelongsToCollectionUri =  HadithOnt + "ChapterBelongsToCollection";
	OntProperty  ChapterBelongsToCollectionProperty = model.getOntProperty(ChapterBelongsToCollectionUri);		
	//System.out.println("ChapterBelongsToCollectionUri: "+ChapterBelongsToCollectionUri);
	//System.out.println("ChapterBelongsToCollectionProperty: "+ChapterBelongsToCollectionProperty.getLocalName());
	
	String ChapterBelongsToBookPropertyUri =  HadithOnt + "ChapterBelongsToBook";
	OntProperty  ChapterBelongsToBookProperty = model.getOntProperty(ChapterBelongsToBookPropertyUri);		
	//System.out.println("ChapterBelongsToBookPropertyUri: "+ChapterBelongsToBookPropertyUri);
	//System.out.println("ChapterBelongsToBookProperty: "+ChapterBelongsToBookProperty.getLocalName());
	
	String HadithChapterNumberPropertyUri =  HadithOnt + "HadithChapterNumber";
	OntProperty  HadithChapterNumberProperty = model.getOntProperty(HadithChapterNumberPropertyUri);		
	//System.out.println("HadithChapterNumberPropertyUri: "+HadithChapterNumberPropertyUri);
	//System.out.println("HadithChapterNumberProperty: "+HadithChapterNumberProperty.getLocalName());
	
	for (HadithChapter hadithChapter: hadithChapters) {
	
		
		String bookUri =  HadithOnt + "bukhari-"+hadithChapter.getBookID();
		Individual bookIndividual = model.getIndividual(bookUri);
		String chapterUri =  HadithOnt+ "bukhari"+"-"+hadithChapter.getBookID()+"-"+hadithChapter.getChapterID();
		//System.out.println(chapterUri);
		
		Individual individual = model.createIndividual(chapterUri, hadithChapterClass);
		individual.addLabel( hadithChapter.getChapterEnglish(), "en" );
		individual.addLabel( hadithChapter.getChapterArabic() ,"ar" );
		individual.addComment( hadithChapter.getChapterIntro() ,"ar" );
		individual.addProperty(ChapterBelongsToCollectionProperty, bukhari);
		individual.addProperty(ChapterBelongsToBookProperty, bookIndividual);
		bukhari.addProperty(CollectionHasChapterProperty, individual);
		bookIndividual.addProperty(BookHasChapterProperty, individual);
		individual.addProperty(HadithChapterNumberProperty,hadithChapter.getChapterID());
				
	}

	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	//writeModel(model);
	FileWriter out = null;
	try 
	{
		out = new FileWriter( inputfilename );
		model.write( out,"RDF/XML-ABBREV"  );
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


public static void AnnotateHadithIndividuals(String collectionName, int inbookID, int limit) {
	OntDocumentManager mgr = new OntDocumentManager();
	OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
	s.setDocumentManager( mgr );
	OntModel model =  ModelFactory.createOntologyModel( s, null );	
	model.read(inputfilename,"RDF/XML-ABBREV" );

	/*================================================================================
	 * 
	 * Automated Hadith Indviduals Population Start
	 * 
	 *=================================================================================*/
	
	//getting data from db
	String collectionUri =  HadithOnt + "Sahih-Al-Bukhari";
	Individual bukhari = model.getIndividual(collectionUri);
	
	int i =0;
	if (limit==0)
		limit = 8000;
	Scanner reader;
	try {
		reader = new Scanner(new File("Hadiths.txt"));
	
	String line = reader.nextLine();
	List<Hadith> hadiths = new ArrayList<Hadith>();
		 	if (line!= null)
   		 		while (line!=null && i<limit) 
   		 		{
   		 			//hadithNumber+"~"+chapterID+"~"+ bookID+"~"+ hadithEng+"~"+hadithAr+"~"+narratorEng+"~"+narratorChain
   		 			//String rawEngNarrator = line.split("~")[0];// resultSet.getString("narratorEng");
				 	 String collection = collectionName;//line.split("~")[0];//  resultSet.getString("collectionName");
		    		 String hadithNumber = line.split("~")[0];//  resultSet.getString("reference");
				 	 String chapterID =  line.split("~")[1];// resultSet.getString("eChapId");
		    		 String bookID =  line.split("~")[2];// resultSet.getString("inBookID");
		    		 String hadithEng =  line.split("~")[3];// rawEngNarrator+": "+ resultSet.getString("textEng");
		    		 
		    		 String hadithAr =  line.split("~")[4];// resultSet.getString("fullHadith");
		    		 String narratorChain = null;//  line.split("~")[6];// resultSet.getString("narratorArab");
		    		 String narratorEng = line.split("~")[5];//  null;

		    		 if (collection.equalsIgnoreCase(collectionName) && bookID.equals(String.valueOf(inbookID)))
		    		 {
		    		
		    			 hadiths.add(new Hadith(hadithNumber,chapterID, bookID, hadithEng, hadithAr, narratorEng,narratorChain));

		   		 			i+=1;
		    		 }
		    		 if (reader.hasNextLine() == true)
						 line = reader.nextLine();
					 else
						 line = null;
   		 		}
		



	
	//putting data in model
	
	

	String hadithClassUri =  HadithOnt+ "Hadith";
	//System.out.println("hadithClassUri: "+hadithClassUri);
	OntClass hadithClass = model.getOntClass(hadithClassUri);
	//System.out.println("hadithClass"+hadithClass.getLocalName());
	
	String hadithNarratorClassUri =  HadithOnt+ "HadithNarrator";
	//System.out.println("hadithNarratorClassUri: "+hadithNarratorClassUri);
	OntClass hadithNarratorClass = model.getOntClass(hadithNarratorClassUri);
	//System.out.println("hadithNarratorClass"+hadithNarratorClass.getLocalName());
	
	
	
	//System.out.println("hadithCollectionIndividualUri: "+collectionUri);
	//System.out.println("hadithCollectionIndividual: "+bukhari.getLocalName());
	
	//from collection and book and chapter
	String CollectionHasHadithUri =  HadithOnt + "CollectionHasHadith";
	OntProperty  CollectionHasHadithProperty = model.getOntProperty(CollectionHasHadithUri);		
	//System.out.println("CollectionHasHadithUri: "+CollectionHasHadithUri);
	//System.out.println("CollectionHasHadithProperty: "+CollectionHasHadithProperty.getLocalName());
	
	String BookHasHadithPropertyUri =  HadithOnt + "BookHasHadith";
	OntProperty  BookHasHadithProperty = model.getOntProperty(BookHasHadithPropertyUri);		
	//System.out.println("BookHasHadithPropertyUri: "+BookHasHadithPropertyUri);
	//System.out.println("BookHasHadithProperty: "+BookHasHadithProperty.getLocalName());
	
	String ChapterHasHadithPropertyUri =  HadithOnt + "ChapterHasHadith";
	OntProperty  ChapterHasHadithProperty = model.getOntProperty(ChapterHasHadithPropertyUri);		
	//System.out.println("BookHasHadithPropertyUri: "+ChapterHasHadithPropertyUri);
	//System.out.println("ChapterHasHadithProperty: "+ChapterHasHadithProperty.getLocalName());
	
	
	String HadithBelongsToCollectionUri =  HadithOnt + "HadithBelongsToCollection";
	OntProperty  HadithBelongsToCollectionProperty = model.getOntProperty(HadithBelongsToCollectionUri);		
	
	String HadithBelongsToBookPropertyUri =  HadithOnt + "HadithBelongsToBook";
	OntProperty  HadithBelongsToBookProperty = model.getOntProperty(HadithBelongsToBookPropertyUri);		
	
	String HadithBelongsToChapterPropertyUri =  HadithOnt + "HadithBelongsToChapter";
	OntProperty  HadithBelongsToChapterProperty = model.getOntProperty(HadithBelongsToChapterPropertyUri);		
	
	String HadithNumberPropertyUri =  HadithOnt + "HadithReferenceNumber";
	OntProperty  HadithNumberProperty = model.getOntProperty(HadithNumberPropertyUri);		
	
	
	
	for (Hadith hadith: hadiths) {
	
		
		String bookUri =  HadithOnt + "bukhari-"+hadith.getBookId();
		String chapterUri = bookUri+"-"+hadith.getChapterId();
		
		Individual bookIndividual = model.getIndividual(bookUri);
		
		Individual chapterIndividual = model.getIndividual(chapterUri);
		
		String hadithUri = chapterUri+"-"+hadith.getHadithNumber();
		Individual individual = model.createIndividual(hadithUri, hadithClass);
		individual.addLabel( hadith.getHadithEnglish(), "en" );
		individual.addLabel( hadith.getHadithArabic() ,"ar" );
	//	individual.addComment( hadith.getNarratorChain() ,"ar" );
		individual.addProperty(HadithBelongsToCollectionProperty, bukhari);
		individual.addProperty(HadithBelongsToBookProperty, bookIndividual);
		individual.addProperty(HadithBelongsToChapterProperty, chapterIndividual);
		bukhari.addProperty(CollectionHasHadithProperty, individual);
		bookIndividual.addProperty(BookHasHadithProperty, individual);
		chapterIndividual.addProperty(ChapterHasHadithProperty, individual);
		individual.addProperty(HadithNumberProperty,hadith.getHadithNumber());
	}
} catch (FileNotFoundException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}

	FileWriter out = null;
	try 
	{
		out = new FileWriter( inputfilename );
		model.write( out,"RDF/XML-ABBREV"  );
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
	


/*public static void AnnotateHadithBooks(String collectionName, int Limit) {
	
	Connection conn = getConnection();
	OntDocumentManager mgr = new OntDocumentManager();
	OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
	s.setDocumentManager( mgr );
	OntModel model =  ModelFactory.createOntologyModel( s, null );	
	
	model.read(readInputFilename,"RDF/XML-ABBREV" );
	================================================================================
	 * 
	 * Automated Book Indviduals Population Start
	 * 
	 *=================================================================================
	
	//get books data
			java.sql.ResultSet resultSet =  null;
			try 
			{
				//SELECT * FROM fyp.books where collectionName='Sahih al-Bukhari' LIMIT 10;
				Statement statement = conn.createStatement();
				String _query = "SELECT * " + "FROM  books where collectionName = '"+collectionName+"'";
				if (Limit>0)
					_query+= " LIMIT "+ String.valueOf(Limit);
				resultSet = statement.executeQuery(_query);
		             
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			List<HadithBook> hadithBooks = new ArrayList<HadithBook>();
			try {
	   		 	if (resultSet!= null)
	   		 		while (resultSet.next()) 
	   		 		{
					 	 String collection = resultSet.getString("collectionName");
			    		 String bookID = resultSet.getString("bookID");
			    		 String bookNameEng = resultSet.getString("bookNameE");
			    		 String bookNameAr = resultSet.getString("bookNameA");
			    		 
			    		 System.out.println(collection+"\t"+bookID+"\t"+bookNameEng+"\t"+bookNameAr);
			    		 hadithBooks.add(new HadithBook(bookID, bookNameEng, bookNameAr, collection));
	   		 		}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		//putting data in model
	
	String hadithBookClassUri =  HadithOnt+ "HadithBook";
	//System.out.println("hadithBookClassUri: "+hadithBookClassUri);
	OntClass c = model.getOntClass(hadithBookClassUri);
	//System.out.println("hadithBookClass"+c.getLocalName());
	String collectionUri =  HadithOnt + "Sahih-Al-Bukhari";
	Individual bukhari = model.getIndividual(collectionUri);
	//System.out.println("hadithCollectionIndividualUri: "+collectionUri);
	//System.out.println("hadithCollectionIndividual: "+bukhari.getLocalName());
	
	String objPropertyUri =  HadithOnt + "BookBelongsToCollection";
	OntProperty  bookBelongsToCollectionProperty = model.getOntProperty(objPropertyUri);		
	//System.out.println("hadithCollectionIndividualUri: "+objPropertyUri);
	//System.out.println("hadithCollectionIndividual: "+bookBelongsToCollectionProperty.getLocalName());
	
	String CollectionHasBookUri =  HadithOnt + "CollectionHasBook";
	OntProperty  CollectionHasBookProperty = model.getOntProperty(CollectionHasBookUri);		
	//System.out.println("CollectionHasBookUri: "+CollectionHasBookUri);
	//System.out.println("CollectionHasBookProperty: "+CollectionHasBookProperty.getLocalName());
	
	String dataPropertyUri =  HadithOnt + "HadithBookNumber";
	OntProperty  HadithBookNumberProperty = model.getOntProperty(dataPropertyUri);		
	//System.out.println("HadithBookNumberPropertyUri: "+dataPropertyUri);
	//System.out.println("HadithBookNumberProperty: "+HadithBookNumberProperty.getLocalName());
	
	for (HadithBook hadithBook: hadithBooks) {
	
		String bookUri =  HadithOnt+ "bukhari"+"-"+hadithBook.getBookID();
		//System.out.println(bookUri);
		
		Individual individual = model.createIndividual(bookUri, c);
		individual.addLabel( hadithBook.getBookNameEnglish(), "en" );
		individual.addLabel( hadithBook.getBookNameArabic() ,"ar" );
		individual.addProperty(bookBelongsToCollectionProperty, bukhari);
		individual.addProperty(HadithBookNumberProperty,hadithBook.getBookID());
		bukhari.addProperty(CollectionHasBookProperty, individual);
				
	}
	
	================================================================================
	 * 
	 * Automated Book Indviduals Population end
	 * 
	 *=================================================================================
	//writeModel(model);
	FileWriter out = null;
	try 
	{
		out = new FileWriter( inputfilename );
		model.write( out,"RDF/XML-ABBREV"  );
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
*/


/*	
public static void AnnotateHadithChapters(String collectionName, int inbookID, int limit) {
		
	
	
		Connection conn = getConnection();
		
		
		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
		s.setDocumentManager( mgr );
		OntModel model =  ModelFactory.createOntologyModel( s, null );	
		model.read(inputfilename,"RDF/XML-ABBREV" );

		================================================================================
		 * 
		 * Automated Chapter Indviduals Population Start
		 * 
		 *=================================================================================
		
		//getting data from db
		String collectionUri =  HadithOnt + "Sahih-Al-Bukhari";
		Individual bukhari = model.getIndividual(collectionUri);
		
		
		java.sql.ResultSet resultSet =  null;
			try 
			{
				Statement statement = conn.createStatement();
				String _query = "SELECT * " + "FROM  chapter  where BookID='"+String.valueOf(inbookID)+"' and collectionName = '"+collectionName+"'";
				if (limit >0)
					_query+= " LIMIT "+ String.valueOf(limit);
				resultSet = statement.executeQuery(_query);
		             
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//int check = 0;
			List<HadithChapter> hadithChapters = new ArrayList<HadithChapter>();
			try {
	   		 	if (resultSet!= null)
	   		 		while (resultSet.next() && check<1000) 
	   		 		{
	   		 			//check+=1;
					 	 String collection = resultSet.getString("collectionName");
					 	 String chapterID = resultSet.getString("chapterID");
			    		 String bookID = resultSet.getString("bookId");
			    		 String chapterNameEng = resultSet.getString("chapterEng");
			    		 String chapterNameAr = resultSet.getString("chapterArab");
			    		 String chapterIntro = resultSet.getString("chapterIntro");
			    		 
			    		 System.out.println(collection+"\t"+chapterID+"\t"+chapterNameEng+"\t"+chapterNameAr);
			    		 hadithChapters.add(new HadithChapter(chapterID, bookID, chapterNameEng, chapterNameAr, chapterIntro));
	   		 		}
			} catch (SQLException e) {
				e.printStackTrace();
			}



		
		//putting data in model
		
		

		String hadithChapterClassUri =  HadithOnt+ "HadithChapter";
		//System.out.println("hadithChapterClassUri: "+hadithChapterClassUri);
		OntClass hadithChapterClass = model.getOntClass(hadithChapterClassUri);
		//System.out.println("hadithChapterClass"+hadithChapterClass.getLocalName());
		
		//System.out.println("hadithCollectionIndividualUri: "+collectionUri);
		//System.out.println("hadithCollectionIndividual: "+bukhari.getLocalName());
		
		//from collection and book
		String CollectionHasChapterUri =  HadithOnt + "CollectionHasChapter";
		OntProperty  CollectionHasChapterProperty = model.getOntProperty(CollectionHasChapterUri);		
		//System.out.println("ChapterBelongsToCollectionUri: "+CollectionHasChapterUri);
		//System.out.println("ChapterBelongsToCollectionProperty: "+CollectionHasChapterProperty.getLocalName());
		
		String BookHasChapterPropertyUri =  HadithOnt + "BookHasChapter";
		OntProperty  BookHasChapterProperty = model.getOntProperty(BookHasChapterPropertyUri);		
		//System.out.println("ChapterBelongsToBookPropertyUri: "+BookHasChapterPropertyUri);
		//System.out.println("ChapterBelongsToBookProperty: "+BookHasChapterProperty.getLocalName());
		
		//from chapter
		String ChapterBelongsToCollectionUri =  HadithOnt + "ChapterBelongsToCollection";
		OntProperty  ChapterBelongsToCollectionProperty = model.getOntProperty(ChapterBelongsToCollectionUri);		
		//System.out.println("ChapterBelongsToCollectionUri: "+ChapterBelongsToCollectionUri);
		//System.out.println("ChapterBelongsToCollectionProperty: "+ChapterBelongsToCollectionProperty.getLocalName());
		
		String ChapterBelongsToBookPropertyUri =  HadithOnt + "ChapterBelongsToBook";
		OntProperty  ChapterBelongsToBookProperty = model.getOntProperty(ChapterBelongsToBookPropertyUri);		
		//System.out.println("ChapterBelongsToBookPropertyUri: "+ChapterBelongsToBookPropertyUri);
		//System.out.println("ChapterBelongsToBookProperty: "+ChapterBelongsToBookProperty.getLocalName());
		
		String HadithChapterNumberPropertyUri =  HadithOnt + "HadithChapterNumber";
		OntProperty  HadithChapterNumberProperty = model.getOntProperty(HadithChapterNumberPropertyUri);		
		//System.out.println("HadithChapterNumberPropertyUri: "+HadithChapterNumberPropertyUri);
		//System.out.println("HadithChapterNumberProperty: "+HadithChapterNumberProperty.getLocalName());
		
		for (HadithChapter hadithChapter: hadithChapters) {
		
			
			String bookUri =  HadithOnt + "bukhari-"+hadithChapter.getBookID();
			Individual bookIndividual = model.getIndividual(bookUri);
			String chapterUri =  HadithOnt+ "bukhari"+"-"+hadithChapter.getBookID()+"-"+hadithChapter.getChapterID();
			//System.out.println(chapterUri);
			
			Individual individual = model.createIndividual(chapterUri, hadithChapterClass);
			individual.addLabel( hadithChapter.getChapterEnglish(), "en" );
			individual.addLabel( hadithChapter.getChapterArabic() ,"ar" );
			individual.addComment( hadithChapter.getChapterIntro() ,"ar" );
			individual.addProperty(ChapterBelongsToCollectionProperty, bukhari);
			individual.addProperty(ChapterBelongsToBookProperty, bookIndividual);
			bukhari.addProperty(CollectionHasChapterProperty, individual);
			bookIndividual.addProperty(BookHasChapterProperty, individual);
			individual.addProperty(HadithChapterNumberProperty,hadithChapter.getChapterID());
					
		}
		
		================================================================================
		 * 
		 * Automated Chapter Indviduals Population end
		 * 
		 *=================================================================================
		
		//writeModel(model);
		FileWriter out = null;
		try 
		{
			out = new FileWriter( inputfilename );
			model.write( out,"RDF/XML-ABBREV"  );
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
*/
/*public static void AnnotateHadithIndividuals(String collectionName, int inbookID, int limit) {
	
	Connection conn = getConnection();
	
	
	OntDocumentManager mgr = new OntDocumentManager();
	OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
	s.setDocumentManager( mgr );
	OntModel model =  ModelFactory.createOntologyModel( s, null );	
	model.read(inputfilename,"RDF/XML-ABBREV" );

	================================================================================
	 * 
	 * Automated Hadith Indviduals Population Start
	 * 
	 *=================================================================================
	
	//getting data from db
	String collectionUri =  HadithOnt + "Sahih-Al-Bukhari";
	Individual bukhari = model.getIndividual(collectionUri);
	
	
	java.sql.ResultSet resultSet =  null;
		try 
		{
			Statement statement = conn.createStatement();
			String _query = "SELECT * " + "FROM  hadith2  where inBookID='"+String.valueOf(inbookID)+"' and collectionName = '"+collectionName+"'";
			if (limit >0)
				_query+= " LIMIT "+ String.valueOf(limit);
			//String _query = "SELECT * " + "FROM  hadith2 where inBookID='64'";
			resultSet = statement.executeQuery(_query);
	             
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int i = 0;
		List<Hadith> hadiths = new ArrayList<Hadith>();
		try {
   		 	if (resultSet!= null)
   		 		while (resultSet.next() && i<600) 
   		 		{
   		 			i+=1;
   		 			String rawEngNarrator = resultSet.getString("narratorEng");
				 	 String collection = resultSet.getString("collectionName");
		    		 String hadithNumber = resultSet.getString("reference");
				 	 String chapterID = resultSet.getString("eChapId");
		    		 String bookID = resultSet.getString("inBookID");
		    		 String hadithEng = rawEngNarrator+": "+ resultSet.getString("textEng");
		    		 
		    		 String hadithAr = resultSet.getString("fullHadith");
		    		 String narratorChain = resultSet.getString("narratorArab");
		    		 String narratorEng = null;
		    		 //ystem.out.println(rawEngNarrator);
		    		 if (rawEngNarrator.equals("null")==false &&  rawEngNarrator.equals("")==false && rawEngNarrator!=null )
		    		 {	 narratorEng = rawEngNarrator.split(":")[0].split("said")[0].split("that")[0];
		    		 	if (narratorEng.length()>9) {
		    		 	//System.out.println(narratorEng.substring(0, 8)+"\t"+narratorEng.substring(9));
		    		 	if(narratorEng.substring(0, 8).equals("Narrated "))
		    		 		narratorEng = narratorEng.substring(9);}
		    		 }
		    				 
		    		 //System.out.println(collection+"\t"+hadithNumber+"\t"+narratorEng+"\t"+hadithEng+"\t"+hadithAr);
		    		 hadiths.add(new Hadith(hadithNumber,chapterID, bookID, hadithEng, hadithAr, narratorEng,narratorChain));
   		 		}
		} catch (SQLException e) {
			e.printStackTrace();
		}



	
	//putting data in model
	
	

	String hadithClassUri =  HadithOnt+ "Hadith";
	//System.out.println("hadithClassUri: "+hadithClassUri);
	OntClass hadithClass = model.getOntClass(hadithClassUri);
	//System.out.println("hadithClass"+hadithClass.getLocalName());
	
	String hadithNarratorClassUri =  HadithOnt+ "HadithNarrator";
	//System.out.println("hadithNarratorClassUri: "+hadithNarratorClassUri);
	OntClass hadithNarratorClass = model.getOntClass(hadithNarratorClassUri);
	//System.out.println("hadithNarratorClass"+hadithNarratorClass.getLocalName());
	
	
	
	//System.out.println("hadithCollectionIndividualUri: "+collectionUri);
	//System.out.println("hadithCollectionIndividual: "+bukhari.getLocalName());
	
	//from collection and book and chapter
	String CollectionHasHadithUri =  HadithOnt + "CollectionHasHadith";
	OntProperty  CollectionHasHadithProperty = model.getOntProperty(CollectionHasHadithUri);		
	//System.out.println("CollectionHasHadithUri: "+CollectionHasHadithUri);
	//System.out.println("CollectionHasHadithProperty: "+CollectionHasHadithProperty.getLocalName());
	
	String BookHasHadithPropertyUri =  HadithOnt + "BookHasHadith";
	OntProperty  BookHasHadithProperty = model.getOntProperty(BookHasHadithPropertyUri);		
	//System.out.println("BookHasHadithPropertyUri: "+BookHasHadithPropertyUri);
	//System.out.println("BookHasHadithProperty: "+BookHasHadithProperty.getLocalName());
	
	String ChapterHasHadithPropertyUri =  HadithOnt + "ChapterHasHadith";
	OntProperty  ChapterHasHadithProperty = model.getOntProperty(ChapterHasHadithPropertyUri);		
	//System.out.println("BookHasHadithPropertyUri: "+ChapterHasHadithPropertyUri);
	//System.out.println("ChapterHasHadithProperty: "+ChapterHasHadithProperty.getLocalName());
	
	
	String HadithBelongsToCollectionUri =  HadithOnt + "HadithBelongsToCollection";
	OntProperty  HadithBelongsToCollectionProperty = model.getOntProperty(HadithBelongsToCollectionUri);		
	
	String HadithBelongsToBookPropertyUri =  HadithOnt + "HadithBelongsToBook";
	OntProperty  HadithBelongsToBookProperty = model.getOntProperty(HadithBelongsToBookPropertyUri);		
	
	String HadithBelongsToChapterPropertyUri =  HadithOnt + "HadithBelongsToChapter";
	OntProperty  HadithBelongsToChapterProperty = model.getOntProperty(HadithBelongsToChapterPropertyUri);		
	
	String HadithNumberPropertyUri =  HadithOnt + "HadithReferenceNumber";
	OntProperty  HadithNumberProperty = model.getOntProperty(HadithNumberPropertyUri);		
	
	
	
	for (Hadith hadith: hadiths) {
	
		
		String bookUri =  HadithOnt + "bukhari-"+hadith.getBookId();
		String chapterUri = bookUri+"-"+hadith.getChapterId();
		
		Individual bookIndividual = model.getIndividual(bookUri);
		
		Individual chapterIndividual = model.getIndividual(chapterUri);
		
		String hadithUri = chapterUri+"-"+hadith.getHadithNumber();
		Individual individual = model.createIndividual(hadithUri, hadithClass);
		individual.addLabel( hadith.getHadithEnglish(), "en" );
		individual.addLabel( hadith.getHadithArabic() ,"ar" );
		individual.addComment( hadith.getNarratorChain() ,"ar" );
		individual.addProperty(HadithBelongsToCollectionProperty, bukhari);
		individual.addProperty(HadithBelongsToBookProperty, bookIndividual);
		individual.addProperty(HadithBelongsToChapterProperty, chapterIndividual);
		bukhari.addProperty(CollectionHasHadithProperty, individual);
		bookIndividual.addProperty(BookHasHadithProperty, individual);
		chapterIndividual.addProperty(ChapterHasHadithProperty, individual);
		individual.addProperty(HadithNumberProperty,hadith.getHadithNumber());
	}
	FileWriter out = null;
	try 
	{
		out = new FileWriter( inputfilename );
		model.write( out,"RDF/XML-ABBREV"  );
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
	*/
}	
		
		
		
		

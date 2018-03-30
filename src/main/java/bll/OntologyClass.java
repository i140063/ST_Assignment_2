package bll;

import java.util.ArrayList;
import java.util.List;

public class OntologyClass {

	String className;
	int indvidualCount;
	List<OntologyClass> subClasses;
	
/*	public OntologyClass() {
		super();
	}
*/	
	public OntologyClass(String className, int indvidualCount) {
		super();
		this.className = className;
		this.indvidualCount = indvidualCount;
		subClasses =new ArrayList<OntologyClass>();
	}
	public void setSubClasses(List<OntologyClass> subClasses) {
		this.subClasses = subClasses;
	}

/*	public OntologyClass(String className, int indvidualCount, List<OntologyClass> subClass) {
		super();
		this.className = className;
		this.indvidualCount = indvidualCount;
		this.subClasses = subClass;
	}
	
	
	public List<OntologyClass> getSubClasses() {
		return subClasses;
	}


	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getIndvidualCount() {
		return indvidualCount;
	}
	public void setIndvidualCount(int indvidualCount) {
		this.indvidualCount = indvidualCount;
	}
	
	public void addSubClass(OntologyClass subClass) {
		this.subClasses.add(subClass);
	}
*/	
	
	
}

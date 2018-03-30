package bll;

import java.util.List;

import org.apache.jena.query.ResultSet;

public class CustomResult {

	boolean executionStatus;
	List<List<String>>  resultSet;
	
	public CustomResult() {}
	/*
	public CustomResult(boolean executionStatus, List<List<String>>  resultSet) {
		
		this.executionStatus = executionStatus;
		this.resultSet = resultSet;
	}*/
	
	public boolean isExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(boolean executionStatus) {
		this.executionStatus = executionStatus;
	}
	public List<List<String>>  getResultSet() {
		return resultSet;
	} 
	public void setResultSet(List<List<String>>  resultSet) {
		this.resultSet = resultSet;
	}
}

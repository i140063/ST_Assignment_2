

 /** To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bll.CustomResult;
import bll.SemanticHadithController;

@WebServlet(name = "SelectServlet", urlPatterns = {"/SelectServlet"})
public class SelectServlet extends HttpServlet {
	
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            {
    	try {
	        response.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        String FormName = request.getParameter("FormName");
	        String output = "";
	        try {
	            switch (FormName) {
	            	/*case "QueryForm":
	            		String query = request.getParameter("query");
	            		SemanticHadithController Controller = SemanticHadithController.Instance();
	            		String []arr=  {"x", "y"};
	            		CustomResult result = Controller.SparqlQuery(query,arr);
	            		
	            		if (result.isExecutionStatus()==true) 
	            		{
	            			request.setAttribute("propertyList", result.getResultSet());
	            			RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/QuerySuccessful.jsp");
	            			requestDispatcher.forward(request,response);
	            			//response.sendRedirect("QuerySuccessful.jsp");
	            			
	            		}
	            		else
	            			response.sendRedirect("welcomePage.jsp");
	            			request.getSession().setAttribute("Message", null);
	            		
	            		break;*/
	            	case "companionInstance":
	            		String classUri = request.getParameter("ontoUriClass")+request.getParameter("class");
	            		String Instanceuri = request.getParameter("uri");
	            		String label = request.getParameter("label");
	            		String comment = request.getParameter("comment");
	            		
	            		SemanticHadithController Controller2 = SemanticHadithController.Instance();
	            		Controller2.SparqlIndvidualInsert(classUri, Instanceuri, label, comment);
	            		response.sendRedirect("InstanceInsertion.jsp");
	            		
	            		break;
	            	case "propertyLink":
	            		String domainUri = request.getParameter("ontoUriDomain")+request.getParameter("domain");
	            		String propertyUri =  request.getParameter("ontoUriProperty")+request.getParameter("property");
	            		String rangeUri =  request.getParameter("ontoUriRange")+request.getParameter("range");
	            		SemanticHadithController Controller3 = SemanticHadithController.Instance();
	            		Controller3.SparqlIndvidualRelations(domainUri, propertyUri, rangeUri);
	            		response.sendRedirect("PropertyLinks.jsp");
	            		
	            		break;
	            		
	            	default:
	            		System.out.println("Problem with form");
	            		request.getParameter("WrongForm");
	                    break;
	            }
	            out.print(output);
	            out.flush();
	        } finally {
	            out.close();
	        }
        }
    	catch(Exception ex) {
    		try {
				
    			request.getHeader("Exception");
				response.sendRedirect("GenericError.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
	        
    		
    	}
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

package kdy.config;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kdy.engine.SearchEngine;
import kdy.engine.document.ScoreDoc;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = super.getServletContext();
		SearchEngine engine = (SearchEngine)context.getAttribute("engine");
		
		String query = request.getParameter("query");
		query = URLDecoder.decode(query, "UTF-8");
		
		System.out.println("query : " + query);
		StringBuffer buffer = new StringBuffer();
		ScoreDoc[] results = engine.search(query);
		
		int to = 0;
		if( results != null ) {
			to = Math.min(100, results.length);
		}
		
		for(int i = 0 ; i<to ; i++ ) {
			ScoreDoc doc = results[i];
			buffer.append(String.format("<li class=\"list\"><a href=\"#\" class=\"listItem\" data-score='%f'>%d</a></li>", 
					doc.getScore(),
					(int)doc.getData()));
		}
		
		response.setContentType("html/text");
		response.getWriter().write(buffer.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

package kdy.config;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXException;

import kdy.core.BookInfo;
import kdy.core.DataHandler;

/**
 * Servlet implementation class DocumentReadServlet
 */
@WebServlet("/DocumentReadServlet")
public class DocumentReadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentReadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String docId = (String)request.getParameter("document");
		
		docId = String.format("%05d", Integer.parseInt(docId)); 
		
		System.out.println("Doc Id : " + docId );
		
		ServletContext context = getServletContext();
		String path = context.getRealPath("/WEB-INF/data");
		File xml = new File(path + "/" + docId + ".xml");
		if( xml.exists() ) {
			SAXParser parser = (SAXParser)context.getAttribute("parser");
			DataHandler handler = (DataHandler)context.getAttribute("handler");
			
			try {
				parser.parse(xml, handler);
				
				BookInfo bookInfo = handler.getBookInfo();
				StringBuffer buffer = new StringBuffer();
				
				buffer.append("{");
					buffer.append("\"id\" : \"" + bookInfo.getBookId() + "\",");
					buffer.append("\"title\" : \"" + URLEncoder.encode(bookInfo.getTitle(), "UTF-8") + "\",");
					buffer.append("\"see\" : \"" + URLEncoder.encode(bookInfo.getSee(), "UTF-8") + "\",");
					buffer.append("\"seealso\" : \"" + URLEncoder.encode(bookInfo.getSeeAlso(), "UTF-8") + "\",");
					buffer.append("\"contents\" : \"" + URLEncoder.encode(bookInfo.getContents(), "UTF-8") + "\",");
					buffer.append("\"subs\" : \"" + URLEncoder.encode(bookInfo.getSubs(), "UTF-8") + "\"");
				buffer.append("}");
				
				System.out.println("Document : " + buffer.toString());
				
				response.setContentType("application/json");
				response.getWriter().write(buffer.toString());
				
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

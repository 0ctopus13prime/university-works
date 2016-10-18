package kdy.config;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kdy.core.DataHandler;
import kdy.engine.SearchEngine;
import kdy.engine.document.Document;

import org.xml.sax.SAXException;

/**
 * 인덱싱을 시작한다.
 *
 */
@WebListener
public class InitListener implements ServletContextListener {

    public InitListener() {
    }

	@Override
	public void contextDestroyed(ServletContextEvent ignore) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		//서치 엔진
		//서치 엔진에서 로드함
		//문서 리더
		
		/*
		 * 총 2개의 url
		 * 
		 * 검색 : 검색하면 JSON으로 문서 아이디와 유사도를 보내줌
		 * 문서 : 문서 아이디를 보내면 읽어서 JSON으로 제목 등을 보내줌
		 */
		
		try {
			SearchEngine searchEngine = new SearchEngine();
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			DataHandler handler = new DataHandler();
			
			ServletContext context = event.getServletContext();
			String dataPath = context.getRealPath("/WEB-INF/data");
			File dir = new File(dataPath);
			File[] list = dir.listFiles();
			
			int count = 0;
			
			for(File xml : list ) {
				if( xml.getAbsolutePath().endsWith("xml") ) {
					if( count++ % 200 == 0 ) {
						System.out.println("processing ... " + count);
					}
					parser.parse(xml, handler);
					
					//파싱해서 책 아이디를 데이터로, 
					//책 내용을 색인함
					Document doc = new Document();
					doc.setData(handler.getBookInfo().getBookId());
					doc.setText(handler.getBookInfo().getContents());
					
					//책 정보 색인
					searchEngine.writeData(doc);
				}
			}
			
			context.setAttribute("engine", searchEngine);
			context.setAttribute("parser", parser);
			context.setAttribute("handler", handler);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package kdy.core;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import kdy.engine.SearchEngine;
import kdy.engine.document.Document;
import kdy.engine.document.ScoreDoc;
import kdy.parser.BigramParser;
import kdy.parser.Term;
import kdy.parser.TermVector;

public class Main {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		String filePath = "C:\\Users\\13prime\\Dropbox\\보완\\학교공부\\2015-1\\파일처리\\과제\\검색엔진\\dataset";
		File dir = new File(filePath);
		File[] flist = dir.listFiles();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		
		DataHandler handler = new DataHandler();
		
		SearchEngine se = new SearchEngine();
		int count = 0;
		for(File f : flist) {
			if( f.getAbsolutePath().endsWith("xml") ) {
				if( ++count%200 == 0 ) {
					System.out.println(count);
				}
				parser.parse(f, handler);
				BookInfo bi = handler.getBookInfo();
				
				Document doc = new Document();
				doc.setData(bi.getBookId());
				doc.setText(bi.getContents());
				se.writeData(doc);
			}
		}
		
		long start = System.currentTimeMillis();
		
		ScoreDoc[] results = se.search("광개토대왕때 고구려의 영토는?");
		
		long end = System.currentTimeMillis();
		
		System.out.println("total : " + (end - start));
		
		for(int i = 0 ; i<10 ; i++ ) {
			ScoreDoc sd = results[i];
			System.out.println("Score : " + sd.getScore());
			
			BookInfo bi = (BookInfo)sd.getData();
			System.out.println("Id : " + bi.getBookId());
		}
		
		
	}
}
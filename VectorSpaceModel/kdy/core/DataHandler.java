package kdy.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DataHandler extends DefaultHandler {
	private String key;
	private BookInfo bookInfo;
	
	public DataHandler() {
	}
	
	@Override
	public void startDocument() {
		bookInfo = new BookInfo();
		key = null;
	}
	
	@Override
	public void startElement(String uri,
            String localName,
            String qName,
            Attributes attributes)
              throws SAXException {
		key = qName;
	}
	
	@Override
	public void	characters(char[] ch, int start, int length) {
		String str = String.valueOf(ch, start, length);
		if( !str.trim().isEmpty() ) {
			switch(key) {
			case "id" : 
				bookInfo.setBookId(Integer.parseInt(str));
				break;
			case "title" :
				bookInfo.setTitle(str); break;
			case "contents" : 
				bookInfo.setContents(str); break;
			case "seealso" : 
				bookInfo.setSeeAlso(str); break;
			case "see" : 
				bookInfo.setSee(str); break;
			case "subs" :
				bookInfo.setSubs(str); break;
			}
		}
	}
	
	public BookInfo getBookInfo() {
		return bookInfo;
	}
}

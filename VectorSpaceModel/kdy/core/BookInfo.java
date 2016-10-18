package kdy.core;

public class BookInfo {
	private int bookId;
	private String title;
	private String contents;
	private String seeAlso;
	private String see;
	private String subs;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents == null ? "" : contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getSeeAlso() {
		return seeAlso == null ? "" : seeAlso;
	}
	public void setSeeAlso(String seeAlso) {
		this.seeAlso = seeAlso;
	}
	public String getSee() {
		return see == null ? "" : see;
	}
	public void setSee(String see) {
		this.see = see;
	}
	public String getSubs() {
		return subs == null ? "" : subs;
	}
	public void setSubs(String subs) {
		this.subs = subs;
	}
}

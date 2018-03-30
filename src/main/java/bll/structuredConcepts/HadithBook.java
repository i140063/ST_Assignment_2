/**
 * 
 */
package bll.structuredConcepts;

/**
 * @author THELAPTOP-HUT
 *
 */
public class HadithBook {

	String bookID, bookNameEnglish, bookNameArabic, collectionName;
	
	

	public HadithBook(String bookID, String bookNameEnglish, String bookNameArabic, String collectionName) {
		super();
		this.bookID = bookID;
		this.bookNameEnglish = bookNameEnglish;
		this.bookNameArabic = bookNameArabic;
		this.collectionName = collectionName;
	}

	public String getBookID() {
		return bookID;
	}


	public String getBookNameEnglish() {
		return bookNameEnglish;
	}

	public String getBookNameArabic() {
		return bookNameArabic;
	}

}

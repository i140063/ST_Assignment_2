package bll.structuredConcepts;

public class HadithChapter {

	String chapterID, bookID, chapterEnglish, chapterArabic, chapterIntro;
	
	

	public HadithChapter(String chapterID, String bookID, String chapterEnglish, String chapterArabic,
			String chapterIntro) {
		super();
		this.chapterID = chapterID;
		this.bookID = bookID;
		this.chapterEnglish = chapterEnglish;
		this.chapterArabic = chapterArabic;
		this.chapterIntro = chapterIntro;
	}

	public String getChapterID() {
		return chapterID;
	}

	public String getBookID() {
		return bookID;
	}

	public String getChapterEnglish() {
		return chapterEnglish;
	}


	public String getChapterArabic() {
		return chapterArabic;
	}

	public String getChapterIntro() {
		return chapterIntro;
	}

}

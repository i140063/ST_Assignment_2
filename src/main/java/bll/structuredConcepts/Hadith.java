package bll.structuredConcepts;

public class Hadith {

	String hadithNumber, chapterId, bookId, hadithEnglish, hadithArabic, narratorEnglish, narratorChain;

	
	public Hadith(String hadithNumber, String chapterId, String bookId, String hadithEnglish, String hadithArabic,
			String narratorEnglish, String narratorChain) {
		super();
		this.hadithNumber = hadithNumber;
		this.chapterId = chapterId;
		this.bookId = bookId;
		this.hadithEnglish = hadithEnglish;
		this.hadithArabic = hadithArabic;
		this.narratorEnglish = narratorEnglish;
		this.narratorChain = narratorChain;
	}

	public String getHadithNumber() {
		return hadithNumber;
	}


	public String getChapterId() {
		return chapterId;
	}


	public String getBookId() {
		return bookId;
	}


	public String getHadithEnglish() {
		return hadithEnglish;
	}

	public String getHadithArabic() {
		return hadithArabic;
	}

	public String getNarratorChain() {
		return narratorChain;
	}

	
}

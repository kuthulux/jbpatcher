package com.mobileread.ixtab.patch.fontsize;

public class FWAdapter531 extends FWAdapter {

	public String[] getSupportedLocales() {
		return new String[]{"en","de","es","fr","it","pt"};
	}

	public String[] getClasses() {
		return new String[] {
				"com.amazon.ebook.booklet.reader.resources.ReaderResources",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_de",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_en_GB",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_es",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_fr",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_it",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_ja",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_pt",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_zh",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_de",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_en_GB",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_es",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_fr",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_it",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_ja",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_pt",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_zh",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_de",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_en_GB",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_es",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_fr",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_it",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_ja",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_pt",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_zh",
			};
	}

	public String[] getMd5Before() {
		return new String[] {
				// ReaderResources
				"352802e537d0456b20ecb0f69f43ef5c", // default
				"696eb2fcf01a45f9aaf5ee45a583a23e", // de
				"e829a742ec11be5000dc39b43b2ed406", // en_GB
				"4a9a0823691ed3619e6f1feb97b3a441", // es
				"e5122e6f2d83888f6900b7eb07a53ed7", // fr
				"63b57539515f3e7617dd0134bc77a603", // it
				"a3e9aab4545440840d87377db25f18dc", // ja
				"4acbf0de4e67d847ee93e2b36f986db6", // pt
				"31e597113fef4ab7e6618d25ef4e327c", // zh
				// MobiReaderImplResources
				"5c3ba5125d1a518b41fa451d12a7a89b", // default
				"b038a45ed53eb922c2ed632e38a8af31", // de
				"8147db6b35edf3c9f39d88b4807d55ba", // en_GB
				"49db788f811482ee0d69fba8dfea9c8a", // es
				"e921897ff473860bd44cfc85f5a7ebe6", // fr
				"8f9c44aa1ef9fcdaaad6a3452b1bd98c", // it
				"6f345189945ae06816366269d8e7667e", // ja
				"7a8d3c9daef090a69872d18603bf0499", // pt
				"27831df378209e9a607f8ab488c859b1", // zh
				// ReaderUtilsResources
				"14bf04bf69cd3578137260c6ed0bb82c", // default
				"2aaf63f1146fbb6e9ba4fcf43e33c604", // de
				"5fab503396325510125e4ee8fef3f1f4", // en_GB
				"26a476936799cf8904eef799f8923256", // es
				"98784df9ee3daf004af782ad6103ac23", // fr
				"c8a61ed13a2062c9ce2d5ab294151fcd", // it
				"2b975eb96b27b00e544c994a6c75f426", // ja
				"c170c59ad11dac2910c75cf68442fba7", // pt
				"0b61b88f8c8f55f645394b2c112c8c1e", // zh
			};
	}

	public String[] getMd5After() {
		return new String[] {
				//FIXME: these ALL need to be fixed. :-(
				// ReaderResources
				"2f5a0f6b2f19d106668f43077c299896", // default
				"99d68a99ba1f1ea23464733c7eaab72e", // de
				"9be537a37c85b17b213fd88fe07dfc0f", // en_GB
				"d8d3a64a658dc4fb8e2672fb9cc13be0", // es
				"4c22b5e6eca6aaeac60806791c9b2878", // fr
				"c0325b9aa80291bdf086f50057f5d310", // it
				"ce6023425cd4c4e3349a8e1c2a4c3402", // ja
				"fac2601d1f9f7ec2842c680851f3c001", // pt
				"280947e2847baab967adf2c97ea63a29", // zh
				// MobiReaderImplResources
				"4063925ced7f06b9acac354a5c963904", // default
				"4565348793fae31abad970982d3927f8", // de
				"c736c9e38ddf12c1a68f72486339a1de", // en_GB
				"a4d3f5e006b1cfd59389ce7e846d5dc2", // es
				"128e7ea90550a321d878c7e141d2ed0f", // fr
				"6eab2da59d0e9d9e9136c48b365b7699", // it
				"e5aa3953f82a05ad2ea9cae24ee0dc1d", // ja
				"bbd809510eb2dd7c82227141a5917227", // pt
				"52a38718c9864a63391011d39fde55b3", // zh
				// ReaderUtilsResources
				"fd416712d8e8c22466b88c3edb6683c8", // default
				"e24405702945400af764d36552057b35", // de
				"cc31c53522c77a9dd33d56223ada9cce", // en_GB
				"20db1480b33df1ced357b5df0b5dbdc6", // es
				"a80efc70150f7125900b69b356644b21", // fr
				"79069c1a42abb40da50fe2e3c00e7b5c", // it
				"13ff507056401d3d2d08363aad4b5849", // ja
				"4d8ecbde77c4c94b77bc4f797eaf6e33", // pt
				"59b44dc027a72a35d39661b410067c2f", // zh
			};
	}

	public String getFieldName() {
		return "H";
	}

}

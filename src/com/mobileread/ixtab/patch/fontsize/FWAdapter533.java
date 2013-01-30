package com.mobileread.ixtab.patch.fontsize;


public class FWAdapter533 extends FWAdapter {

	public String[] getSupportedLocales() {
		return new String[]{"de","en_GB","es","fr","it","ja","pt","zh"};
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
				"dcfdbfd24f3222f32bb6ba88fad728ec", // default
				"d23f48ccd8283862d8cb16827145c222", // de
				"4391576346cbfa563d7be87f622928c0", // en_GB
				"5beb54de895a5a0147904875021ba45b", // es
				"a45613a1e0dace1fca4cb909205e5783", // fr
				"5ce60c3a76a9a662725cf042c7a2d623", // it
				"3773b89b627bf78a47a49eb57e7e0d90", // ja
				"8feb6f09ad12aec6764a467426cd4b81", // pt
				"2c23eaf4454a07ac50b7f9e4554a0f7a", // zh
				// MobiReaderImplResources
				"81a66bd699240845016459289951ac97", // default
				"a0b35088cda25f624669a5a582de33ee", // de
				"d6eb2363239c2d9dd859614b6f38cb29", // en_GB
				"d35fc44e601440e4c7bb6d4d56486830", // es
				"c0c39829dcc06a6367b403fc8a219ce3", // fr
				"d44202f9261b35fed832e8deef65ea25", // it
				"16db2ee514fa850ff69773b73936e0ad", // ja
				"04a5ae1eaa2a0d2e5495ffff7d3d6ce7", // pt
				"9ec8588957f1ddfe6cc8671a6191740e", // zh
				// ReaderUtilsResources
				"b256697f90a08dba101e8b187488a986", // default
				"734462c673f4f84fee69fef59ab4d75e", // de
				"9a0f003fee8a91b6807152b220fd437c", // en_GB
				"c23ab918ce73ff0111ad62480ecfa155", // es
				"a21f6aec57e2083d0993abed0f6c0383", // fr
				"bc2e7092f41622fec6e6921ed6e67308", // it
				"3e35f31545919422ee3f2cdbcc4a920e", // ja
				"4758ed651e09287fa6cc3bdab732c381", // pt
				"140e7cb8758bd41932a6ee5d1118ea7c", // zh
			};
	}

	public String[] getMd5After() {
		return new String[] {
				// ReaderResources
				"20026c7ea22c58ad0dc956f37886f7ad", // default
				"e7671e2320f4023e8f2bc797177c4c83", // de
				"f11de70514f0b2eadeb45a1739b5a772", // en_GB
				"7d92bc5f5f1cb6b7a9c46e38ea5e0da5", // es
				"e2ce8dec83127c36f190a23336f76b29", // fr
				"53b561f87706965db09a7466e635fd3b", // it
				"3371ac108a9b8f47c09e5c1643822168", // ja
				"129c30a67f0de6b919ab4abc3835c9fe", // pt
				"f23b46e9dc30003b96657d38aea48815", // zh
				// MobiReaderImplResources
				"271042a1bd79586f0ba448eee3909019", // default
				"e59da5ab3b57144a0276631b7f330b40", // de
				"986a0ada5b0419b4193f6460a641c76e", // en_GB
				"482391eb2461efd204b680c391e18609", // es
				"9c5a200dcb952162764f4e2a979a47d4", // fr
				"4c846176642d73a43fbf3bde9db6525d", // it
				"072880892824044380784b111b481775", // ja
				"22edfc8d3dee257038626b01266ad9ca", // pt
				"b2886e107f049ab214646abc83554c66", // zh
				// ReaderUtilsResources
				"64019d8997477d7dc915f65bed702c7a", // default
				"10259dbed41f02b37f43eee11ca09f09", // de
				"576b63d4b2c86da7e0f92bc861beaf1f", // en_GB
				"87bd8d0c35eec608e019af0d8e6f9ef1", // es
				"924cb47e9b0f7f6baf4046cc3581329f", // fr
				"dc52b174e228141008996c68e344cace", // it
				"f022dc2fbba561550ec596b4d839ef0c", // ja
				"3db6ee4d218546fd93742c466cf270af", // pt
				"0aa5073c43edcba4bf3b7ef0afef9942", // zh
			};
	}

	public String getFieldName() {
		return "H";
	}

}

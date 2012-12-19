package com.mobileread.ixtab.patch.fontsize;

public class FWAdapter512 extends FWAdapter {

	public String[] getSupportedLocales() {
		return new String[]{"en","de","es","fr","it","pt"};
	}

	public String[] getClasses() {
		return new String[] {
				"com.amazon.ebook.booklet.reader.resources.ReaderResources",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_de",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_en",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_en_GB",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_es",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_fr",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_it",
				"com.amazon.ebook.booklet.reader.resources.ReaderResources_pt",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_de",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_en_GB",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_es",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_fr",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_it",
				"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_pt",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_de",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_en_GB",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_es",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_fr",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_it",
				"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_pt",
			};
	}

	public String[] getMd5Before() {
		return new String[] {
				// ReaderResources
				"1a92381dcf76ba5640e1484e6d3528e3",
				"5b2b068bfdfe075b02a5401191543860",
				"4c46534179c65e364faf125a0bfdd35a",
				"408ee5536be3eda70c939899394ff980",
				"b7c900e8fd34cca90d81c46ea53ec645",
				"1a9ccd3e1dfd979a4532c37eba42eeb0",
				"9b8c4887eed56ddd26a0465cf32f718b",
				"b716ea26fb223daaaecb2f97d252419c",
				// MobiReaderImplResources
				"848a34e10a878cdf75f8e657a496018b",
				"19c03c7da3fe5e79729347282aa6ad23",
				"7b1820a2f0cd4e54c163307868a0846d",
				"9f1c5fd275af4aa1701d28e4bcbadc3f",
				"1ea92dfbf367c4db1f0fd58e1724c703",
				"a142c505c40ce963c5e8c28002bf3368",
				"13d864b7fae715f570cec46dfd356239",
				// ReaderUtilsResources
				"2af2e83e6065ce43a2150508de566942",
				"b080da9e40006e85fd757a559d82c6ca",
				"1f999abeaf3fdd8abc1e44c7301cd45b",
				"a1ba01510ea1e8bda434e72a48f45bd7",
				"f066279d800e85a7c537d41c1483bbe2",
				"3191e538b1f75846750a0bc65851570b",
				"b16a895f7bf9bf0a9ee50b69f1cafbe1",
			};
	}

	public String[] getMd5After() {
		return new String[] {
				//
				"213c8870afd8e52cafa33893db85de5e",
				"5c833f8d70695ca1d3aef3da29234511",
				"25b1b4c381c20626100ca16446645121",
				"e9059bbd30f92894bdf83b64ff295a67",
				"b70417f1aaaa14e23abcb6f49fb9d53a",
				"990858ab29f17f68d3bf8ceec8cfd6f4",
				"3e08021b1ec2a8e96ccdaee18e93fc88",
				"b8a1e61b240583ef63c99680565410e9",
				//
				"ff456df6df7ea17a6fb1270766ab07bf",
				"5d66106dd3274913dce024fbcf0ff12b",
				"faa09f981738392092b34a8c82d8ed5b",
				"8cdbc4d86d49378ab6448879a9db5974",
				"612134ed6bb2696a4423a84a7248dccb",
				"0ae0749814883bb200a914f603fd5c87",
				"f5ff8ca492ebaff1ec7950d50a0acf18",
				//
				"612dc4f3cf5abc7230436930902784d5",
				"38d78672a6e6bc4266c224423ffa47ab",
				"014bf384326a0c31a717277e4857858b",
				"d567d06460d55e09ef6ecf8068560903",
				"a2dd94e9421dca4a844ad5a17d8822af",
				"cf9e82b35c5dcd15a3fdc2aefafc384a",
				"cf4c12d538d246ee7160905a021f0897",
			};
	}

	public String getFieldName() {
		return "f";
	}

}

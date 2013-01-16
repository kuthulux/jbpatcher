package com.mobileread.ixtab.patch.fontsize;

public class FWAdapter532 extends FWAdapter {

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
				"07c7636445d091e89620058d96204ede", // default
				"442f95ef54a505d8770ce4751b51488a", // de
				"5ddcf895d6baad0ebf947cb93ade7ed2", // en_GB
				"be83404d501b53ef477b8cd7ae8b8a5c", // es
				"2b5694b0ae313f039343c23d63eb98e3", // fr
				"4ff4baf567c44f4b180530a0dc8909ad", // it
				"23f951c874448894a2bce5202ea20a59", // ja
				"2d7c21795bcd9744e18420ef628aea20", // pt
				"71691f1509c01a456db70ad771f6a5df", // zh
				// MobiReaderImplResources
				"9b270a52d9995414156a9e3164a62853", // default
				"0f322752fce3af6390c01a4d9487adad", // de
				"5919f9f86df18bf59807c4f36a1782ef", // en_GB
				"fa7d723dd000011e9af47d7a18cc0ae8", // es
				"c416fdba54c6c141460879b8a45fbbd5", // fr
				"e168d622522c0800ec6a6cc8895b869c", // it
				"0413594d016b7eac70ab6297f0999b98", // ja
				"9987885abae0945528d17db1a4bf164a", // pt
				"55a87bbc952e1201383c4ea403e5f66c", // zh
				// ReaderUtilsResources
				"bb3488fc2643ad56db35c7a29948325e", // default
				"227bcaab0bf16dae08bdb7ee3a471afd", // de
				"4ed98bd0616b2550f2ff0052600cc94a", // en_GB
				"8593c40e30c633a7e74bd7d2e90d0bdf", // es
				"3a6203741cd2fd1b7da31b0c2bd26f2d", // fr
				"f59c3373dec5420a8712b0749c5c0626", // it
				"9e2bdabb9964adacea19d0abd5a0a98a", // ja
				"dd1eb63fa433568bcacf033816b370e0", // pt
				"fb3dd2ebd9ab7d6e41cd39d0baf7c795", // zh
			};
	}

	public String[] getMd5After() {
		return new String[] {
				//FIXME: these ALL need to be fixed. :-(
				// ReaderResources
				"58b4138a95565c05bbf3ba5993403e00", // default
				"56a0682f199d872b3298bad024df696d", // de
				"c5e4a530ae2cd4e58f98ed1cd646c4ff", // en_GB
				"e6ac5073591b85ee49397bebc0188338", // es
				"119cd47e3d43e4a7ee7a1e2e257fd512", // fr
				"0c2a4a832971c005319ea6d7bb4377af", // it
				"b1af745b770cd491b8f8470eb7e334db", // ja
				"e93a66434442b7a78eff7701cb9465ca", // pt
				"587ec3d3263f9970b1000e69b55d8b27", // zh
				// MobiReaderImplResources
				"333ad3b5337f5a192ba4c546ff6713ca", // default
				"41ecd09d0e51f279da87ba06e3916a4e", // de
				"6f1d686ca9ebe6b897afd44a16a25506", // en_GB
				"aa03cd3b301da09049e26b8261b23b1f", // es
				"59fb025e0a0bf3cd541b8ff79e4356bd", // fr
				"362c4212773d4a24b287676237f1a04f", // it
				"153901a73a848bf949b9ec6dbcb24857", // ja
				"6250940d890d25e4b52d9cafce29be02", // pt
				"a42d157f0b3137c9b2c31599a08174f1", // zh
				// ReaderUtilsResources
				"789ab8a8ea29e4473c77fafc1d365073", // default
				"767e3069a9d6b0c9f17c7e6c4677bf02", // de
				"a5d95b76e02ad35eaf6c1e48a8c74a3b", // en_GB
				"b615862debebe11a6ab5686ed8efccb5", // es
				"417622c5c7aadba922e7280babb9b48c", // fr
				"0ffcff217807f134e8e649cd86c97bed", // it
				"0fbbe5412a82375b13e84d7aa3ceca8e", // ja
				"8b3f12676cb2ce4731399ff629bc3997", // pt
				"39b21caab38c28fba3d3e4824be50e5f", // zh
			};
	}

	public String getFieldName() {
		return "H";
	}

}

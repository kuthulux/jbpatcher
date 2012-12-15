package com.mobileread.ixtab.patch.hyphenation.common;


public interface HyphenationEngine {
    public abstract int[] getSupportedLanguageIds();
    public abstract String getHyphenSymbol();
    public abstract Hyphenation hyphenate(String s);
}

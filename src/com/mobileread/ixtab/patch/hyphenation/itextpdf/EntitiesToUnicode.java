/*
 * $Id: EntitiesToUnicode.java 5075 2012-02-27 16:36:18Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.mobileread.ixtab.patch.hyphenation.itextpdf;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains entities that can be used in an entity tag.
 */

public class EntitiesToUnicode {

    /**
     * This is a map that contains the names of entities and their unicode value.
     */
    private static final Map MAP = new HashMap();
    static {
        MAP.put("nbsp",new Character('\u00a0')); // no-break space = non-breaking space, U+00A0 ISOnum
        MAP.put("iexcl",new Character('\u00a1')); // inverted exclamation mark, U+00A1 ISOnum
        MAP.put("cent",new Character('\u00a2')); // cent sign, U+00A2 ISOnum
        MAP.put("pound",new Character('\u00a3')); // pound sign, U+00A3 ISOnum
        MAP.put("curren",new Character('\u00a4')); // currency sign, U+00A4 ISOnum
        MAP.put("yen",new Character('\u00a5')); // yen sign = yuan sign, U+00A5 ISOnum
        MAP.put("brvbar",new Character('\u00a6')); // broken bar = broken vertical bar, U+00A6 ISOnum
        MAP.put("sect",new Character('\u00a7')); // section sign, U+00A7 ISOnum
        MAP.put("uml",new Character('\u00a8')); // diaeresis = spacing diaeresis, U+00A8 ISOdia
        MAP.put("copy",new Character('\u00a9')); // copyright sign, U+00A9 ISOnum
        MAP.put("ordf",new Character('\u00aa')); // feminine ordinal indicator, U+00AA ISOnum
        MAP.put("laquo",new Character('\u00ab')); // left-pointing double angle quotation mark = left pointing guillemet, U+00AB ISOnum
        MAP.put("not",new Character('\u00ac')); // not sign, U+00AC ISOnum
        MAP.put("shy",new Character('\u00ad')); // soft hyphen = discretionary hyphen, U+00AD ISOnum
        MAP.put("reg",new Character('\u00ae')); // registered sign = registered trade mark sign, U+00AE ISOnum
        MAP.put("macr",new Character('\u00af')); // macron = spacing macron = overline = APL overbar, U+00AF ISOdia
        MAP.put("deg",new Character('\u00b0')); // degree sign, U+00B0 ISOnum
        MAP.put("plusmn",new Character('\u00b1')); // plus-minus sign = plus-or-minus sign, U+00B1 ISOnum
        MAP.put("sup2",new Character('\u00b2')); // superscript two = superscript digit two = squared, U+00B2 ISOnum
        MAP.put("sup3",new Character('\u00b3')); // superscript three = superscript digit three = cubed, U+00B3 ISOnum
        MAP.put("acute",new Character('\u00b4')); // acute accent = spacing acute, U+00B4 ISOdia
        MAP.put("micro",new Character('\u00b5')); // micro sign, U+00B5 ISOnum
        MAP.put("para",new Character('\u00b6')); // pilcrow sign = paragraph sign, U+00B6 ISOnum
        MAP.put("middot",new Character('\u00b7')); // middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum
        MAP.put("cedil",new Character('\u00b8')); // cedilla = spacing cedilla, U+00B8 ISOdia
        MAP.put("sup1",new Character('\u00b9')); // superscript one = superscript digit one, U+00B9 ISOnum
        MAP.put("ordm",new Character('\u00ba')); // masculine ordinal indicator, U+00BA ISOnum
        MAP.put("raquo",new Character('\u00bb')); // right-pointing double angle quotation mark = right pointing guillemet, U+00BB ISOnum
        MAP.put("frac14",new Character('\u00bc')); // vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum
        MAP.put("frac12",new Character('\u00bd')); // vulgar fraction one half = fraction one half, U+00BD ISOnum
        MAP.put("frac34",new Character('\u00be')); // vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum
        MAP.put("iquest",new Character('\u00bf')); // inverted question mark = turned question mark, U+00BF ISOnum
        MAP.put("Agrave",new Character('\u00c0')); // latin capital letter A with grave = latin capital letter A grave, U+00C0 ISOlat1
        MAP.put("Aacute",new Character('\u00c1')); // latin capital letter A with acute, U+00C1 ISOlat1
        MAP.put("Acirc",new Character('\u00c2')); // latin capital letter A with circumflex, U+00C2 ISOlat1
        MAP.put("Atilde",new Character('\u00c3')); // latin capital letter A with tilde, U+00C3 ISOlat1
        MAP.put("Auml",new Character('\u00c4')); // latin capital letter A with diaeresis, U+00C4 ISOlat1
        MAP.put("Aring",new Character('\u00c5')); // latin capital letter A with ring above = latin capital letter A ring, U+00C5 ISOlat1
        MAP.put("AElig",new Character('\u00c6')); // latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1
        MAP.put("Ccedil",new Character('\u00c7')); // latin capital letter C with cedilla, U+00C7 ISOlat1
        MAP.put("Egrave",new Character('\u00c8')); // latin capital letter E with grave, U+00C8 ISOlat1
        MAP.put("Eacute",new Character('\u00c9')); // latin capital letter E with acute, U+00C9 ISOlat1
        MAP.put("Ecirc",new Character('\u00ca')); // latin capital letter E with circumflex, U+00CA ISOlat1
        MAP.put("Euml",new Character('\u00cb')); // latin capital letter E with diaeresis, U+00CB ISOlat1
        MAP.put("Igrave",new Character('\u00cc')); // latin capital letter I with grave, U+00CC ISOlat1
        MAP.put("Iacute",new Character('\u00cd')); // latin capital letter I with acute, U+00CD ISOlat1
        MAP.put("Icirc",new Character('\u00ce')); // latin capital letter I with circumflex, U+00CE ISOlat1
        MAP.put("Iuml",new Character('\u00cf')); // latin capital letter I with diaeresis, U+00CF ISOlat1
        MAP.put("ETH",new Character('\u00d0')); // latin capital letter ETH, U+00D0 ISOlat1
        MAP.put("Ntilde",new Character('\u00d1')); // latin capital letter N with tilde, U+00D1 ISOlat1
        MAP.put("Ograve",new Character('\u00d2')); // latin capital letter O with grave, U+00D2 ISOlat1
        MAP.put("Oacute",new Character('\u00d3')); // latin capital letter O with acute, U+00D3 ISOlat1
        MAP.put("Ocirc",new Character('\u00d4')); // latin capital letter O with circumflex, U+00D4 ISOlat1
        MAP.put("Otilde",new Character('\u00d5')); // latin capital letter O with tilde, U+00D5 ISOlat1
        MAP.put("Ouml",new Character('\u00d6')); // latin capital letter O with diaeresis, U+00D6 ISOlat1
        MAP.put("times",new Character('\u00d7')); // multiplication sign, U+00D7 ISOnum
        MAP.put("Oslash",new Character('\u00d8')); // latin capital letter O with stroke = latin capital letter O slash, U+00D8 ISOlat1
        MAP.put("Ugrave",new Character('\u00d9')); // latin capital letter U with grave, U+00D9 ISOlat1
        MAP.put("Uacute",new Character('\u00da')); // latin capital letter U with acute, U+00DA ISOlat1
        MAP.put("Ucirc",new Character('\u00db')); // latin capital letter U with circumflex, U+00DB ISOlat1
        MAP.put("Uuml",new Character('\u00dc')); // latin capital letter U with diaeresis, U+00DC ISOlat1
        MAP.put("Yacute",new Character('\u00dd')); // latin capital letter Y with acute, U+00DD ISOlat1
        MAP.put("THORN",new Character('\u00de')); // latin capital letter THORN, U+00DE ISOlat1
        MAP.put("szlig",new Character('\u00df')); // latin small letter sharp s = ess-zed, U+00DF ISOlat1
        MAP.put("agrave",new Character('\u00e0')); // latin small letter a with grave = latin small letter a grave, U+00E0 ISOlat1
        MAP.put("aacute",new Character('\u00e1')); // latin small letter a with acute, U+00E1 ISOlat1
        MAP.put("acirc",new Character('\u00e2')); // latin small letter a with circumflex, U+00E2 ISOlat1
        MAP.put("atilde",new Character('\u00e3')); // latin small letter a with tilde, U+00E3 ISOlat1
        MAP.put("auml",new Character('\u00e4')); // latin small letter a with diaeresis, U+00E4 ISOlat1
        MAP.put("aring",new Character('\u00e5')); // latin small letter a with ring above = latin small letter a ring, U+00E5 ISOlat1
        MAP.put("aelig",new Character('\u00e6')); // latin small letter ae = latin small ligature ae, U+00E6 ISOlat1
        MAP.put("ccedil",new Character('\u00e7')); // latin small letter c with cedilla, U+00E7 ISOlat1
        MAP.put("egrave",new Character('\u00e8')); // latin small letter e with grave, U+00E8 ISOlat1
        MAP.put("eacute",new Character('\u00e9')); // latin small letter e with acute, U+00E9 ISOlat1
        MAP.put("ecirc",new Character('\u00ea')); // latin small letter e with circumflex, U+00EA ISOlat1
        MAP.put("euml",new Character('\u00eb')); // latin small letter e with diaeresis, U+00EB ISOlat1
        MAP.put("igrave",new Character('\u00ec')); // latin small letter i with grave, U+00EC ISOlat1
        MAP.put("iacute",new Character('\u00ed')); // latin small letter i with acute, U+00ED ISOlat1
        MAP.put("icirc",new Character('\u00ee')); // latin small letter i with circumflex, U+00EE ISOlat1
        MAP.put("iuml",new Character('\u00ef')); // latin small letter i with diaeresis, U+00EF ISOlat1
        MAP.put("eth",new Character('\u00f0')); // latin small letter eth, U+00F0 ISOlat1
        MAP.put("ntilde",new Character('\u00f1')); // latin small letter n with tilde, U+00F1 ISOlat1
        MAP.put("ograve",new Character('\u00f2')); // latin small letter o with grave, U+00F2 ISOlat1
        MAP.put("oacute",new Character('\u00f3')); // latin small letter o with acute, U+00F3 ISOlat1
        MAP.put("ocirc",new Character('\u00f4')); // latin small letter o with circumflex, U+00F4 ISOlat1
        MAP.put("otilde",new Character('\u00f5')); // latin small letter o with tilde, U+00F5 ISOlat1
        MAP.put("ouml",new Character('\u00f6')); // latin small letter o with diaeresis, U+00F6 ISOlat1
        MAP.put("divide",new Character('\u00f7')); // division sign, U+00F7 ISOnum
        MAP.put("oslash",new Character('\u00f8')); // latin small letter o with stroke, = latin small letter o slash, U+00F8 ISOlat1
        MAP.put("ugrave",new Character('\u00f9')); // latin small letter u with grave, U+00F9 ISOlat1
        MAP.put("uacute",new Character('\u00fa')); // latin small letter u with acute, U+00FA ISOlat1
        MAP.put("ucirc",new Character('\u00fb')); // latin small letter u with circumflex, U+00FB ISOlat1
        MAP.put("uuml",new Character('\u00fc')); // latin small letter u with diaeresis, U+00FC ISOlat1
        MAP.put("yacute",new Character('\u00fd')); // latin small letter y with acute, U+00FD ISOlat1
        MAP.put("thorn",new Character('\u00fe')); // latin small letter thorn, U+00FE ISOlat1
        MAP.put("yuml",new Character('\u00ff')); // latin small letter y with diaeresis, U+00FF ISOlat1
        // Latin Extended-B
        MAP.put("fnof",new Character('\u0192')); // latin small f with hook = function = florin, U+0192 ISOtech
        // Greek
        MAP.put("Alpha",new Character('\u0391')); // greek capital letter alpha, U+0391
        MAP.put("Beta",new Character('\u0392')); // greek capital letter beta, U+0392
        MAP.put("Gamma",new Character('\u0393')); // greek capital letter gamma, U+0393 ISOgrk3
        MAP.put("Delta",new Character('\u0394')); // greek capital letter delta, U+0394 ISOgrk3
        MAP.put("Epsilon",new Character('\u0395')); // greek capital letter epsilon, U+0395
        MAP.put("Zeta",new Character('\u0396')); // greek capital letter zeta, U+0396
        MAP.put("Eta",new Character('\u0397')); // greek capital letter eta, U+0397
        MAP.put("Theta",new Character('\u0398')); // greek capital letter theta, U+0398 ISOgrk3
        MAP.put("Iota",new Character('\u0399')); // greek capital letter iota, U+0399
        MAP.put("Kappa",new Character('\u039a')); // greek capital letter kappa, U+039A
        MAP.put("Lambda",new Character('\u039b')); // greek capital letter lambda, U+039B ISOgrk3
        MAP.put("Mu",new Character('\u039c')); // greek capital letter mu, U+039C
        MAP.put("Nu",new Character('\u039d')); // greek capital letter nu, U+039D
        MAP.put("Xi",new Character('\u039e')); // greek capital letter xi, U+039E ISOgrk3
        MAP.put("Omicron",new Character('\u039f')); // greek capital letter omicron, U+039F
        MAP.put("Pi",new Character('\u03a0')); // greek capital letter pi, U+03A0 ISOgrk3
        MAP.put("Rho",new Character('\u03a1')); // greek capital letter rho, U+03A1
        // there is no Sigmaf, and no U+03A2 character either
        MAP.put("Sigma",new Character('\u03a3')); // greek capital letter sigma, U+03A3 ISOgrk3
        MAP.put("Tau",new Character('\u03a4')); // greek capital letter tau, U+03A4
        MAP.put("Upsilon",new Character('\u03a5')); // greek capital letter upsilon, U+03A5 ISOgrk3
        MAP.put("Phi",new Character('\u03a6')); // greek capital letter phi, U+03A6 ISOgrk3
        MAP.put("Chi",new Character('\u03a7')); // greek capital letter chi, U+03A7
        MAP.put("Psi",new Character('\u03a8')); // greek capital letter psi, U+03A8 ISOgrk3
        MAP.put("Omega",new Character('\u03a9')); // greek capital letter omega, U+03A9 ISOgrk3
        MAP.put("alpha",new Character('\u03b1')); // greek small letter alpha, U+03B1 ISOgrk3
        MAP.put("beta",new Character('\u03b2')); // greek small letter beta, U+03B2 ISOgrk3
        MAP.put("gamma",new Character('\u03b3')); // greek small letter gamma, U+03B3 ISOgrk3
        MAP.put("delta",new Character('\u03b4')); // greek small letter delta, U+03B4 ISOgrk3
        MAP.put("epsilon",new Character('\u03b5')); // greek small letter epsilon, U+03B5 ISOgrk3
        MAP.put("zeta",new Character('\u03b6')); // greek small letter zeta, U+03B6 ISOgrk3
        MAP.put("eta",new Character('\u03b7')); // greek small letter eta, U+03B7 ISOgrk3
        MAP.put("theta",new Character('\u03b8')); // greek small letter theta, U+03B8 ISOgrk3
        MAP.put("iota",new Character('\u03b9')); // greek small letter iota, U+03B9 ISOgrk3
        MAP.put("kappa",new Character('\u03ba')); // greek small letter kappa, U+03BA ISOgrk3
        MAP.put("lambda",new Character('\u03bb')); // greek small letter lambda, U+03BB ISOgrk3
        MAP.put("mu",new Character('\u03bc')); // greek small letter mu, U+03BC ISOgrk3
        MAP.put("nu",new Character('\u03bd')); // greek small letter nu, U+03BD ISOgrk3
        MAP.put("xi",new Character('\u03be')); // greek small letter xi, U+03BE ISOgrk3
        MAP.put("omicron",new Character('\u03bf')); // greek small letter omicron, U+03BF NEW
        MAP.put("pi",new Character('\u03c0')); // greek small letter pi, U+03C0 ISOgrk3
        MAP.put("rho",new Character('\u03c1')); // greek small letter rho, U+03C1 ISOgrk3
        MAP.put("sigmaf",new Character('\u03c2')); // greek small letter final sigma, U+03C2 ISOgrk3
        MAP.put("sigma",new Character('\u03c3')); // greek small letter sigma, U+03C3 ISOgrk3
        MAP.put("tau",new Character('\u03c4')); // greek small letter tau, U+03C4 ISOgrk3
        MAP.put("upsilon",new Character('\u03c5')); // greek small letter upsilon, U+03C5 ISOgrk3
        MAP.put("phi",new Character('\u03c6')); // greek small letter phi, U+03C6 ISOgrk3
        MAP.put("chi",new Character('\u03c7')); // greek small letter chi, U+03C7 ISOgrk3
        MAP.put("psi",new Character('\u03c8')); // greek small letter psi, U+03C8 ISOgrk3
        MAP.put("omega",new Character('\u03c9')); // greek small letter omega, U+03C9 ISOgrk3
        MAP.put("thetasym",new Character('\u03d1')); // greek small letter theta symbol, U+03D1 NEW
        MAP.put("upsih",new Character('\u03d2')); // greek upsilon with hook symbol, U+03D2 NEW
        MAP.put("piv",new Character('\u03d6')); // greek pi symbol, U+03D6 ISOgrk3
        // General Punctuation
        MAP.put("bull",new Character('\u2022')); // bullet = black small circle, U+2022 ISOpub
        // bullet is NOT the same as bullet operator, U+2219
        MAP.put("hellip",new Character('\u2026')); // horizontal ellipsis = three dot leader, U+2026 ISOpub
        MAP.put("prime",new Character('\u2032')); // prime = minutes = feet, U+2032 ISOtech
        MAP.put("Prime",new Character('\u2033')); // double prime = seconds = inches, U+2033 ISOtech
        MAP.put("oline",new Character('\u203e')); // overline = spacing overscore, U+203E NEW
        MAP.put("frasl",new Character('\u2044')); // fraction slash, U+2044 NEW
        // Letterlike Symbols
        MAP.put("weierp",new Character('\u2118')); // script capital P = power set = Weierstrass p, U+2118 ISOamso
        MAP.put("image",new Character('\u2111')); // blackletter capital I = imaginary part, U+2111 ISOamso
        MAP.put("real",new Character('\u211c')); // blackletter capital R = real part symbol, U+211C ISOamso
        MAP.put("trade",new Character('\u2122')); // trade mark sign, U+2122 ISOnum
        MAP.put("alefsym",new Character('\u2135')); // alef symbol = first transfinite cardinal, U+2135 NEW
        // alef symbol is NOT the same as hebrew letter alef,
        // U+05D0 although the same glyph could be used to depict both characters
        // Arrows
        MAP.put("larr",new Character('\u2190')); // leftwards arrow, U+2190 ISOnum
        MAP.put("uarr",new Character('\u2191')); // upwards arrow, U+2191 ISOnum
        MAP.put("rarr",new Character('\u2192')); // rightwards arrow, U+2192 ISOnum
        MAP.put("darr",new Character('\u2193')); // downwards arrow, U+2193 ISOnum
        MAP.put("harr",new Character('\u2194')); // left right arrow, U+2194 ISOamsa
        MAP.put("crarr",new Character('\u21b5')); // downwards arrow with corner leftwards = carriage return, U+21B5 NEW
        MAP.put("lArr",new Character('\u21d0')); // leftwards double arrow, U+21D0 ISOtech
        // ISO 10646 does not say that lArr is the same as the 'is implied by' arrow
        // but also does not have any other character for that function. So ? lArr can
        // be used for 'is implied by' as ISOtech suggests
        MAP.put("uArr",new Character('\u21d1')); // upwards double arrow, U+21D1 ISOamsa
        MAP.put("rArr",new Character('\u21d2')); // rightwards double arrow, U+21D2 ISOtech
        // ISO 10646 does not say this is the 'implies' character but does not have
        // another character with this function so ?
        // rArr can be used for 'implies' as ISOtech suggests
        MAP.put("dArr",new Character('\u21d3')); // downwards double arrow, U+21D3 ISOamsa
        MAP.put("hArr",new Character('\u21d4')); // left right double arrow, U+21D4 ISOamsa
        // Mathematical Operators
        MAP.put("forall",new Character('\u2200')); // for all, U+2200 ISOtech
        MAP.put("part",new Character('\u2202')); // partial differential, U+2202 ISOtech
        MAP.put("exist",new Character('\u2203')); // there exists, U+2203 ISOtech
        MAP.put("empty",new Character('\u2205')); // empty set = null set = diameter, U+2205 ISOamso
        MAP.put("nabla",new Character('\u2207')); // nabla = backward difference, U+2207 ISOtech
        MAP.put("isin",new Character('\u2208')); // element of, U+2208 ISOtech
        MAP.put("notin",new Character('\u2209')); // not an element of, U+2209 ISOtech
        MAP.put("ni",new Character('\u220b')); // contains as member, U+220B ISOtech
        // should there be a more memorable name than 'ni'?
        MAP.put("prod",new Character('\u220f')); // n-ary product = product sign, U+220F ISOamsb
        // prod is NOT the same character as U+03A0 'greek capital letter pi' though
        // the same glyph might be used for both
        MAP.put("sum",new Character('\u2211')); // n-ary sumation, U+2211 ISOamsb
        // sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both
        MAP.put("minus",new Character('\u2212')); // minus sign, U+2212 ISOtech
        MAP.put("lowast",new Character('\u2217')); // asterisk operator, U+2217 ISOtech
        MAP.put("radic",new Character('\u221a')); // square root = radical sign, U+221A ISOtech
        MAP.put("prop",new Character('\u221d')); // proportional to, U+221D ISOtech
        MAP.put("infin",new Character('\u221e')); // infinity, U+221E ISOtech
        MAP.put("ang",new Character('\u2220')); // angle, U+2220 ISOamso
        MAP.put("and",new Character('\u2227')); // logical and = wedge, U+2227 ISOtech
        MAP.put("or",new Character('\u2228')); // logical or = vee, U+2228 ISOtech
        MAP.put("cap",new Character('\u2229')); // intersection = cap, U+2229 ISOtech
        MAP.put("cup",new Character('\u222a')); // union = cup, U+222A ISOtech
        MAP.put("int",new Character('\u222b')); // integral, U+222B ISOtech
        MAP.put("there4",new Character('\u2234')); // therefore, U+2234 ISOtech
        MAP.put("sim",new Character('\u223c')); // tilde operator = varies with = similar to, U+223C ISOtech
        // tilde operator is NOT the same character as the tilde, U+007E,
        // although the same glyph might be used to represent both
        MAP.put("cong",new Character('\u2245')); // approximately equal to, U+2245 ISOtech
        MAP.put("asymp",new Character('\u2248')); // almost equal to = asymptotic to, U+2248 ISOamsr
        MAP.put("ne",new Character('\u2260')); // not equal to, U+2260 ISOtech
        MAP.put("equiv",new Character('\u2261')); // identical to, U+2261 ISOtech
        MAP.put("le",new Character('\u2264')); // less-than or equal to, U+2264 ISOtech
        MAP.put("ge",new Character('\u2265')); // greater-than or equal to, U+2265 ISOtech
        MAP.put("sub",new Character('\u2282')); // subset of, U+2282 ISOtech
        MAP.put("sup",new Character('\u2283')); // superset of, U+2283 ISOtech
        // note that nsup, 'not a superset of, U+2283' is not covered by the Symbol
        // font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn
        MAP.put("nsub",new Character('\u2284')); // not a subset of, U+2284 ISOamsn
        MAP.put("sube",new Character('\u2286')); // subset of or equal to, U+2286 ISOtech
        MAP.put("supe",new Character('\u2287')); // superset of or equal to, U+2287 ISOtech
        MAP.put("oplus",new Character('\u2295')); // circled plus = direct sum, U+2295 ISOamsb
        MAP.put("otimes",new Character('\u2297')); // circled times = vector product, U+2297 ISOamsb
        MAP.put("perp",new Character('\u22a5')); // up tack = orthogonal to = perpendicular, U+22A5 ISOtech
        MAP.put("sdot",new Character('\u22c5')); // dot operator, U+22C5 ISOamsb
        // dot operator is NOT the same character as U+00B7 middle dot
        // Miscellaneous Technical
        MAP.put("lceil",new Character('\u2308')); // left ceiling = apl upstile, U+2308 ISOamsc
        MAP.put("rceil",new Character('\u2309')); // right ceiling, U+2309 ISOamsc
        MAP.put("lfloor",new Character('\u230a')); // left floor = apl downstile, U+230A ISOamsc
        MAP.put("rfloor",new Character('\u230b')); // right floor, U+230B ISOamsc
        MAP.put("lang",new Character('\u2329')); // left-pointing angle bracket = bra, U+2329 ISOtech
        // lang is NOT the same character as U+003C 'less than'
        // or U+2039 'single left-pointing angle quotation mark'
        MAP.put("rang",new Character('\u232a')); // right-pointing angle bracket = ket, U+232A ISOtech
        // rang is NOT the same character as U+003E 'greater than'
        // or U+203A 'single right-pointing angle quotation mark'
        // Geometric Shapes
        MAP.put("loz",new Character('\u25ca')); // lozenge, U+25CA ISOpub
        // Miscellaneous Symbols
        MAP.put("spades",new Character('\u2660')); // black spade suit, U+2660 ISOpub
        // black here seems to mean filled as opposed to hollow
        MAP.put("clubs",new Character('\u2663')); // black club suit = shamrock, U+2663 ISOpub
        MAP.put("hearts",new Character('\u2665')); // black heart suit = valentine, U+2665 ISOpub
        MAP.put("diams",new Character('\u2666')); // black diamond suit, U+2666 ISOpub
        // C0 Controls and Basic Latin
        MAP.put("quot",new Character('\u0022')); // quotation mark = APL quote, U+0022 ISOnum
        MAP.put("amp",new Character('\u0026')); // ampersand, U+0026 ISOnum
        MAP.put("apos",new Character('\''));
        MAP.put("lt",new Character('\u003c')); // less-than sign, U+003C ISOnum
        MAP.put("gt",new Character('\u003e')); // greater-than sign, U+003E ISOnum
        // Latin Extended-A
        MAP.put("OElig",new Character('\u0152')); // latin capital ligature OE, U+0152 ISOlat2
        MAP.put("oelig",new Character('\u0153')); // latin small ligature oe, U+0153 ISOlat2
        // ligature is a misnomer, this is a separate character in some languages
        MAP.put("Scaron",new Character('\u0160')); // latin capital letter S with caron, U+0160 ISOlat2
        MAP.put("scaron",new Character('\u0161')); // latin small letter s with caron, U+0161 ISOlat2
        MAP.put("Yuml",new Character('\u0178')); // latin capital letter Y with diaeresis, U+0178 ISOlat2
        // Spacing Modifier Letters
        MAP.put("circ",new Character('\u02c6')); // modifier letter circumflex accent, U+02C6 ISOpub
        MAP.put("tilde",new Character('\u02dc')); // small tilde, U+02DC ISOdia
        // General Punctuation
        MAP.put("ensp",new Character('\u2002')); // en space, U+2002 ISOpub
        MAP.put("emsp",new Character('\u2003')); // em space, U+2003 ISOpub
        MAP.put("thinsp",new Character('\u2009')); // thin space, U+2009 ISOpub
        MAP.put("zwnj",new Character('\u200c')); // zero width non-joiner, U+200C NEW RFC 2070
        MAP.put("zwj",new Character('\u200d')); // zero width joiner, U+200D NEW RFC 2070
        MAP.put("lrm",new Character('\u200e')); // left-to-right mark, U+200E NEW RFC 2070
        MAP.put("rlm",new Character('\u200f')); // right-to-left mark, U+200F NEW RFC 2070
        MAP.put("ndash",new Character('\u2013')); // en dash, U+2013 ISOpub
        MAP.put("mdash",new Character('\u2014')); // em dash, U+2014 ISOpub
        MAP.put("lsquo",new Character('\u2018')); // left single quotation mark, U+2018 ISOnum
        MAP.put("rsquo",new Character('\u2019')); // right single quotation mark, U+2019 ISOnum
        MAP.put("sbquo",new Character('\u201a')); // single low-9 quotation mark, U+201A NEW
        MAP.put("ldquo",new Character('\u201c')); // left double quotation mark, U+201C ISOnum
        MAP.put("rdquo",new Character('\u201d')); // right double quotation mark, U+201D ISOnum
        MAP.put("bdquo",new Character('\u201e')); // double low-9 quotation mark, U+201E NEW
        MAP.put("dagger",new Character('\u2020')); // dagger, U+2020 ISOpub
        MAP.put("Dagger",new Character('\u2021')); // double dagger, U+2021 ISOpub
        MAP.put("permil",new Character('\u2030')); // per mille sign, U+2030 ISOtech
        MAP.put("lsaquo",new Character('\u2039')); // single left-pointing angle quotation mark, U+2039 ISO proposed
        // lsaquo is proposed but not yet ISO standardized
        MAP.put("rsaquo",new Character('\u203a')); // single right-pointing angle quotation mark, U+203A ISO proposed
        // rsaquo is proposed but not yet ISO standardized
        MAP.put("euro",new Character('\u20ac')); // euro sign, U+20AC NEW
    }


    /**
     * Translates an entity to a unicode character.
     *
     * @param	name	the name of the entity
     * @return	the corresponding unicode character
     */
    public static char decodeEntity(final String name) {
    	if (name.startsWith("#x")) {
    		try {
    			return (char)Integer.parseInt(name.substring(2),16);
    		}
    		catch(NumberFormatException nfe) {
    			return '\0';
    		}
    	}
    	if (name.startsWith("#")) {
    		try {
    			return (char)Integer.parseInt(name.substring(1));
    		}
    		catch(NumberFormatException nfe) {
    			return '\0';
    		}
    	}
    	Character c = (Character) MAP.get(name);
        if (c == null)
            return '\0';
        else
            return c.charValue();
    }

    /**
     * Translates a String with entities (&...;) to a String without entities,
     * replacing the entity with the right (unicode) character.
     */
    public static String decodeString(final String s) {
    	int pos_amp = s.indexOf('&');
    	if (pos_amp == -1) return s;

    	int pos_sc;
    	int pos_a;
    	StringBuffer buf = new StringBuffer(s.substring(0, pos_amp));
    	char replace;
    	while (true) {
    		pos_sc = s.indexOf(';', pos_amp);
    		if (pos_sc == -1) {
    			buf.append(s.substring(pos_amp));
    			return buf.toString();
    		}
    		pos_a = s.indexOf('&', pos_amp + 1);
    		while (pos_a != -1 && pos_a < pos_sc) {
    			buf.append(s.substring(pos_amp, pos_a));
    			pos_amp = pos_a;
    			pos_a = s.indexOf('&', pos_amp + 1);
    		}
    		replace = decodeEntity(s.substring(pos_amp + 1, pos_sc));
    		if (s.length() < pos_sc + 1) {
    			return buf.toString();
    		}
    		if (replace == '\0') {
    			buf.append(s.substring(pos_amp, pos_sc + 1));
    		}
    		else {
    			buf.append(replace);
    		}
    		pos_amp = s.indexOf('&', pos_sc);
    		if (pos_amp == -1) {
    			buf.append(s.substring(pos_sc + 1));
    			return buf.toString();
    		}
    		else {
    			buf.append(s.substring(pos_sc + 1, pos_amp));
    		}
    	}
    }
}
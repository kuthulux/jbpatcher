/*
 * $Id: SimplePatternParser.java 5075 2012-02-27 16:36:18Z blowagie $
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;


/** Parses the xml hyphenation pattern.
 *
 * @author Paulo Soares
 */
public class SimplePatternParser implements SimpleXMLDocHandler,
		PatternConsumer {
	int currElement;

	PatternConsumer consumer;

	StringBuffer token;

	ArrayList exception;

	char hyphenChar;

	SimpleXMLParser parser;

	static final int ELEM_CLASSES = 1;

	static final int ELEM_EXCEPTIONS = 2;

	static final int ELEM_PATTERNS = 3;

	static final int ELEM_HYPHEN = 4;

	/** Creates a new instance of PatternParser2 */
	public SimplePatternParser() {
		token = new StringBuffer();
		hyphenChar = '-'; // default
	}

	public void parse(InputStream stream, PatternConsumer consumer) {
		this.consumer = consumer;
		try {
			SimpleXMLParser.parse(this, stream);
		} catch (IOException e) {
			throw new ExceptionConverter(e);
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
			}
		}
	}

	protected static String getPattern(String word) {
		StringBuffer pat = new StringBuffer();
		int len = word.length();
		for (int i = 0; i < len; i++) {
			if (!Character.isDigit(word.charAt(i))) {
				pat.append(word.charAt(i));
			}
		}
		return pat.toString();
	}

	protected ArrayList normalizeException(ArrayList ex) {
		ArrayList res = new ArrayList();
		for (int i = 0; i < ex.size(); i++) {
			Object item = ex.get(i);
			if (item instanceof String) {
				String str = (String) item;
				StringBuffer buf = new StringBuffer();
				for (int j = 0; j < str.length(); j++) {
					char c = str.charAt(j);
					if (c != hyphenChar) {
						buf.append(c);
					} else {
						res.add(buf.toString());
						buf.setLength(0);
						char[] h = new char[1];
						h[0] = hyphenChar;
						// we use here hyphenChar which is not necessarily
						// the one to be printed
						res.add(new Hyphen(new String(h), null, null));
					}
				}
				if (buf.length() > 0) {
					res.add(buf.toString());
				}
			} else {
				res.add(item);
			}
		}
		return res;
	}

	protected String getExceptionWord(ArrayList ex) {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < ex.size(); i++) {
			Object item = ex.get(i);
			if (item instanceof String) {
				res.append((String) item);
			} else {
				if (((Hyphen) item).noBreak != null) {
					res.append(((Hyphen) item).noBreak);
				}
			}
		}
		return res.toString();
	}

	protected static String getInterletterValues(String pat) {
		StringBuffer il = new StringBuffer();
		String word = pat + "a"; // add dummy letter to serve as sentinel
		int len = word.length();
		for (int i = 0; i < len; i++) {
			char c = word.charAt(i);
			if (Character.isDigit(c)) {
				il.append(c);
				i++;
			} else {
				il.append('0');
			}
		}
		return il.toString();
	}

	public void endDocument() {
	}

    public void endElement(String tag) {
		if (token.length() > 0) {
			String word = token.toString();
			switch (currElement) {
			case ELEM_CLASSES:
				consumer.addClass(word);
				break;
			case ELEM_EXCEPTIONS:
				exception.add(word);
				exception = normalizeException(exception);
				consumer.addException(getExceptionWord(exception),
						(ArrayList) exception.clone());
				break;
			case ELEM_PATTERNS:
				consumer.addPattern(getPattern(word),
						getInterletterValues(word));
				break;
			case ELEM_HYPHEN:
				// nothing to do
				break;
			}
			if (currElement != ELEM_HYPHEN) {
				token.setLength(0);
			}
		}
		if (currElement == ELEM_HYPHEN) {
			currElement = ELEM_EXCEPTIONS;
		} else {
			currElement = 0;
		}
	}

	public void startDocument() {
	}

	public void startElement(String tag, Map h) {
		if (tag.equals("hyphen-char")) {
			String hh = (String) h.get("value");
			if (hh != null && hh.length() == 1) {
				hyphenChar = hh.charAt(0);
			}
		} else if (tag.equals("classes")) {
			currElement = ELEM_CLASSES;
		} else if (tag.equals("patterns")) {
			currElement = ELEM_PATTERNS;
		} else if (tag.equals("exceptions")) {
			currElement = ELEM_EXCEPTIONS;
			exception = new ArrayList();
		} else if (tag.equals("hyphen")) {
			if (token.length() > 0) {
				exception.add(token.toString());
			}
			exception.add(new Hyphen((String)h.get("pre"), (String)h
					.get("no"), (String)h.get("post")));
			currElement = ELEM_HYPHEN;
		}
		token.setLength(0);
	}

    public void text(String str) {
		StringTokenizer tk = new StringTokenizer(str);
		while (tk.hasMoreTokens()) {
			String word = tk.nextToken();
			// System.out.println("\"" + word + "\"");
			switch (currElement) {
			case ELEM_CLASSES:
				consumer.addClass(word);
				break;
			case ELEM_EXCEPTIONS:
				exception.add(word);
				exception = normalizeException(exception);
				consumer.addException(getExceptionWord(exception),
						(ArrayList) exception.clone());
				exception.clear();
				break;
			case ELEM_PATTERNS:
				consumer.addPattern(getPattern(word),
						getInterletterValues(word));
				break;
			}
		}
	}

	// PatternConsumer implementation for testing purposes
	public void addClass(String c) {
		System.out.println("class: " + c);
	}

	public void addException(String w, ArrayList e) {
		System.out.println("exception: " + w + " : " + e.toString());
	}

	public void addPattern(String p, String v) {
		System.out.println("pattern: " + p + " : " + v);
	}
	/*
	public static void main(String[] args) throws Exception {
		try {
			if (args.length > 0) {
				SimplePatternParser pp = new SimplePatternParser();
				pp.parse(new FileInputStream(args[0]), pp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}

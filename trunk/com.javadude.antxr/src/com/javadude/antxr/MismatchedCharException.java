/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *******************************************************************************/
package com.javadude.antxr;

import com.javadude.antxr.collections.impl.BitSet;

public class MismatchedCharException extends RecognitionException {
	private static final long serialVersionUID = 1L;
	// Types of chars
    public static final int CHAR = 1;
    public static final int NOT_CHAR = 2;
    public static final int RANGE = 3;
    public static final int NOT_RANGE = 4;
    public static final int SET = 5;
    public static final int NOT_SET = 6;

    // One of the above
    public int mismatchType;

    // what was found on the input stream
    public int foundChar;

    // For CHAR/NOT_CHAR and RANGE/NOT_RANGE
    public int expecting;

    // For RANGE/NOT_RANGE (expecting is lower bound of range)
    public int upper;

    // For SET/NOT_SET
    public BitSet set;

    // who knows...they may want to ask scanner questions
    public CharScanner scanner;

    /**
     * MismatchedCharException constructor comment.
     */
    public MismatchedCharException() {
        super("Mismatched char");
    }

    // Expected range / not range
    public MismatchedCharException(char c, char lower, char upper_, boolean matchNot, CharScanner scanner_) {
        super("Mismatched char", scanner_.getFilename(), scanner_.getLine(), scanner_.getColumn());
        mismatchType = matchNot ? NOT_RANGE : RANGE;
        foundChar = c;
        expecting = lower;
        upper = upper_;
        scanner = scanner_;
    }

    // Expected token / not token
    public MismatchedCharException(char c, char expecting_, boolean matchNot, CharScanner scanner_) {
        super("Mismatched char", scanner_.getFilename(), scanner_.getLine(), scanner_.getColumn());
        mismatchType = matchNot ? NOT_CHAR : CHAR;
        foundChar = c;
        expecting = expecting_;
        scanner = scanner_;
    }

    // Expected BitSet / not BitSet
    public MismatchedCharException(char c, BitSet set_, boolean matchNot, CharScanner scanner_) {
        super("Mismatched char", scanner_.getFilename(), scanner_.getLine(), scanner_.getColumn());
        mismatchType = matchNot ? NOT_SET : SET;
        foundChar = c;
        set = set_;
        scanner = scanner_;
    }

    /**
     * Returns a clean error message (no line number/column information)
     */
    @Override
    public String getMessage() {
        StringBuffer sb = new StringBuffer();

        switch (mismatchType) {
            case CHAR:
                sb.append("expecting ");   appendCharName(sb, expecting);
                sb.append(", found ");     appendCharName(sb, foundChar);
                break;
            case NOT_CHAR:
                sb.append("expecting anything but '");
                appendCharName(sb, expecting);
                sb.append("'; got it anyway");
                break;
            case RANGE:
            case NOT_RANGE:
                sb.append("expecting token ");
                if (mismatchType == NOT_RANGE) {
                    sb.append("NOT ");
                }
                sb.append("in range: ");
                appendCharName(sb, expecting);
                sb.append("..");
                appendCharName(sb, upper);
                sb.append(", found ");
                appendCharName(sb, foundChar);
                break;
            case SET:
            case NOT_SET:
                sb.append("expecting " + (mismatchType == NOT_SET ? "NOT " : "") + "one of (");
                int[] elems = set.toArray();
                for (int i = 0; i < elems.length; i++) {
                    appendCharName(sb, elems[i]);
                }
                sb.append("), found ");
                appendCharName(sb, foundChar);
                break;
            default :
                sb.append(super.getMessage());
                break;
        }

        return sb.toString();
    }

    /** Append a char to the msg buffer.  If special,
	 *  then show escaped version
	 */
	private void appendCharName(StringBuffer sb, int c) {
        switch (c) {
		case 65535 :
			// 65535 = (char) -1 = EOF
            sb.append("'<EOF>'");
			break;
		case '\n' :
			sb.append("'\\n'");
			break;
		case '\r' :
			sb.append("'\\r'");
			break;
		case '\t' :
			sb.append("'\\t'");
			break;
		default :
            sb.append('\'');
            sb.append((char) c);
            sb.append('\'');
			break;
        }
    }
}


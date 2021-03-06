/**
 * Copyright 2010 The European Bioinformatics Institute, and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hupo.psi.calimocho.tab.util;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
public class ParseUtils {

    /**
     * <p>Processes an String and splits using a set of delimiters.
     * If the delimiter is in a group surrounded by quotes, don't split that group.
     * Quotes in the String must be escaped using a backlash if that quote does not delimit a group.</p>
     * <p/>
     * <pre>
     *       aaaaa|bbbb  (delimiter '|')          = ["aaaaaa", "bbbbbb"]
     *       "aaa|aaa"|bbbbb  (delimiter '|)      = ["aaa|aaa", "bbbbb"]
     *       "aa\"a|a\"aa"|bbbbb  (delimiter '|)  = ["aa"a|a"aa", "bbbbb"]
     * </pre>
     *
     * @param str                   The string to split
     * @param delimiters            The delimiters to use
     * @param removeUnescapedQuotes remove unsecaped quotes.
     * @return An array containing the groups after splitting
     */
    public static String[] quoteAwareSplit(String str, String[] delimiters, boolean removeUnescapedQuotes) {
        if (str == null) {
            throw new NullPointerException("Null String to create Field");
        }

        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Empty String passed to create Field");
        }

        if (delimiters == null) {
            throw new NullPointerException("Null delimiters to create Field");
        }

        if (delimiters.length == 0) {
            throw new IllegalArgumentException("At least one delimiter char is needed");
        }

        List<String> groups = new LinkedList<String>();

        StringBuilder currGroup = new StringBuilder(str.length());

        final char[] chars = str.toCharArray();

        boolean withinQuotes = false;
        boolean previousCharIsEscape = false;

        Character previousChar = null;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            boolean markedAsEscape = false;

            if (c == '"') {
                if (withinQuotes) {
                    if (previousCharIsEscape) {
                        if (!removeUnescapedQuotes) {
                            currGroup.append("\\");
                        }
                        currGroup.append(c);
                    } else if (currGroup.length() > 0) {
                        withinQuotes = !withinQuotes;
                    }
                } else {
                    withinQuotes = true;
                }

                if (!removeUnescapedQuotes && !previousCharIsEscape) {
                    currGroup.append(c);
                }

            } else if (delimiters != null && arrayContains(delimiters, c)) {
                if (currGroup.length() > 0) {
                    if (!withinQuotes) {
                        groups.add(currGroup.toString());
                        // Note: the length of the stringbuilder can only be smaller, let's reuse the existing one.
                        currGroup.setLength(0);
                    } else {
                        currGroup.append(c);
                    }
                } else if (withinQuotes) {
                    currGroup.append(c);
                } else {
                    groups.add(currGroup.toString());
                }
            } else if (c == '\\') {
                if (withinQuotes) {
                    previousCharIsEscape = true;
                    markedAsEscape = true;
                } else {
                    currGroup.append(c);
                }
            } else {
                currGroup.append(c);
            }

            if (!markedAsEscape) {
                previousCharIsEscape = false;
            }
        }

        if (currGroup.length() > 0) {
            groups.add(currGroup.toString());
        }

        return groups.toArray(new String[groups.size()]);
    }

    private static boolean arrayContains(String[] strs, char cToFind) {
        for (String str : strs) {
            if (arrayContains(str.toCharArray(), cToFind)) {
                return true;
            }
        }

        return false;
    }

    private static boolean arrayContains(char[] chars, char cToFind) {
        for (char c : chars) {
            if (c == cToFind) {
                return true;
            }
        }

        return false;
    }

    public static String[] columnSplit(String columnString, String separator) {
        if (columnString == null || columnString.length() == 0) {
            return new String[0];
        }
        return quoteAwareSplit(columnString, new String[]{separator}, false);
    }
}

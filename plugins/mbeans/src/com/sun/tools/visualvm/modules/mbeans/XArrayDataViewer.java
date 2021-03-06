/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.visualvm.modules.mbeans;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

class XArrayDataViewer {

    private XArrayDataViewer() {}

    public static boolean isViewableValue(Object value) {
        return Utils.canBeRenderedAsArray(value);
    }

    public static Component loadArray(Object value) {
        Component comp = null;
        if (isViewableValue(value)) {
            Object[] arr;
            if (value instanceof Collection) {
                arr = ((Collection) value).toArray();
            } else if (value instanceof Map) {
                arr = ((Map) value).entrySet().toArray();
            } else if (value instanceof Object[]) {
                arr = (Object[]) value;
            } else {
                int length = Array.getLength(value);
                arr = new Object[length];
                for (int i = 0; i < length; i++) {
                    arr[i] = Array.get(value, i);
                }
            }
            JEditorPane arrayEditor = new JEditorPane();
            arrayEditor.setContentType("text/html"); // NOI18N
            arrayEditor.setEditable(false);
            Color evenRowColor = arrayEditor.getBackground();
            int red = evenRowColor.getRed();
            int green = evenRowColor.getGreen();
            int blue = evenRowColor.getBlue();
            String evenRowColorStr =
                    "rgb(" + red + "," + green + "," + blue + ")"; // NOI18N
            Color oddRowColor = new Color(
                    red < 20 ? red + 20 : red - 20,
                    green < 20 ? green + 20 : green - 20,
                    blue < 20 ? blue + 20 : blue - 20);
            String oddRowColorStr =
                    "rgb(" + oddRowColor.getRed() + "," + // NOI18N
                    oddRowColor.getGreen() + "," + // NOI18N
                    oddRowColor.getBlue() + ")"; // NOI18N
            Color foreground = arrayEditor.getForeground();
            String textColor = String.format("%06x", // NOI18N
                                             foreground.getRGB() & 0xFFFFFF);
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body text=#"+textColor+"><table width=\"100%\">"); // NOI18N
            for (int i = 0; i < arr.length; i++) {
                if (i % 2 == 0) {
                    sb.append("<tr style=\"background-color: " + // NOI18N
                            evenRowColorStr + "\"><td><pre>" + // NOI18N
                            (arr[i] == null ?
                                arr[i] : htmlize(arr[i].toString())) +
                            "</pre></td></tr>"); // NOI18N
                } else {
                    sb.append("<tr style=\"background-color: " + // NOI18N
                            oddRowColorStr + "\"><td><pre>" + // NOI18N
                            (arr[i] == null ?
                                arr[i] : htmlize(arr[i].toString())) +
                            "</pre></td></tr>"); // NOI18N
                }
            }
            if (arr.length == 0) {
                sb.append("<tr style=\"background-color: " + // NOI18N
                        evenRowColorStr + "\"><td></td></tr>"); // NOI18N
            }
            sb.append("</table></body></html>"); // NOI18N
            arrayEditor.setText(sb.toString());
            JScrollPane scrollp = new JScrollPane(arrayEditor);
            comp = scrollp;
        }
        return comp;
    }

    private static String htmlize(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;"); // NOI18N
    }
}

/*
 * Copyright 2007-2010 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.visualvm.modules.tracer.impl.timeline;

import com.sun.tools.visualvm.modules.tracer.impl.swing.ColorIcon;
import com.sun.tools.visualvm.modules.tracer.impl.swing.LabelRenderer;
import com.sun.tools.visualvm.modules.tracer.impl.swing.LegendFont;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import org.netbeans.lib.profiler.charts.ChartContext;
import org.netbeans.lib.profiler.charts.ChartOverlay;
import org.netbeans.lib.profiler.charts.swing.Utils;
import org.netbeans.lib.profiler.charts.xy.synchronous.SynchronousXYItemsModel;

/**
 *
 * @author Jiri Sedlacek
 */
final class TimelineUnitsOverlay extends ChartOverlay {

    private final TimelineChart chart;
    private final LabelRenderer painter;

    private Model model;


    TimelineUnitsOverlay(TimelineChart chart) {
        this.chart = chart;

        painter = new LabelRenderer();
        painter.setFont(new LegendFont());

        int size = painter.getFont().getSize() - 3;
        ColorIcon.setup(size, size,
                LegendFont.FOREGROUND_COLOR, LegendFont.BACKGROUND_COLOR);
    }


    void setupModel(Model model) {
        this.model = model;
    }


    private boolean hasValues() {
        return ((SynchronousXYItemsModel)chart.getItemsModel()).getTimeline().
                getTimestampsCount() > 0;
    }

    private void setupPainter(String text, Color color) {
        painter.setText(text);
        painter.setIcon(color == null ? null : ColorIcon.fromColor(color));
    }


    public void paint(Graphics g) {
        if (model == null || !hasValues()) return;

        int w = getWidth();
        model.prefetch();
        int rowsCount = chart.getRowsCount();

        for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
            TimelineChart.Row row = chart.getRow(rowIndex);
            ChartContext rowContext = row.getContext();

            int y = Utils.checkedInt(rowContext.getViewportOffsetY());
            int h = rowContext.getViewportHeight();

            Color[] colors = model.getColors(row);

            int xx = w - 2;
            int yy = y;
            for (int itemIndex = colors.length - 1; itemIndex >= 0; itemIndex--) {
                setupPainter(model.getMaxUnits(row)[itemIndex], colors[itemIndex]);
                xx -= painter.getPreferredSize().width;
                paint(g, xx, yy);
                xx -= 10;
            }

            xx = w - 2;
            yy = -1;
            for (int itemIndex = colors.length - 1; itemIndex >= 0; itemIndex--) {
                setupPainter(model.getMinUnits(row)[itemIndex], colors[itemIndex]);
                Dimension pd = painter.getPreferredSize();
                xx -= pd.width;
                if (yy == -1) yy = y + h - pd.height - 1;
                paint(g, xx, yy);
                xx -= 10;
            }
        }
    }

    private void paint(Graphics g, int x, int y) {
        painter.setLocation(x, y + 1);
        painter.setForeground(LegendFont.BACKGROUND_COLOR);
        painter.paint(g);

        painter.setLocation(x, y);
        painter.setForeground(LegendFont.FOREGROUND_COLOR);
        if (painter.getIcon() != null)
            painter.setIcon(ColorIcon.BOTTOM_SHADOW);
        painter.paint(g);
    }


    // --- Peformance tweaks ---------------------------------------------------

    public void invalidate() {}

    public void update(Graphics g) {}


    // --- Model definition ----------------------------------------------------

    static interface Model {

        public void prefetch();
        public Color[]  getColors(TimelineChart.Row row);
        public String[] getMinUnits(TimelineChart.Row row);
        public String[] getMaxUnits(TimelineChart.Row row);

    }

}

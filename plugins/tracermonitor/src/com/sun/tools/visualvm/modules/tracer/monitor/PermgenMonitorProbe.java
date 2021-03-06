/*
 *  Copyright 2007-2010 Sun Microsystems, Inc.  All Rights Reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *  This code is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License version 2 only, as
 *  published by the Free Software Foundation.  Sun designates this
 *  particular file as subject to the "Classpath" exception as provided
 *  by Sun in the LICENSE file that accompanied this code.
 *
 *  This code is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  version 2 for more details (a copy is included in the LICENSE file that
 *  accompanied this code).
 *
 *  You should have received a copy of the GNU General Public License version
 *  2 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 *  CA 95054 USA or visit www.sun.com if you need additional information or
 *  have any questions.
 */

package com.sun.tools.visualvm.modules.tracer.monitor;

import com.sun.tools.visualvm.application.jvm.Jvm;
import com.sun.tools.visualvm.application.jvm.MonitoredData;
import com.sun.tools.visualvm.modules.tracer.ItemValueFormatter;
import com.sun.tools.visualvm.modules.tracer.ProbeItemDescriptor;
import com.sun.tools.visualvm.modules.tracer.TracerProbeDescriptor;
import javax.swing.Icon;

/**
 *
 * @author Jiri Sedlacek
 */
class PermgenMonitorProbe extends MonitorProbe {
    
    private static final String NAME = "PermGen";
    private static final String DESCR = "Monitors allocated and used PermGen size.";
    private static final int POSITION = 30;


    PermgenMonitorProbe(MonitoredDataResolver resolver) {
        super(2, createItemDescriptors(), resolver);
    }


    long[] getValues(MonitoredData data) {
        return new long[] {
            data.getGenCapacity()[1],
            data.getGenUsed()[1]
        };
    }


    static final TracerProbeDescriptor createDescriptor(Icon icon, boolean available,
                                                        Jvm jvm) {
        return new TracerProbeDescriptor(NAME, DESCR, icon, POSITION, available &&
                                         jvm.isMemoryMonitoringSupported());
    }
    
    private static final ProbeItemDescriptor[] createItemDescriptors() {
        return new ProbeItemDescriptor[] {
            ProbeItemDescriptor.continuousLineFillItem("Size",
                    "Monitors current PermGen size", ItemValueFormatter.DEFAULT_BYTES,
                    1d, 0, 10 * 1024 * 1024),
            ProbeItemDescriptor.continuousLineFillItem("Used",
                    "Monitors currently used PermGen", ItemValueFormatter.DEFAULT_BYTES,
                    1d, 0, 10 * 1024 * 1024)
        };
    }

}

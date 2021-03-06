/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.visualvm.modules.extensions;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.tools.jvmstat.JvmstatModel;
import com.sun.tools.visualvm.tools.jvmstat.JvmJvmstatModel;
import com.sun.tools.visualvm.tools.jvmstat.MonitoredValue;

/**
 *
 * @author Tomas Hurka
 */
class ExtendedJvmJvmstatModel extends JvmJvmstatModel {
    private static final String PERM_GEN_PREFIX = "sun.gc.generation.2.";   // NOI18N

    ExtendedJvmJvmstatModel(Application app,JvmstatModel stat) {
        super(app,stat);
        initMonitoredValues();
    }

    private void initMonitoredValues() {
      loadedClasses = jvmstat.findMonitoredValueByName("java.cls.loadedClasses");   // NOI18N
      sharedLoadedClasses = jvmstat.findMonitoredValueByName("java.cls.sharedLoadedClasses");   // NOI18N
      sharedUnloadedClasses = jvmstat.findMonitoredValueByName("java.cls.sharedUnloadedClasses");   // NOI18N
      unloadedClasses = jvmstat.findMonitoredValueByName("java.cls.unloadedClasses");   // NOI18N
      threadsDaemon = jvmstat.findMonitoredValueByName("java.threads.daemon");  // NOI18N
      threadsLive = jvmstat.findMonitoredValueByName("java.threads.live");  // NOI18N
      threadsLivePeak = jvmstat.findMonitoredValueByName("java.threads.livePeak");  // NOI18N
      threadsStarted = jvmstat.findMonitoredValueByName("java.threads.started");    // NOI18N
      applicationTime = jvmstat.findMonitoredValueByName("sun.rt.applicationTime"); // NOI18N
      upTime = jvmstat.findMonitoredValueByName("sun.os.hrt.ticks");    // NOI18N
      MonitoredValue osFrequencyMon = jvmstat.findMonitoredValueByName("sun.os.hrt.frequency"); // NOI18N
      osFrequency = getLongValue(osFrequencyMon);
      genCapacity = jvmstat.findMonitoredValueByPattern("sun.gc.generation.[0-9]+.capacity");   // NOI18N
      genUsed = jvmstat.findMonitoredValueByPattern("sun.gc.generation.[0-9]+.space.[0-9]+.used");  // NOI18N
      genMaxCapacity=getGenerationSum(jvmstat.findMonitoredValueByPattern("sun.gc.generation.[0-9]+.maxCapacity")); // NOI18N
    }

    protected String getPermGenPrefix() {
        return PERM_GEN_PREFIX;
    }
    
}

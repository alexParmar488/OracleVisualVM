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

package net.java.visualvm.modules.glassfish;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.application.views.ApplicationViewsSupport;
import com.sun.tools.visualvm.core.ui.DataSourceViewPlugin;
import com.sun.tools.visualvm.core.ui.DataSourceViewPluginProvider;

/**
 * Provides a basic overview of a GlassFish application server
 * configuration and capabilities
 * @author Jaroslav Bachorik
 */
public class GlassFishOverviewPluginProvider extends DataSourceViewPluginProvider<Application> {
    private final static class InstanceProvider {
        public static final GlassFishOverviewPluginProvider INSTANCE = new GlassFishOverviewPluginProvider();
    }
    
    @Override
    protected DataSourceViewPlugin createPlugin(Application app) {
        return new GlassFishOverviewPlugin(app);
    }

    @Override
    protected boolean supportsPluginFor(Application app) {
        return (ApplicationTypeFactory.getApplicationTypeFor(app) instanceof GlassFishApplicationType);
    }

    public static void initialize() {
        ApplicationViewsSupport.sharedInstance().getOverviewView().
                registerPluginProvider(InstanceProvider.INSTANCE);
    }
    
    public static void shutdown() {
        ApplicationViewsSupport.sharedInstance().getOverviewView().
                unregisterPluginProvider(InstanceProvider.INSTANCE);
    }
}

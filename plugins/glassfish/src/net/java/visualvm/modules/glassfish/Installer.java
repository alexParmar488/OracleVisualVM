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

import net.java.visualvm.modules.glassfish.datasource.GlassFishApplicationProvider;
import net.java.visualvm.modules.glassfish.dataview.GlassFishApplicationViewProvider;
import net.java.visualvm.modules.glassfish.datasource.GlassFishModelProvider;
import net.java.visualvm.modules.glassfish.datasource.GlassFishDataSourceDescriptorProvider;
import net.java.visualvm.modules.glassfish.datasource.GlassFishServletProvider;
import net.java.visualvm.modules.glassfish.dataview.GlassFishServletViewProvider;
import net.java.visualvm.modules.glassfish.dataview.GlassFishWebModuleViewProvider;
import net.java.visualvm.modules.glassfish.jmx.GFJmxModelFactory;
import org.openide.modules.ModuleInstall;


/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {
    //~ Instance fields ----------------------------------------------------------------------------------------------------------

    private GlassFishApplicationTypeFactory factory;

    //~ Methods ------------------------------------------------------------------------------------------------------------------

    @Override
    public void restored() {
        GlassFishApplicationTypeFactory.initialize();        
        GlassFishApplicationViewProvider.initialize();
        GlassFishModelProvider.initialize();
        GlassFishServletProvider.initialize();
        GlassFishWebModuleViewProvider.initialize();
        GlassFishServletViewProvider.initialize();
        GlassFishOverviewPluginProvider.initialize();
        GlassFishDataSourceDescriptorProvider.initialize();
        GlassFishApplicationProvider.initialize();
    }

    @Override
    public void uninstalled() {
        GlassFishApplicationTypeFactory.shutdown();   
        GlassFishApplicationViewProvider.shutdown();
        GlassFishModelProvider.shutdown();
        GlassFishServletProvider.shutdown();
        GlassFishWebModuleViewProvider.shutdown();
        GlassFishServletViewProvider.shutdown();
        GlassFishOverviewPluginProvider.shutdown();
        GlassFishDataSourceDescriptorProvider.shutdown();
        GlassFishApplicationProvider.shutdown();
        super.uninstalled();
    }
}

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

package com.sun.tools.visualvm.core.model;

import com.sun.tools.visualvm.core.datasource.DataSource;

/**
 * Abstract class, which simplifies implementation of {@link ModelProvider}.
 * It uses class hierarchy to compute priority of providers.
 * @author Tomas Hurka
 */
public abstract class AbstractModelProvider<M extends Model,B extends DataSource> implements ModelProvider<M,B> {
    
    /**
     * {@inheritDoc}
     */ 
    public abstract M createModelFor(B app);
    
    public int priority() {
        int depth = 1;
        Class cls = getClass();
        
        for (;!cls.equals(AbstractModelProvider.class);cls=cls.getSuperclass()) {
            depth++;
        }
        return depth;
    }
}

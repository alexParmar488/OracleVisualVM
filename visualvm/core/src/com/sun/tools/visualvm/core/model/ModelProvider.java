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
 * This interface should be implemented if you want to extend ModelFactory 
 * with your own provider. Implementors of this ModelProvider inteface
 * should be registered with appropriate {@link ModelFactory} using
 * {@link ModelFactory#registerProvider(ModelProvider)}
 * @author Tomas Hurka
 */
public interface ModelProvider<M extends Model,B extends DataSource> {
    
    /**
     * This is factory method for creating new instance 
     * of Model for DataSource. Implementation of this method
     * should return <code>null</code> if this model provider 
     * cannot construct model for passed dataSource.
     * @param dataSource {@link DataSource} for which {@link Model} should be created
     * @return model subclass for dataSource 
     */ 
    M createModelFor(B dataSource);
    /**
     * Priority is used by {@link ModelFactory} to sort registered 
     * {@link ModelProvider}. Model provider with the highest priority
     * will be invoked first and so no, until non-null value 
     * is returned from {@link #createModelFor(DataSource )}
     * @return priority of this ModelProvider
     */
    int priority();
}

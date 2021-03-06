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

import com.sun.tools.visualvm.tools.jmx.CachedMBeanServerConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.*;
import javax.swing.Icon;

class XMBean {
    private final MBeansTab mbeansTab;
    private final ObjectName objectName;
    private Icon icon;
    private String text;
    private Boolean broadcaster;
    private final Object broadcasterLock = new Object();
    private MBeanInfo mbeanInfo;
    private final Object mbeanInfoLock = new Object();
    private final static Logger LOGGER = Logger.getLogger(XMBean.class.getName());
    
    public XMBean(ObjectName objectName, MBeansTab mbeansTab) {
        this.mbeansTab = mbeansTab;
        this.objectName = objectName;
        text = objectName.getKeyProperty("name"); // NOI18N
        if (text == null)
            text = objectName.getDomain();
        if (MBeanServerDelegate.DELEGATE_NAME.equals(objectName)) {
            icon = IconManager.MBEANSERVERDELEGATE;
        } else {
            icon = IconManager.MBEAN;
        }
    }
    
    MBeanServerConnection getMBeanServerConnection() {
        return mbeansTab.getMBeanServerConnection();
    }
    
    CachedMBeanServerConnection getCachedMBeanServerConnection() {
        return mbeansTab.getCachedMBeanServerConnection();
    }
    
    public Boolean isBroadcaster() {
        synchronized (broadcasterLock) {
            if (broadcaster == null) {
                try {
                    broadcaster = getMBeanServerConnection().isInstanceOf(
                            getObjectName(),
                            "javax.management.NotificationBroadcaster"); // NOI18N
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Couldn't check if MBean [" + // NOI18N
                            objectName + "] is a notification broadcaster", e); // NOI18N
                    return false;
                }
            }
            return broadcaster;
        }
    }
    
    public Object invoke(String operationName) throws Exception {
        Object result = getMBeanServerConnection().invoke(
                getObjectName(), operationName, new Object[0], new String[0]);
        return result;
    }
    
    public Object invoke(String operationName, Object params[], String sig[])
    throws Exception {
        Object result = getMBeanServerConnection().invoke(
                getObjectName(), operationName, params, sig);
        return result;
    }
    
    public void setAttribute(Attribute attribute)
    throws AttributeNotFoundException, InstanceNotFoundException,
            InvalidAttributeValueException, MBeanException,
            ReflectionException, IOException {
        getMBeanServerConnection().setAttribute(getObjectName(), attribute);
    }
    
    public Object getAttribute(String attributeName)
    throws AttributeNotFoundException, InstanceNotFoundException,
            MBeanException, ReflectionException, IOException {
        return getCachedMBeanServerConnection().getAttribute(
                getObjectName(), attributeName);
    }
    
    public AttributeList getAttributes(String attributeNames[])
    throws AttributeNotFoundException, InstanceNotFoundException,
            MBeanException, ReflectionException, IOException {
        return getCachedMBeanServerConnection().getAttributes(
                getObjectName(), attributeNames);
    }
    
    public AttributeList getAttributes(MBeanAttributeInfo attributeNames[])
    throws AttributeNotFoundException, InstanceNotFoundException,
            MBeanException, ReflectionException, IOException {
        String attributeString[] = new String[attributeNames.length];
        for (int i = 0; i < attributeNames.length; i++) {
            attributeString[i] = attributeNames[i].getName();
        }
        return getAttributes(attributeString);
    }
    
    public ObjectName getObjectName() {
        return objectName;
    }
    
    public MBeanInfo getMBeanInfo() throws InstanceNotFoundException,
            IntrospectionException, ReflectionException, IOException {
        synchronized (mbeanInfoLock) {
            if (mbeanInfo == null) {
                mbeanInfo = getMBeanServerConnection().getMBeanInfo(objectName);
            }
            return mbeanInfo;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof XMBean)) return false;
        XMBean that = (XMBean) obj;
        return getObjectName().equals(that.getObjectName());
    }

    @Override
    public int hashCode() {
        return (objectName == null ? 0 : objectName.hashCode());
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Icon getIcon() {
        return icon;
    }
    
    public void setIcon(Icon icon) {
        this.icon = icon;
    }
    
    @Override
    public String toString() {
        return getText();
    }
}

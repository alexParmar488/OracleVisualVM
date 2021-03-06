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

/*
 * NewApplicationTypeForm.java
 *
 * Created on Oct 22, 2008, 2:40:07 PM
 */
package com.sun.tools.visualvm.modules.customtype.ui;

import com.sun.tools.visualvm.modules.customtype.ApplicationType;
import com.sun.tools.visualvm.modules.customtype.actions.ValidationSupport;
import com.sun.tools.visualvm.modules.customtype.icons.ImageUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Jaroslav Bachorik
 */
public class ApplicationTypeForm extends javax.swing.JPanel {

    final private static String defaultName = "<" + "New Application Type" + ">";
    final private static String defaultDescription = "<" + "Put description here" + ">";
    final private static ImageIcon DEFAULT_ICON = new javax.swing.ImageIcon(ApplicationTypeForm.class.getResource("/com/sun/tools/visualvm/modules/customtype/ui/application.png")); // NOI18N
    private ApplicationType applicationType;
    private File iconFile = null;
    private boolean iconFileReset = false;
    private ValidationSupport validationSupport = new ValidationSupport() {

        @Override
        public boolean isValid() {
            return !appTypeName.getText().isEmpty();
        }
    };

    /** Creates new form NewApplicationTypeForm */
    public ApplicationTypeForm(ApplicationType appType) {
        initComponents();
        applicationType = appType;
        appTypeName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                validationSupport.updateValidity();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validationSupport.updateValidity();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validationSupport.updateValidity();
            }
        });
        loadData();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iconMenu = new javax.swing.JPopupMenu();
        resetIcon = new javax.swing.JMenuItem();
        nameLabel = new javax.swing.JLabel();
        mainClassLabel = new javax.swing.JLabel();
        urlLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        appTypeName = new javax.swing.JTextField();
        appTypeMainClass = new javax.swing.JTextField();
        appTypeUrl = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        appTypeDescription = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        appTypeIcon = new javax.swing.JButton();

        resetIcon.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.resetIcon.text")); // NOI18N
        resetIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetIconActionPerformed(evt);
            }
        });
        iconMenu.add(resetIcon);

        nameLabel.setDisplayedMnemonic('N');
        nameLabel.setLabelFor(appTypeName);
        nameLabel.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.nameLabel.text")); // NOI18N

        mainClassLabel.setDisplayedMnemonic('C');
        mainClassLabel.setLabelFor(appTypeMainClass);
        mainClassLabel.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.mainClassLabel.text")); // NOI18N

        urlLabel.setDisplayedMnemonic('U');
        urlLabel.setLabelFor(appTypeUrl);
        urlLabel.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.urlLabel.text")); // NOI18N

        jLabel4.setDisplayedMnemonic('I');
        jLabel4.setLabelFor(appTypeIcon);
        jLabel4.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.jLabel4.text")); // NOI18N

        appTypeName.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.appTypeName.text")); // NOI18N
        appTypeName.setFocusCycleRoot(true);
        appTypeName.setNextFocusableComponent(appTypeIcon);
        appTypeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                appTypeNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                appTypeNameFocusLost(evt);
            }
        });
        appTypeName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                appTypeNameKeyTyped(evt);
            }
        });

        appTypeMainClass.setEditable(false);
        appTypeMainClass.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.appTypeMainClass.text")); // NOI18N
        appTypeMainClass.setNextFocusableComponent(appTypeUrl);

        appTypeUrl.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.appTypeUrl.text")); // NOI18N
        appTypeUrl.setNextFocusableComponent(appTypeDescription);
        appTypeUrl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                appTypeUrlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                appTypeUrlFocusLost(evt);
            }
        });

        descriptionLabel.setDisplayedMnemonic('D');
        descriptionLabel.setLabelFor(appTypeDescription);
        descriptionLabel.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.descriptionLabel.text")); // NOI18N

        appTypeDescription.setColumns(20);
        appTypeDescription.setLineWrap(true);
        appTypeDescription.setRows(5);
        appTypeDescription.setTabSize(4);
        appTypeDescription.setWrapStyleWord(true);
        appTypeDescription.setNextFocusableComponent(appTypeName);
        appTypeDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                appTypeDescriptionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                appTypeDescriptionFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(appTypeDescription);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setOpaque(false);

        jTextPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextPane1.setContentType(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.jTextPane1.contentType")); // NOI18N
        jTextPane1.setEditable(false);
        jTextPane1.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.jTextPane1.text")); // NOI18N
        jScrollPane2.setViewportView(jTextPane1);

        appTypeIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/tools/visualvm/modules/customtype/ui/application.png"))); // NOI18N
        appTypeIcon.setText(org.openide.util.NbBundle.getMessage(ApplicationTypeForm.class, "ApplicationTypeForm.appTypeIcon.text")); // NOI18N
        appTypeIcon.setComponentPopupMenu(iconMenu);
        appTypeIcon.setHideActionText(true);
        appTypeIcon.setNextFocusableComponent(appTypeMainClass);
        appTypeIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appTypeIconActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(mainClassLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(urlLabel)
                            .addComponent(descriptionLabel)
                            .addComponent(nameLabel))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(appTypeName, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(appTypeIcon))
                            .addComponent(appTypeUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .addComponent(appTypeMainClass, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameLabel)
                            .addComponent(appTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(appTypeIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mainClassLabel)
                    .addComponent(appTypeMainClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(urlLabel)
                    .addComponent(appTypeUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void appTypeNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_appTypeNameFocusGained
        appTypeName.setSelectionStart(0);
        appTypeName.setSelectionEnd(appTypeName.getText().length());
    }//GEN-LAST:event_appTypeNameFocusGained

    private void appTypeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_appTypeNameFocusLost
        appTypeName.setSelectionEnd(0);
    }//GEN-LAST:event_appTypeNameFocusLost

    private void appTypeDescriptionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_appTypeDescriptionFocusGained
        appTypeDescription.setSelectionStart(0);
        appTypeDescription.setSelectionEnd(appTypeDescription.getText().length());
    }//GEN-LAST:event_appTypeDescriptionFocusGained

    private void appTypeDescriptionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_appTypeDescriptionFocusLost
        appTypeDescription.setSelectionEnd(0);
    }//GEN-LAST:event_appTypeDescriptionFocusLost

    private void appTypeUrlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_appTypeUrlFocusGained
        appTypeUrl.setSelectionStart(0);
        appTypeUrl.setSelectionEnd(appTypeUrl.getText().length());
    }//GEN-LAST:event_appTypeUrlFocusGained

    private void appTypeUrlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_appTypeUrlFocusLost
        appTypeUrl.setSelectionEnd(0);
    }//GEN-LAST:event_appTypeUrlFocusLost

    private void appTypeNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_appTypeNameKeyTyped
        validationSupport.updateValidity();
    }//GEN-LAST:event_appTypeNameKeyTyped

    private void appTypeIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appTypeIconActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return (f.isDirectory() || f.getName().endsWith("png") || f.getName().endsWith("jpg") || f.getName().endsWith("jpeg") || f.getName().endsWith("gif"));
            }

            @Override
            public String getDescription() {
                return "Icon files";
            }
        });
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            iconFile = jfc.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(iconFile);
                appTypeIcon.setIcon(new ImageIcon(ImageUtils.resizeImage(img, 16, 16)));
                iconFileReset = false;
            } catch (IOException iOException) {
            }
        }
}//GEN-LAST:event_appTypeIconActionPerformed

    private void resetIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetIconActionPerformed
        iconFile = null;
        appTypeIcon.setIcon(DEFAULT_ICON);
        iconFileReset = true;
    }//GEN-LAST:event_resetIconActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea appTypeDescription;
    private javax.swing.JButton appTypeIcon;
    private javax.swing.JTextField appTypeMainClass;
    private javax.swing.JTextField appTypeName;
    private javax.swing.JTextField appTypeUrl;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JPopupMenu iconMenu;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel mainClassLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JMenuItem resetIcon;
    private javax.swing.JLabel urlLabel;
    // End of variables declaration//GEN-END:variables

    private void loadData() {
        iconFileReset = false;
        appTypeName.setText(applicationType.getName() == null || applicationType.getName().isEmpty() ? defaultName : applicationType.getName());
        appTypeMainClass.setText(applicationType.getMainClass());
        appTypeUrl.setText(applicationType.getInfoURL() != null ? applicationType.getInfoURL().toString() : "");
        appTypeDescription.setText((applicationType.getDescription() != null && !applicationType.getDescription().isEmpty()) ? applicationType.getDescription() : defaultDescription);
        if (applicationType.getIconURL() != null) {
            BufferedImage iconImage = null;
            try {
                iconImage = ImageIO.read(applicationType.getIconURL());
            } catch (IOException e) {
            }
            if (iconImage != null) {
                iconImage = ImageUtils.resizeImage(iconImage, 16, 16);
            }
            appTypeIcon.setIcon(new ImageIcon(iconImage));
        } else {
            appTypeIcon.setIcon(DEFAULT_ICON);
        }
        appTypeMainClass.setEditable(appTypeMainClass.getText().isEmpty());
    }

    public boolean storeData() {
        boolean result = true;
        if (appTypeName.getText().isEmpty() || appTypeName.getText().equals(defaultName)) {
            nameLabel.setForeground(Color.RED);
            result = false;
        } else {
            nameLabel.setForeground(Color.BLACK);
        }
        URL infoUrl = null;
        if (!appTypeUrl.getText().isEmpty()) {
            try {
                infoUrl = new URL(appTypeUrl.getText());
                urlLabel.setForeground(Color.BLACK);
            } catch (MalformedURLException e) {
                urlLabel.setForeground(Color.RED);
                result = false;
            }
        } else {
            urlLabel.setForeground(Color.BLACK);
        }

        if (result) {
            applicationType.setName(appTypeName.getText());
            applicationType.setMainClass(appTypeMainClass.getText());
            applicationType.setDescription(appTypeDescription.getText().equals(defaultDescription) ? "" : appTypeDescription.getText());
            if (iconFileReset) {
                applicationType.setIconURL(null);
            } else if (iconFile != null) {
                try {
                    applicationType.setIconURL(iconFile.toURI().toURL());
                } catch (IOException e) {
                }
            }
            applicationType.setInfoUrl(infoUrl);
        }
        return result;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}

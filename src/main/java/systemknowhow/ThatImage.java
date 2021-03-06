/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemknowhow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;




/**
 *
 * @author mkfs
 */
public class ThatImage extends javax.swing.JPanel {

    /**
     * Creates new form ThatImage
     */
    JFileChooser jfc;
    String baseDir="baseDir/";
    public static String baseDirChars="baseDir/Chars";
    public static String importDir="baseDir/Imports";
    public static String baseDirLinkedResources="baseDir/LinkedResources/";
    
    public ThatImage(String WebServerName,String LicenseKey, JSONObject SysInfo) {
       jfc=new JFileChooser();
        initComponents();
        rectBound = new int[2][2];
        
        File file = new File(baseDir);
        file.mkdirs();
        file = new File(baseDirChars);
        file.mkdirs();
        file = new File("baseDir/Links");
        file.mkdirs();
        file = new File(baseDirLinkedResources);
        file.mkdirs();
        file = new File("baseDir/Imports");
        file.mkdirs();
        
        TheCharLibrary.load();
    }
    public ThatImage() {
        jfc=new JFileChooser();
        initComponents();
        rectBound = new int[2][2];
        
        File file = new File(baseDir);
        file.mkdirs();
        file = new File(baseDirChars);
        file.mkdirs();
        file = new File("baseDir/Links");
        file.mkdirs();
        file = new File(baseDirLinkedResources);
        file.mkdirs();
        file = new File("baseDir/Imports");
        file.mkdirs();
        
        TheCharLibrary.load();
    }
    File[] files;
    String CurrentSelection="_";
    String CurrentSelectionPath="_";
    int[][] rectBound;
    boolean Point1Selected = false;
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        EditView = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        ActiveProfile = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CharList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(51, 51, 51));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setOpaque(false);

        jLabel2.setText("What is this about?");

        jButton3.setText("Get Question");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setText("Select Folder");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jTextField1.setText(" It is about finding the one.");

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setOpaque(false);

        EditView.setBackground(new java.awt.Color(0, 0, 0));
        EditView.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        EditView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        EditView.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        EditView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EditViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(EditView);

        jButton8.setText("Steal Files");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Steal This");
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                .addGap(28, 28, 28))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addGap(0, 147, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton6.setBackground(new java.awt.Color(0, 0, 0));
        jButton6.setForeground(new java.awt.Color(153, 255, 0));
        jButton6.setText("Reload");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Relate");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton2.setText("Get Next Image");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("Save A Character");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel2.setOpaque(false);

        jScrollPane2.setOpaque(false);

        CharList.setModel(TheCharLibrary.listModel);
        CharList.setOpaque(false);
        CharList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CharListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(CharList);

        jList1.setModel(TheCharLibrary.TheActiveChar);
        jList1.setOpaque(false);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jList1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ActiveProfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ActiveProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton5)
                        .addComponent(jButton7)
                        .addComponent(jButton4))
                    .addComponent(jButton6)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
       jfc.setMultiSelectionEnabled(true);
        jfc.showOpenDialog(this);
        files=jfc.getSelectedFiles();
        ImageSelCounter=0;
    }//GEN-LAST:event_jButton1ActionPerformed
int ImageSelCounter=0;
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        jButton9.setEnabled(true);
        try {
            if(files!=null)
                if(files.length>0)
                {
                    try {
            QnASession.writeToDisc("baseDir/Links/"+Tools.getMD5(CurrentSelectionPath)+CurrentSelection);
            
        }catch(Exception ex){}
        
                    int sel=ImageSelCounter;//(int)(files.length*Math.random());
                    ImageSelCounter=ImageSelCounter<files.length?ImageSelCounter+1:0;
                    
                    CurrentSelection=files[sel].getName();
                    CurrentSelectionPath=files[sel].getCanonicalPath();
                    
                    

                    EditView.setIcon(new ImageIcon(Tools.getScaledImage(ImageIO.read(new File(CurrentSelectionPath)), jScrollPane1.getWidth(), jScrollPane1.getHeight())));
                    ActiveProfile.setIcon(new ImageIcon(Tools.getScaledImage(ImageIO.read(new File(CurrentSelectionPath)), 250, 250)));
                    questionsCounter=0;
                    
                    
                    QnASession.loadToSession("baseDir/Links/"+Tools.getMD5(CurrentSelectionPath)+CurrentSelection);
                    QnASession.printSession(CurrentSelection);
                    jLabel2.setText(QuestionFramework.RankedQuestions.get(new Double(questionsCounter)));
                    jTextField1.setText(QnASession.Session.get(jLabel2.getText()))
                    
                   ;
                }
        }catch(IOException ex){}catch(Exception ex){}
        
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    int questionsCounter=0;
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        if(questionsCounter<QuestionFramework.RankedQuestions.size())
        { 
            QnASession.Session.put(jLabel2.getText(),jTextField1.getText());
            jLabel2.setText(QuestionFramework.RankedQuestions.get(new Double(questionsCounter)));
            
            if(QnASession.Session.containsKey(jLabel2.getText())){
                jTextField1.setText(QnASession.Session.get(jLabel2.getText()));
            }else{
                jTextField1.setText("");
            }
            
            questionsCounter++;
        }
        else{
            QnASession.Session.put(jLabel2.getText(),jTextField1.getText());
            jLabel2.setText("I am done with questions!");
        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       
        try {
            QnASession.writeToDisc("baseDir/Links/"+Tools.getMD5(CurrentSelectionPath)+CurrentSelection);
            
        }catch(Exception ex){}
        
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        
         try {
        BufferedImage image = Tools.PixelInformation.getSubimage(Tools.getScaledImage(ImageIO.read(new File(CurrentSelectionPath)), jScrollPane1.getWidth(), jScrollPane1.getHeight()), rectBound[0][0], rectBound[0][1], rectBound[1][0] - rectBound[0][0], rectBound[1][1] - rectBound[0][1]);

        Tools.dumpImageToFile(image, "baseDir/Chars/"+JOptionPane.showInputDialog("Name:"), new File(CurrentSelectionPath).getName());
        JOptionPane.showMessageDialog(new Frame(), "", "Source Image", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(image));
    } catch (IOException ex) {
        Logger.getLogger(ThatImage.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void EditViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditViewMouseClicked
       
        if (!Point1Selected) {
            rectBound[0][0] = evt.getX();
            rectBound[0][1] = evt.getY();
            JOptionPane.showMessageDialog(new Frame(), evt.getX() + "-" + evt.getY(), "First Point", JOptionPane.INFORMATION_MESSAGE, null);
            Point1Selected = true;
        } else {
            JOptionPane.showMessageDialog(new Frame(), evt.getX() + "-" + evt.getY(), "Second Point", JOptionPane.INFORMATION_MESSAGE, null);
            rectBound[1][0] = evt.getX();
            rectBound[1][1] = evt.getY();
            Point1Selected = false;
        }
        
        
    }//GEN-LAST:event_EditViewMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       TheCharLibrary.load();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       
        try {
            File file = new File(CurrentSelectionPath);
            String newPath = baseDirLinkedResources + Tools.getMD5(CurrentSelectionPath)+ CurrentSelection;

            File charImgFile = TheCharLibrary.listOfValidFiles[CharList.getSelectedIndex()];

            CharSession charSession = TheCharLibrary.getLinkedResources(charImgFile.getCanonicalPath());
            
            if(CurrentSelectionPath.contains("Imports")&CurrentSelectionPath.contains("baseDir")){
                charSession.addResource(CurrentSelectionPath);
            }else
            { charSession.addResource(Tools.moveFile(file, newPath, ".png"));}
            
            TheCharLibrary.writeToCharSession(charSession);
        } catch (IOException ex) {
            Logger.getLogger(ThatImage.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }//GEN-LAST:event_jButton7ActionPerformed

    private void CharListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CharListMouseClicked
       //set image to ActiveProfile
        try {
            File charImgFile = TheCharLibrary.listOfValidFiles[CharList.getSelectedIndex()];
            CharSession charSession = TheCharLibrary.getLinkedResources(charImgFile.getCanonicalPath());
            TheCharLibrary.loadTheActiveChar(charSession);
            ActiveProfile.setIcon(new ImageIcon(Tools.getScaledImage(ImageIO.read(TheCharLibrary.listOfValidFiles[CharList.getSelectedIndex()]), 250, 250)));
        } catch (IOException ex) {
            Logger.getLogger(ThatImage.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }//GEN-LAST:event_CharListMouseClicked

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        try {
            // jList1.getSelectedIndex()

            EditView.setIcon(new ImageIcon(Tools.getScaledImage(ImageIO.read(new File(TheCharLibrary.TheActiveCharLinks[jList1.getSelectedIndex()])), jScrollPane1.getWidth(), jScrollPane1.getHeight())));
        } catch (IOException ex) {
            Logger.getLogger(ThatImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//EditView.setIcon(new ImageIcon(TheCharLibrary.TheActiveCharLinks[jList1.getSelectedIndex()]));
        repaint();
        //EditView
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(true);
        jfc.showOpenDialog(this);
        files=jfc.getSelectedFiles();
        
        
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.showOpenDialog(this);
        
        Tools.moveFiles(files, jfc.getSelectedFile().getAbsolutePath());
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.showOpenDialog(this);
        File file=new File(CurrentSelectionPath);
        Tools.moveFiles(new File[]{file}, jfc.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_jButton9ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActiveProfile;
    private javax.swing.JList CharList;
    private javax.swing.JLabel EditView;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

      @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = Color.ORANGE;
        Color color2 = Color.RED;
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        
        try {
     
      BufferedImage img = null;
      
      
try {
    img = ImageIO.read(new File("G:\\PROJECTS\\SystemKnowHow\\tattoo14.png"));
} catch (IOException e) {
}
     // in.close();
      g2d.drawImage(img, getWidth()-210, 0, 200, 200, null);
      
      Ellipse2D ellipse = new Ellipse2D.Float();
      ellipse.setFrame(getWidth()/2-200, getHeight()/2-200, 400, 400);
      
       //g2d.clip(ellipse);
      if(jList1.getSelectedIndex()>-1){
            img = ImageIO.read(new File(TheCharLibrary.TheActiveCharLinks[jList1.getSelectedIndex()]));
        }
      
      g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
      
    } catch (Exception e) {
      System.out.print(e);
    }
        
        
        
        
    }
    
public static void main(String... args){
    
      /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThatImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThatImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThatImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThatImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    JFrame jf=new JFrame();
    jf.add(new ThatImage());
    jf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    jf.setVisible(true);
    //JOptionPane.showMessageDialog(null, new ThatImage());
}
}

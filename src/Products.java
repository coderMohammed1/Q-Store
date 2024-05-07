/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Zord
 */



import java.math.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import java.lang.*;

import java.sql.*;

import javax.swing.*;
import javax.swing.border.Border;

import javax.swing.event.*;
import javax.print.DocFlavor.STRING;
import javax.xml.crypto.Data;

class Product{
    int id;
    String pname;
    
    double price;
    String man;
    String seller;
    
    public Product(int id, String pname, double price, String man, String seller) {
        this.id = id;
        this.pname = pname;
        this.price = price;
        this.man = man; // manufacturer name ex:Dell
        this.seller = seller;
    }
    
    public Product() { }
    
}

class Pquery{
    static final String URL = "jdbc:derby:oshop";
    static final String USERNAME = "app";

    static  final String PASSWORD = "";
    static Connection connect;

    PreparedStatement query;

    Pquery(){
         try{
        connect = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        query = connect.prepareStatement("SELECT PRODUCT.ID ,PRODUCT.P_NAME,PRODUCT.PRICE,PRODUCT.MANUFACTURER,USERS.FIRST_NAME"
                + " FROM PRODUCT JOIN USERS ON PRODUCT.SELLER = USERS.ID ORDER BY PRODUCT.ID DESC FETCH FIRST 30 ROWS ONLY" ); // Deafult query
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
       
    }
    
    ResultSet res(int ind,String setter){
        ResultSet res2 = null;
        try{
            if(ind == 1){
            // to search
            query = connect.prepareStatement("SELECT PRODUCT.ID ,PRODUCT.P_NAME,PRODUCT.PRICE,PRODUCT.MANUFACTURER,USERS.FIRST_NAME "
                    + "FROM PRODUCT JOIN USERS ON PRODUCT.SELLER = USERS.ID WHERE P_NAME LIKE %?% OR MANUFACTURER LIKE %?% OR SELLER LIKE %?%"
                    + " ORDER BY PRODUCT.ID DESC FETCH FIRST 30 ROWS ONLY" );
            
                    query.setString(1,setter );
                    query.setString(2,setter );
                    query.setString(3,setter );
            }
      
             res2 = query.executeQuery();
        }catch(Exception err){
            err.printStackTrace();
            close();
            System.exit(4);
        }//finally{
            //close();
        //}
        return res2;
    }
    
    static void close(){
            try{
                  connect.close();
            }catch(Exception err)   {
                System.out.println("connection dose not want to close");
            }
        }
    
    
}

public class Products extends javax.swing.JFrame {

    /**
     * Creates new form Products
     */
    public static Products obg;
   JPanel mainPanel; // FlowLayout
    JPanel productsPanel; // GridLayout

    JButton cartButton;
    JLabel productName;
    
//    String[] productNames = {"apple", "orange", "car", "helicopter", "crystal", "building","goods","anything","anything2",
//        "anything3","anything4","anything5","anything6","anything7","anything8","anything9"
//        ,"anything10","anything11","anything12","anything13","anything14","anything15","anything16"};
    
    // main:
     public static ResultSet setop = null;
     public static ArrayList<Product> plist = new ArrayList<Product>();
    

    public Products() {
        super("Product Page");
        initComponents();
        initComponents2();
        setUpLayout();
        addProductsToPanel();
        getContentPane().setBackground(new Color(0xF0EBF8));
    }

    private void initComponents2() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        productsPanel = new JPanel();
    }

   private void setUpLayout() {
    setLayout(new FlowLayout());
    mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Add some space between components
    
    // Change the layout of productsPanel to GridLayout
    productsPanel.setLayout(new GridLayout(0, 4, 20, 20)); // 4 columns, space between cells
    
    Border border = BorderFactory.createEmptyBorder(45, 45, 45, 45); // Add padding around productsPanel
    productsPanel.setBorder(border);
    
    // Create a scroll pane with horizontal and vertical scrollbars as needed
    JScrollPane scrollPane = new JScrollPane(productsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(1500, 600)); // Set preferred size for scroll pane
    
    // Set a minimum width for each product panel
    int minWidth = 150; // Adjust as needed
    for (Component component : productsPanel.getComponents()) {
        if (component instanceof JPanel) {
            JPanel productPanel = (JPanel) component;
            productPanel.setMinimumSize(new Dimension(minWidth, productPanel.getMinimumSize().height));
        }
    }
    
    add(mainPanel);
    mainPanel.add(scrollPane);
}




    private void addProductsToPanel() {
        for (Product mypro : plist) {
            JPanel productPanel = createProductPanel(mypro.pname, mypro.price, mypro.seller);
            productsPanel.add(productPanel);
        }
        Pquery.close();
    }

    private JPanel createProductPanel(String productName, double price, String seller) {
        JPanel productPanel = new JPanel(new GridLayout(4, 1)); // 4 rows for product name, price, seller, and button
        productPanel.setBackground(Color.WHITE); // Set background color
        productPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // Add red border

        JLabel nameLabel = new JLabel(productName);
        JLabel priceLabel = new JLabel(String.valueOf(price) + " $");
        JLabel sellerLabel = new JLabel("Seller: " + seller);

        nameLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED))); // Add border between name and price
        priceLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED))); // Add border between price and seller
        sellerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to seller label

        nameLabel.setHorizontalAlignment(JLabel.CENTER); // Center-align the label
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        sellerLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton cartButton = new JButton("Add to Cart");
        cartButton.setAlignmentX(JButton.CENTER_ALIGNMENT); // Center-align the button

        productPanel.add(nameLabel);
        productPanel.add(priceLabel);
        productPanel.add(sellerLabel);
        productPanel.add(cartButton);

        productPanel.setPreferredSize(new Dimension(250, 150)); // Set panel size

        return productPanel;
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/LogoShop.jpeg"))); // NOI18N
        jLabel2.setText("Q Store");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel2.setIconTextGap(11);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/cart.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Sign out");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Order History");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(31, 31, 31)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54))
        );

        jButton4.setText("Search");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 702, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(54, 54, 54))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(641, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SignIn.cust = null;
        SignIn.sell = null;
        
        SignIn.obj.setVisible(false);
        obg.setVisible(false);
        SignIn.caller();
        //this is to siignout
    }//GEN-LAST:event_jButton2ActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void caller3(){
        Products.main(null);
    }
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        try{
        Pquery pres = new Pquery();
         setop = pres.res(0, ""); // so the deafult query will be executed!
            while (setop.next()) {
                
                Product prod = new Product(setop.getInt("ID"), setop.getString("P_NAME"), setop.getDouble("PRICE"),
                        setop.getString("MANUFACTURER"),setop.getString("FIRST_NAME"));
                
                plist.add(prod);
            }
        }catch(Exception prop){
            prop.printStackTrace();
        }
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               obg = new Products();
               obg.setVisible(true);
            }
        });
        
        // try to close() here
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

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
import java.security.PublicKey;

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
    PreparedStatement iscart;
    
    PreparedStatement inscart;
    PreparedStatement insp;
    PreparedStatement tot;
    
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
            // to search pls use this query by calling the method res with the parameters ind = 1 and setter = JtextFild1.getText();
            query = connect.prepareStatement("SELECT PRODUCT.ID, PRODUCT.P_NAME, PRODUCT.PRICE, PRODUCT.MANUFACTURER, USERS.FIRST_NAME "
        + "FROM PRODUCT JOIN USERS ON PRODUCT.SELLER = USERS.ID "
        + "WHERE PRODUCT.P_NAME LIKE ? " // Filter by P_NAME
        + "ORDER BY PRODUCT.ID DESC FETCH FIRST 30 ROWS ONLY"
            );
            query.setString(1, setter + '%');

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
    
    // take the id of the cart if it exisits
    ResultSet res3 = null;
    ResultSet cart(int id){
        try{
         iscart = connect.prepareStatement("SELECT ID FROM CART WHERE USER_ID = ? FETCH FIRST ROW ONLY");
         iscart.setInt(1,id);
         res3 = iscart.executeQuery();
        }catch(Exception sqerr){
            sqerr.printStackTrace();
            close();
            System.exit(5);
        }
        
        return  res3;
    }
    
    // adding new cart
    int newc = 0;
    int newcart(double total, int uid){
        try{
            inscart = connect.prepareStatement("INSERT INTO CART(TOTAL,USER_ID) VALUES(?,?)");
            inscart.setDouble(1, total);
            
            inscart.setInt(2, uid);
            newc = inscart.executeUpdate();
        }catch(Exception inse){
            inse.printStackTrace();
        }
        return newc;
    }
    
    // insert new product and link it with a cart
    int newp(int cartid,int proid){
        int newresp = 0;
        try{
            insp = connect.prepareStatement("INSERT INTO CART_PRODUCTS(CART,PRODUCT) VALUES(?,?)");
            insp.setInt(1, cartid);
            insp.setInt(2, proid);
            
            newresp = insp.executeUpdate();
        }catch(Exception ep){
            ep.printStackTrace();
        }
        
        return newresp;
    }
    
    // updates the total
    int total(int cartid, double price){
        int mytot = 0;
        try{
            tot = connect.prepareStatement("UPDATE CART SET TOTAL = TOTAL + ? WHERE ID = ?");
            tot.setDouble(1, price);
            
            tot.setInt(2, cartid);
            mytot = tot.executeUpdate();
        }catch(Exception cat){
            cat.printStackTrace();
        }
        
        return mytot;
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
    public static int id3 = -1;
    public double price2 = 0.0;
    public  static int cart_id = -2;
    
    private class Handler implements ActionListener{
        // add product btn
        @Override
        public void actionPerformed(ActionEvent cbtn) {
            
            JButton clicked = (JButton)cbtn.getSource();
            id3 = (int)clicked.getClientProperty("id");
            price2 = (double)clicked.getClientProperty("price");
            
            
            Pquery cartq = new Pquery();
             ResultSet thecart = null;
             
            if(SignIn.cust != null)
              thecart = cartq.cart(Integer.valueOf(SignIn.cust.id));
            else
                 thecart = cartq.cart(Integer.valueOf(SignIn.sell.id));
            
            int cres = 0;
            try{
                while (thecart.next()) {                
                    cart_id = thecart.getInt("ID");
                    cres ++;
                }
            }catch(Exception carte){
                carte.printStackTrace();
            }
            
            if(cres == 0){ // in case if we do not have a cart already
                Pquery insq =  new Pquery();
                 int pres = 0;
                if(SignIn.cust != null)
                  pres = insq.newcart(price2, Integer.valueOf(SignIn.cust.id));
                else
                    pres = insq.newcart(price2, Integer.valueOf(SignIn.sell.id));
                
                if(pres>0){
                    Pquery cartq2 = new Pquery();
                    ResultSet thecart2; 
                    
                    if(SignIn.cust != null)
                       thecart2 = cartq2.cart(Integer.valueOf(SignIn.cust.id));
                    else
                        thecart2 = cartq2.cart(Integer.valueOf(SignIn.sell.id));
                    
                    try{
                        while (thecart2.next()) {                
                            cart_id = thecart2.getInt("ID");
                        }
                    }catch(Exception carte2){
                        carte2.printStackTrace();
                    }
                    
                    int res5 = 0;
                    res5 = insq.newp(cart_id,id3);
                    
                    if(res5<0){
                        System.err.println("EROR#6");
                    }else{
                        JOptionPane.showMessageDialog(null, "your product has been added!","DONE!",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }else{
                // in case if we have a cart and we want to add a new product to it
                Pquery addpro = new Pquery();
                
                int res6 = 0;
                res6 = addpro.newp(cart_id,id3);
                
                if(res6>0){
                    res6 = addpro.total(cart_id,price2);
                }
                
                if(res6 <= 0){
                    System.err.println("ERORR#7");
                }else{
                    JOptionPane.showMessageDialog(null, "your product has been added!","DONE!",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            Pquery.close();
        }
        
    }
    
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
            JPanel productPanel = createProductPanel(mypro.pname, mypro.price, mypro.seller,mypro.id);
            productsPanel.add(productPanel);
        }
        Pquery.close();
    }

    private JPanel createProductPanel(String productName, double price, String seller,int id) {
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
        cartButton.putClientProperty("id", id); // so we can know which button was clicked!
        
        cartButton.putClientProperty("price", price);
        Handler handler = new Handler();
        cartButton.addActionListener(handler);
        
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
        jButton5 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(300, 100));

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

        jButton5.setText("ref");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jButton5)
                .addGap(17, 17, 17)
                .addComponent(jButton1)
                .addGap(19, 19, 19)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(54, 54, 54))
        );

        jButton4.setText("Search");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 888, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
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
                .addContainerGap(635, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Cart.plist2.clear();
        if(Cart.myobj != null)
            Cart.myobj.setVisible(false);
        Cart.cartCaller();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SignIn.cust = null;
        SignIn.sell = null;
        
        SignIn.obj.setVisible(false);
        obg.setVisible(false);
        
        if(Uploads.obg2 != null)
            Uploads.obg2.setVisible(false);
        
         if(Cart.myobj != null)
            Cart.myobj.setVisible(false);
        
        SignIn.caller();
        //this is to siignout
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       try{  
        if(jTextField1.getText().length() != 0){
            plist.clear();
        Pquery serachQuery  = new Pquery();
        setop = serachQuery.res(1 , jTextField1.getText());
        
    while(setop.next()){
                    Product sprod = new Product(setop.getInt("ID"), setop.getString("P_NAME"), setop.getDouble("PRICE"),
                        setop.getString("MANUFACTURER"),setop.getString("FIRST_NAME"));
                
                plist.add(sprod); 
                
        }
        productsPanel.removeAll();
        productsPanel.setVisible(false);
        addProductsToPanel();
        productsPanel.setVisible(true);
       }else{
         JOptionPane.showMessageDialog(productsPanel, "you cant search with empty string!","bruh what could you find",JOptionPane.ERROR_MESSAGE);
        }
    } catch(Exception prop){
        prop.printStackTrace();
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try{  
            plist.clear();
        Pquery serachQuery  = new Pquery();
        setop = serachQuery.res(0 , "");
        
    while(setop.next()){
                    Product sprod = new Product(setop.getInt("ID"), setop.getString("P_NAME"), setop.getDouble("PRICE"),
                        setop.getString("MANUFACTURER"),setop.getString("FIRST_NAME"));
                
                plist.add(sprod); 
                
        }
        productsPanel.removeAll();
        productsPanel.setVisible(false);
        addProductsToPanel();
        productsPanel.setVisible(true);
       
    } catch(Exception prop){
        prop.printStackTrace();
    }
    }//GEN-LAST:event_jButton5ActionPerformed
 
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
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Zord
 */




import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;


class Cartp{
    
    String pname;
    double price;
    
    int pid;
    
    
    public Cartp(String pname, double prie, int pid){
        this.pname = pname;
        this.price = prie;
        
        this.pid = pid;
    }
}

class queries{
    static final String URL = "jdbc:derby:oshop";
    static final String USERNAME = "app";

    static  final String PASSWORD = "";
    static Connection connect;

    PreparedStatement query;
    PreparedStatement del;
    PreparedStatement upd;
    
    public queries() {
         try{
            connect = DriverManager.getConnection(URL,USERNAME,PASSWORD);
         }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    ResultSet showCart(int uid){
        ResultSet res10 = null;
        try{ // somtime u have to compile it manually
            query = connect.prepareStatement("SELECT USERS.ID AS UID, P_NAME, PRICE, CART_PRODUCTS.PRODUCT AS PID2,"
                    + " CART_PRODUCTS.ID AS PID, CART_PRODUCTS.CART AS UCART, TOTAL FROM CART JOIN CART_PRODUCTS ON"
                    + " CART.ID = CART_PRODUCTS.CART JOIN PRODUCT ON PRODUCT.ID = CART_PRODUCTS.PRODUCT JOIN USERS"
                    + " ON USERS.ID = CART.USER_ID WHERE USERS.ID = ?");

            query.setInt(1, uid);
            res10 = query.executeQuery();
        }catch(Exception sqerr){
            sqerr.printStackTrace();
            close();
        }
        
        return res10;
    }
    
    int delete(int id,double price,int cart){
        int result = 0;
        try{
            del = connect.prepareStatement("DELETE FROM CART_PRODUCTS WHERE ID = ?");
            del.setInt(1, id);
            result = del.executeUpdate();
            
            upd = connect.prepareStatement("UPDATE CART SET TOTAL = TOTAL-? WHERE ID = ?");
            upd.setDouble(1, price);
            upd.setInt(2, cart);
            result = upd.executeUpdate();
            
        }catch(Exception sqlx){
            sqlx.printStackTrace();
            close();
        }
        return result;
    }
    
    static void close(){
            try{
                 connect.close();
            }catch(Exception err)   {
                System.out.println("connection dose not want to close");
            }
        }
    
    
}

public class Cart extends javax.swing.JFrame {

    /**
     * Creates new form Cart
     */
    
    public static Cart myobj;
            
    private class Del implements ActionListener{
        int id4 = 0;
        double price3 = 0.0;
        int cart_id = 0;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            JButton clicked = (JButton)e.getSource();
            id4 = (int)clicked.getClientProperty("id2");
            price3 = (double)clicked.getClientProperty("price2");
            cart_id = (int)clicked.getClientProperty("cartID"); 
            
            
            queries delp = new queries();
            delp.delete(id4, price3, cart_id);
            
            myobj.setVisible(false);
            plist2.clear();
            cartCaller();
        }
        
    }
    static double total;
    static int cid;
    
    static ResultSet mancart = null;
    public static ArrayList<Cartp> plist2 = new ArrayList<Cartp>();
    
   JPanel mainPanel; // FlowLayout
    JPanel productsPanel; // GridLayout

    JButton cartButton;
    JLabel productName;
//    String[] productNames = {"apple", "orange", "car", "helicopter", "crystal", "building","goods","anything","anything2",
//        "anything3","anything4"
//    };
//         double prices[] = new double[productNames.length];
        

    public Cart() {      
        super("cart Page");
        initComponents();
        initComponents2();
        
        setUpLayout();
        addProductsToPanel();
         getContentPane().setBackground(new Color(0xF9ECEA));
    }

    private void initComponents2() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        productsPanel = new JPanel();
    }

    private void setUpLayout() {
        setLayout(new FlowLayout());
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Add some space between components
        productsPanel.setLayout(new GridLayout(0, 8, 20, 20)); // 3 columns for products, space between cells
        Border border = BorderFactory.createEmptyBorder(40, 40, 40, 40); // Add padding around productsPanel
        productsPanel.setBorder(border);
        add(mainPanel);
        mainPanel.add(productsPanel);
    }

    private void addProductsToPanel() {
        int i = 0;
        for (Cartp prod : plist2) {
            JPanel productPanel = createProductPanel(prod.pname,prod.price,prod.pid,cid);
            productsPanel.add(productPanel);
            i++;
        }
        jLabel1.setText("total: " + total +"$");
        queries.close();
    }

    private JPanel createProductPanel(String productName,double price,int prid,int cart3) {
        JPanel productPanel = new JPanel(new GridLayout(3, 1)); // 3 rows for product name and cart button and price
        productPanel.setBackground(Color.WHITE); // Set background color
        productPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Add border for separation

        JLabel nameLabel = new JLabel(productName);
        JLabel priceL = new JLabel(String.valueOf(price)+" $");
        
        nameLabel.setHorizontalAlignment(JLabel.CENTER); // Center-align the label
        priceL.setHorizontalAlignment(JLabel.CENTER);
        JButton cartButton = new JButton("Delete from cart");
        
        cartButton.setAlignmentX(JButton.CENTER_ALIGNMENT); // Center-align the button
        cartButton.putClientProperty("id2", prid);
        cartButton.putClientProperty("price2", price);
        
        cartButton.putClientProperty("cartID", cart3);
        Del handler = new Del();
        cartButton.addActionListener(handler);
        
        productPanel.add(nameLabel);
        productPanel.add(priceL);
        productPanel.add(cartButton);
        

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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Check out");

        jLabel1.setText("Total: 500 $");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel1)))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jButton2.setText("Products");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("orders history");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/LogoShop.jpeg"))); // NOI18N
        jLabel2.setText("Q Store");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel2.setIconTextGap(11);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addGap(46, 46, 46)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 627, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Products.main(null);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    public static void cartCaller(){
        Cart.main(null);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        try{
            queries cartqc = new queries();
            mancart = cartqc.showCart(Integer.valueOf(SignIn.cust.id));
            
            while(mancart.next()){
                Cartp addit = new Cartp(mancart.getString("P_NAME"), mancart.getDouble("price"), mancart.getInt("PID"));
                plist2.add(addit);
                cid = mancart.getInt("UCART");
                total = mancart.getDouble("TOTAL");
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                myobj = new Cart();
                myobj.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}

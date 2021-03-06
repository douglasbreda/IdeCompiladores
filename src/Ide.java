
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dougl
 */
public class Ide extends javax.swing.JFrame {

    public Ide() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        ActionListener listener = (ActionEvent e) -> {
            Executar();
        };
    }
    
    public void Executar(){
        
            Lexico lex = new Lexico();
        
        Sintatico sin = new Sintatico();
        Semantico sem = new Semantico();
        
        lex.setInput(txtCodigo.getText());
        
        try{
            txtMensagens.setText("");
            sin.parse(lex, sem);
            txtMensagens.setForeground(new Color(34, 139, 34));
            txtMensagens.setText("Compilado com sucesso.");
        }
        catch (LexicalError e) {
            txtMensagens.setForeground(Color.red);
            txtMensagens.setText("Ocorreu um erro léxico na posição " + e.getPosition());
        }
        catch(SyntaticError e){
            txtMensagens.setForeground(Color.red);
            txtMensagens.setText("Ocorreu um erro sintático na posição " + e.getPosition());
        }
        catch(SemanticError e){
            txtMensagens.setForeground(Color.red);
            txtMensagens.setText("Ocorreu um erro semântico na posição" + e.getPosition());
        }
    }
    
    //Limpa os campos do formulário
    private void LimparCampos(){
        txtCodigo.setText("");
        txtCodigo.setText("static void main(){\n\n}");
        txtMensagens.setText("");
    }
    
    //Carrega um arquivo
    private void CarregarArquivo(){
        
           JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivos de texto", "txt");
                chooser.setFileFilter(filtro);
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                String filename = f.getAbsolutePath();
                
                try {
                    FileReader reader = new FileReader(filename);
                    BufferedReader br = new BufferedReader(reader);
                    txtCodigo.read(br, null);
                    br.close();
                    txtCodigo.requestFocus();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Arquivo Inválido!");
                }
    }
    
    //Salva os arquivos do usuário
    private void SalvarArquivo(){
        
          String text = txtCodigo.getText();
                JFileChooser chooser = new JFileChooser();
                chooser.showSaveDialog(null);
                try {
                    FileWriter escritor = new FileWriter(chooser.getSelectedFile() + ".txt");
                    escritor.write(text.toString());
                    escritor.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    
                }
                JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!");
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
        jScrollPane2 = new javax.swing.JScrollPane();
        txtCodigo = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMensagens = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ide Compiladores");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Código Fonte"));

        txtCodigo.setColumns(20);
        txtCodigo.setRows(5);
        txtCodigo.setText("static void main(){\n\n}");
        jScrollPane2.setViewportView(txtCodigo);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensagens"));

        txtMensagens.setEditable(false);
        txtMensagens.setColumns(20);
        txtMensagens.setRows(5);
        jScrollPane3.setViewportView(txtMensagens);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon("C:\\Users\\dougl\\Documents\\7º Período\\Compiladores\\IdeCompiladores\\img\\play_property_24px.png")); // NOI18N
        jButton1.setText("Executar");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon("C:\\Users\\dougl\\Documents\\7º Período\\Compiladores\\IdeCompiladores\\img\\stop_property_24px.png")); // NOI18N
        jButton2.setText("Parar");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon("C:\\Users\\dougl\\Documents\\7º Período\\Compiladores\\IdeCompiladores\\img\\remove_property_24px.png")); // NOI18N
        jButton3.setText("Limpar");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jMenu1.setText("Arquivo");

        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setText("Salvar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Exemplos");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem2.setText("Visualizar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Executar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.LimparCampos();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CarregarArquivo();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        SalvarArquivo();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
     
                
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Exemplos oExemplo = new Exemplos();
        oExemplo.setVisible(true);
        oExemplo.setExtendedState(MAXIMIZED_BOTH);
                
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
            java.util.logging.Logger.getLogger(Ide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ide().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea txtCodigo;
    private javax.swing.JTextArea txtMensagens;
    // End of variables declaration//GEN-END:variables
}

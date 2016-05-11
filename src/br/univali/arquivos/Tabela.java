/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.univali.arquivos;

import br.univali.classes.Simbolos;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author dougl
 */
public class Tabela extends javax.swing.JFrame {

    List<Simbolos> lstSimbolos = null;
    /**
     * Creates new form Tabela
     */
    public Tabela() {
        initComponents();
    }
    
    public Tabela(List<Simbolos> pListaSimbolos){
        initComponents();
        this.lstSimbolos = pListaSimbolos;
        CriarTabela();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 1));

        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Tabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tabela().setVisible(true);
            }
        });
    }
    
    ///Cria a tabela para exibição de simbolos
    private void CriarTabela()
    {
        Object[] colunas = {"Nome", "Tipo", "Escopo", "Inicializada", "Utilizada", "Parâmetros", "Vetor", "Função", "Modificador", "Posição"};
        
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        
        jTable1.setModel(modelo);
        
//        jTable1.setEnabled(false);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        for (Simbolos simbolo : lstSimbolos) {
            modelo.addRow(new Object[]{simbolo.Nome, simbolo.Tipo, simbolo.Escopo, simbolo.Inicializada, simbolo.Usada, simbolo.Parametros, simbolo.EhVetor, simbolo.EhFuncao, simbolo.Modificador, simbolo.Posicao});
        }
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

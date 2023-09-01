/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
             
        conn = new conectaDAO().connectDB();
        
        try {
                    String query = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, produto.getNome()); 
                    statement.setDouble(2, produto.getValor()); 
                    statement.setString(3, produto.getStatus()); 
                    statement.executeUpdate();
                    
                    JOptionPane.showMessageDialog(null, "Produto salvo: " + produto.getNome());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao salvar produto: " + produto.getNome());
                }
            }   
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        
        
}


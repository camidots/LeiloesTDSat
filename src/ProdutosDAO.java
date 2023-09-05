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
    ArrayList<ProdutosDTO> vendidos = new ArrayList<>();
    
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
        
        conn = new conectaDAO().connectDB();
        
        try {
                String query = "SELECT * FROM produtos"; 
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id"); 
                    String nome = resultSet.getString("nome");
                    double valor = resultSet.getInt("valor");
                    String status = resultSet.getString("status");

                    ProdutosDTO produto = new ProdutosDTO();
                    
                    produto.setId(id);
                    produto.setNome(nome);
                    produto.setValor(valor);
                    produto.setStatus(status);
                    
                    listagem.add(produto);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return listagem;
        } 
    
    
    public void venderProduto(int id) {
            try (Connection conn = new conectaDAO().connectDB()) {
                String selectQuery = "SELECT status FROM produtos WHERE id = ?";
                try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery)) {
                    selectStatement.setInt(1, id);
                    try (ResultSet resultSet = selectStatement.executeQuery()) {
                        if (resultSet.next()) {
                            String status = resultSet.getString("status");

                            if (status.equalsIgnoreCase("A Venda")) {
                                String updateQuery = "UPDATE produtos SET status = ? WHERE id = ?";
                                try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                                    String statusNovo = "Vendido";
                                    updateStatement.setString(1, statusNovo);
                                    updateStatement.setInt(2, id);
                                    int rowsUpdated = updateStatement.executeUpdate();

                                    if (rowsUpdated > 0) {
                                        JOptionPane.showMessageDialog(null, "Produto marcado como Vendido!");
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Falha ao atualizar o status do produto.");
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "O produto já foi vendido!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Produto não encontrado!");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos(){
        
        conn = new conectaDAO().connectDB();
        
        try {
                String query = "SELECT * FROM produtos WHERE status = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, "Vendido");
                
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id"); 
                    String nome = resultSet.getString("nome");
                    double valor = resultSet.getInt("valor");

                    ProdutosDTO produtoVendido = new ProdutosDTO();
                    
                    produtoVendido.setId(id);
                    produtoVendido.setNome(nome);
                    produtoVendido.setValor(valor);
                    
                    vendidos.add(produtoVendido);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        return vendidos;
    } 
}


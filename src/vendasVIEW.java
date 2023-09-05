
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class vendasVIEW extends JFrame {
    private JTextField buscaCampo;
    private JTable tabela;
    private JPanel painelSuperior, painelFiltro, painelTabela, painelBtn, painelLista, painelBotao1, painelBotao2;;
    private JLabel tituloLabel, filtroLabel, listaLabel;
    private JButton voltarBtn;
    private DefaultTableModel modelo;  
    
    public vendasVIEW(){
        
        setTitle("Vendas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        painelSuperior = new JPanel();
        painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));

        tituloLabel = new JLabel("Produtos Vendidos");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 36));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelSuperior.add(tituloLabel);

        painelLista = new JPanel();
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));

        listaLabel = new JLabel("Listagem");
        listaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        listaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelLista.add(listaLabel);

        painelSuperior.add(Box.createVerticalGlue());
        painelSuperior.add(painelLista);

        painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroLabel = new JLabel("Pesquisar podcast por produto: ");
        buscaCampo = new JTextField(20);
        buscaCampo.setPreferredSize(new Dimension(200, buscaCampo.getPreferredSize().height));
        painelFiltro.add(filtroLabel);
        painelFiltro.add(buscaCampo);

        painelSuperior.add(painelFiltro, BorderLayout.WEST);
        add(painelSuperior, BorderLayout.NORTH);

        painelTabela = new JPanel(new BorderLayout());

        modelo = new DefaultTableModel();
        tabela = new JTable(modelo);

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Valor R$");

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        tabela.getColumnModel().getColumn(0).setHeaderRenderer(centerRenderer); 
        tabela.getColumnModel().getColumn(1).setHeaderRenderer(centerRenderer); 
        tabela.getColumnModel().getColumn(2).setHeaderRenderer(centerRenderer); 
        
        int columnCount = tabela.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        
        painelTabela.add(scrollPane, BorderLayout.CENTER);
        add(painelTabela, BorderLayout.CENTER);

        voltarBtn = new JButton("Voltar");
        
        painelBtn = new JPanel(new BorderLayout());      
        painelBotao1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        painelBotao1.add(voltarBtn);
        
        painelBtn.add(painelBotao1, BorderLayout.EAST);
        
        add(painelBtn, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        
        voltarBtn.addActionListener((ActionEvent e) -> {
            listagemVIEW listagem = new listagemVIEW(); 
            listagem.setVisible(true);
            dispose();
        });
        
        buscaCampo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabela();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabela();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabela();
            }
        });
        setSize(550, 550); 
        listarProdutos();
    }
    
    private void listarProdutos(){
        try {
            ProdutosDAO produtosdao = new ProdutosDAO();
            modelo.setNumRows(0);

            ArrayList<ProdutosDTO> vendidos = produtosdao.listarProdutosVendidos();

            for(int i = 0; i < vendidos.size(); i++){
                modelo.addRow(new Object[]{
                  vendidos.get(i).getId(),
                  vendidos.get(i).getNome(),
                  vendidos.get(i).getValor(),
                });
            } 
        }catch (Exception e) {
            e.printStackTrace();
        }                           
    }
    private void filtrarTabela() {
        String filtro = buscaCampo.getText().trim().toLowerCase();

        DefaultTableModel novoModelo = new DefaultTableModel();

        for (int i = 0; i < modelo.getColumnCount(); i++) {
            novoModelo.addColumn(modelo.getColumnName(i));
        }

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String produtor = modelo.getValueAt(i, 1).toString().toLowerCase();
            if (produtor.contains(filtro)) {
                Object[] rowData = new Object[modelo.getColumnCount()];
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    rowData[j] = modelo.getValueAt(i, j);
                }
                novoModelo.addRow(rowData);
            }
        }
        tabela.setModel(novoModelo);
    }
}

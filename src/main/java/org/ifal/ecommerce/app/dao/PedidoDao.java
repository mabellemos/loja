package org.ifal.ecommerce.app.dao;

import org.ifal.ecommerce.domain.entity.*;
import org.ifal.ecommerce.domain.repository.db.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {

    public void save(Pedido pedido) {
        String sql = "INSERT INTO PEDIDO (CPF_CLIENTE_FK, CPF_FUNCIONARIO_FK, VALOR_TOTAL) VALUES (?,?,?);";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, pedido.getCliente().getCpf());
            pst.setString(2, pedido.getVendedor().getCpf());
            pst.setDouble(3, pedido.calcularTotal());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getInt(1));
            }

            for (Produto produto : pedido.getProdutos()) {
                String sqlProduto = "INSERT INTO ITEM_PEDIDO (ID_PEDIDO_FK, ID_PRODUTO_FK, QUANTIDADE) VALUES (?,?,?);";
                try (PreparedStatement pstProduto = connection.prepareStatement(sqlProduto)) {
                    pstProduto.setInt(1, pedido.getId());
                    pstProduto.setInt(2, produto.getId());
                    pstProduto.setInt(3, produto.getQuantidade());

                    pstProduto.executeUpdate();
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao salvar pedido: " + e.getMessage(), e);
        }
    }

    public List<ItemPedido> findAll() {
        String sql = "SELECT * FROM ITEM_PEDIDO;";
        List<ItemPedido> lista = new ArrayList<>();

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                int idPedido = rs.getInt("ID_PEDIDO_FK");
                int idProduto = rs.getInt("ID_PRODUTO_FK");
                int quantidade = rs.getInt("QUANTIDADE");
                double valor = rs.getDouble("VALOR");

                // Busca os objetos relacionados
                Pedido pedido = new PedidoDao().findById(idPedido);
                Produto produto = new ProdutoDao().findById(idProduto);

                if (pedido == null) {
                    System.out.println("Aviso: Pedido não encontrado para o ID: " + idPedido);
                    continue;
                }

                if (produto == null) {
                    System.out.println("Aviso: Produto não encontrado para o ID: " + idProduto);
                    continue;
                }

                // Criando objeto ItemPedido
                ItemPedido item = new ItemPedido(id, pedido, produto, quantidade, valor);
                lista.add(item);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar itens do pedido: " + e.getMessage(), e);
        }

        if (lista.isEmpty()) {
            System.out.println("Aviso: Nenhum item de pedido foi encontrado na base de dados.");
        }

        return lista;
    }


    public Pedido findById(int id) {
        String sql = "SELECT * FROM PEDIDO WHERE ID = ?;";
        Pedido pedido = null;

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String cpfVendedor = rs.getString("CPF_FUNCIONARIO_FK");
                String cpfCliente = rs.getString("CPF_CLIENTE_FK");

                Funcionario vendedor = new FuncionarioDao().findByCpf(cpfVendedor);
                Cliente cliente = new ClienteDao().findByCpf(cpfCliente);

                pedido = new Pedido(vendedor, cliente);
                pedido.setId(id);

                String sqlProduto = "SELECT * FROM ITEM_PEDIDO WHERE ID_PEDIDO_FK = ?;";
                try (PreparedStatement pstProduto = connection.prepareStatement(sqlProduto)) {
                    pstProduto.setInt(1, id);
                    ResultSet rsProduto = pstProduto.executeQuery();
                    while (rsProduto.next()) {
                        int idProduto = rsProduto.getInt("ID_PRODUTO_FK");
                        int quantidade = rsProduto.getInt("QUANTIDADE");

                        Produto produto = new ProdutoDao().findById(idProduto);
                        produto.setQuantidade(quantidade);
                        pedido.adicionarProduto(produto);
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por ID: " + e.getMessage(), e);
        }

        return pedido;
    }

    public void delete(int id) {
        String sql = "DELETE FROM PEDIDO WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("Nenhum pedido foi deletado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao deletar pedido: " + e.getMessage(), e);
        }
    }
}


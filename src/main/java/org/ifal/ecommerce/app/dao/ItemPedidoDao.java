package org.ifal.ecommerce.app.dao;

import org.ifal.ecommerce.app.dao.PedidoDao;
import org.ifal.ecommerce.app.dao.ProdutoDao;
import org.ifal.ecommerce.domain.entity.ItemPedido;
import org.ifal.ecommerce.domain.entity.Pedido;
import org.ifal.ecommerce.domain.entity.Produto;
import org.ifal.ecommerce.domain.repository.db.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDao {

    public List<ItemPedido> findAll() {
        String sql = "SELECT * FROM ITEM_PEDIDO;";
        List<ItemPedido> lista = new ArrayList<>();

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                ItemPedido item = mapResultSetToItemPedido(rs, connection);
                if (item != null) {
                    lista.add(item);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar itens do pedido: " + e.getMessage(), e);
        }

        return lista;
    }

    public ItemPedido findById(int id) {
        String sql = "SELECT * FROM ITEM_PEDIDO WHERE ID = ?;";
        ItemPedido item = null;

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    item = mapResultSetToItemPedido(rs, connection);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar item do pedido por ID: " + e.getMessage(), e);
        }

        return item;
    }

    public void save(ItemPedido item) {
        String sql = "INSERT INTO ITEM_PEDIDO (ID_PEDIDO_FK, ID_PRODUTO_FK, QUANTIDADE, VALOR) VALUES (?, ?, ?, ?);";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, item.getPedido().getId());
            pst.setInt(2, item.getProduto().getId());
            pst.setInt(3, item.getQuantidade());
            pst.setDouble(4, item.getValor());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao inserir item do pedido: " + e.getMessage(), e);
        }
    }

    public void update(ItemPedido item) {
        String sql = "UPDATE ITEM_PEDIDO SET ID_PEDIDO_FK = ?, ID_PRODUTO_FK = ?, QUANTIDADE = ?, VALOR = ? WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, item.getPedido().getId());
            pst.setInt(2, item.getProduto().getId());
            pst.setInt(3, item.getQuantidade());
            pst.setDouble(4, item.getValor());
            pst.setInt(5, item.getId());

            pst.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao atualizar item do pedido: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM ITEM_PEDIDO WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao excluir item do pedido: " + e.getMessage(), e);
        }
    }

    private ItemPedido mapResultSetToItemPedido(ResultSet rs, Connection connection) throws SQLException {
        int id = rs.getInt("ID");
        int idPedido = rs.getInt("ID_PEDIDO_FK");
        int idProduto = rs.getInt("ID_PRODUTO_FK");
        int quantidade = rs.getInt("QUANTIDADE");
        double valor = rs.getDouble("VALOR");

        Pedido pedido = new PedidoDao().findById(idPedido);
        Produto produto = new ProdutoDao().findById(idProduto);

        if (pedido == null || produto == null) {
            return null;
        }

        return new ItemPedido(id, pedido, produto, quantidade, valor);
    }
}

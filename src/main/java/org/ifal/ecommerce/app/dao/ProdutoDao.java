package org.ifal.ecommerce.app.dao;

import org.ifal.ecommerce.domain.entity.Produto;
import org.ifal.ecommerce.domain.repository.db.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {

    public void save(Produto produto) {
        String sql = "INSERT INTO PRODUTO (ID, NOME, VALOR_UNIT, QUANTIDADE) VALUES (?,?,?,?);";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, produto.getId());
            pst.setString(2, produto.getNome());
            pst.setDouble(3, produto.getValorUnitario());
            pst.setInt(4, produto.getQuantidade());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                produto.setId(rs.getInt(1));
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> findAll() {
        String sql = "SELECT * FROM PRODUTO;";
        List<Produto> lista = new ArrayList<>();

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String nome = rs.getString("NOME");
                double valorUnitario = rs.getDouble("VALOR_UNIT");
                int quantidade = rs.getInt("QUANTIDADE");

                Produto produto = new Produto(id, nome, valorUnitario, quantidade);
                lista.add(produto);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos: " + e.getMessage(), e);
        }

        return lista;
    }

    public Produto findById(int id) {
        String sql = "SELECT * FROM PRODUTO WHERE ID = ?;";
        Produto produto = null;

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("NOME");
                double valorUnitario = rs.getDouble("VALOR_UNIT");
                int quantidade = rs.getInt("QUANTIDADE");

                produto = new Produto(id, nome, valorUnitario, quantidade);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID: " + e.getMessage(), e);
        }

        return produto;
    }

    public void update(Produto produto) {
        String sql = "UPDATE PRODUTO SET NOME = ?, VALOR_UNIT = ?, QUANTIDADE = ? WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, produto.getNome());
            pst.setDouble(2, produto.getValorUnitario());
            pst.setInt(3, produto.getQuantidade());
            pst.setInt(4, produto.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Nenhum produto foi atualizado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM PRODUTO WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("Nenhum produto foi deletado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
        }
    }
}

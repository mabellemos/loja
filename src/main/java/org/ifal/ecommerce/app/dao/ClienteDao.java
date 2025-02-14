package org.ifal.ecommerce.app.dao;

import org.ifal.ecommerce.domain.entity.Cliente;
import org.ifal.ecommerce.domain.repository.db.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    public void save(Cliente cliente) {
        String sql = "INSERT INTO CLIENTE (CPF, NOME, ENDERECO, TELEFONE) VALUES (?,?,?,?);";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, cliente.getCpf());
            pst.setString(2, cliente.getNome());
            pst.setString(3, cliente.getEndereco());
            pst.setString(4, cliente.getTelefone());

            pst.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage(), e);
        }
    }

    public List<Cliente> findAll() {
        String sql = "SELECT * FROM CLIENTE;";
        List<Cliente> lista = new ArrayList<>();

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String cpf = rs.getString("CPF");
                String nome = rs.getString("NOME");
                String endereco = rs.getString("ENDERECO");
                String telefone = rs.getString("TELEFONE");

                Cliente cliente = new Cliente(cpf, nome, endereco, telefone);
                lista.add(cliente);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes: " + e.getMessage(), e);
        }

        return lista;
    }

    public Cliente findByCpf(String cpf) {
        String sql = "SELECT * FROM CLIENTE WHERE CPF = ?;";
        Cliente cliente = null;

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, cpf);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("NOME");
                    String endereco = rs.getString("ENDERECO");
                    String telefone = rs.getString("TELEFONE");

                    cliente = new Cliente(cpf, nome, endereco, telefone);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
        }

        return cliente;
    }



    public void update(Cliente cliente) {
        String sql = "UPDATE CLIENTE SET CPF = ?, NOME = ?, ENDERECO = ?, TELEFONE = ? WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, cliente.getCpf());
            pst.setString(2, cliente.getNome());
            pst.setString(3, cliente.getEndereco());
            pst.setString(4, cliente.getTelefone());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Nenhum cliente foi atualizado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM CLIENTE WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("Nenhum cliente foi deletado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage(), e);
        }
    }
}

package org.ifal.ecommerce.app.dao;

import org.ifal.ecommerce.domain.entity.Funcionario;
import org.ifal.ecommerce.domain.repository.db.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDao {

    public void save(Funcionario funcionario) {
        String sql = "INSERT INTO FUNCIONARIO (CPF, NOME, ENDERECO, TELEFONE) VALUES (?,?,?,?);";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, funcionario.getCpf());
            pst.setString(2, funcionario.getNome());
            pst.setString(3, funcionario.getEndereco());
            pst.setString(4, funcionario.getTelefone());

            pst.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao salvar funcionário: " + e.getMessage(), e);
        }
    }

    public List<Funcionario> findAll() {
        String sql = "SELECT * FROM FUNCIONARIO;";
        List<Funcionario> lista = new ArrayList<>();

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String cpf = rs.getString("CPF");
                String nome = rs.getString("NOME");
                String endereco = rs.getString("ENDERECO");
                String telefone = rs.getString("TELEFONE");

                Funcionario funcionario = new Funcionario(cpf, nome, endereco, telefone);
                lista.add(funcionario);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionários: " + e.getMessage(), e);
        }

        return lista;
    }

    public Funcionario findByCpf(String cpf) {
        String sql = "SELECT * FROM FUNCIONARIO WHERE CPF = ?;";
        Funcionario funcionario = null;

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, cpf);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("NOME");
                    String endereco = rs.getString("ENDERECO");
                    String telefone = rs.getString("TELEFONE");

                    funcionario = new Funcionario(cpf, nome, endereco, telefone);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por CPF: " + e.getMessage(), e);
        }

        return funcionario;
    }



    public void update(Funcionario funcionario) {
        String sql = "UPDATE FUNCIONARIO SET CPF = ?, NOME = ?, ENDERECO = ?, TELEFONE = ? WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, funcionario.getCpf());
            pst.setString(2, funcionario.getNome());
            pst.setString(4, funcionario.getEndereco());
            pst.setString(5, funcionario.getTelefone());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Nenhum funcionário foi atualizado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao atualizar funcionário: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM FUNCIONARIO WHERE ID = ?;";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("Nenhum funcionário foi deletado. ID não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário: " + e.getMessage(), e);
        }
    }
}


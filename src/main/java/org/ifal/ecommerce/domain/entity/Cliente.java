package org.ifal.ecommerce.domain.entity;

public class Cliente {
    private String cpf;
    private String nome;
    private String endereco;
    private String telefone;

    public Cliente() {}

    public Cliente(String cpf, String nome, String endereco, String telefone) {
        setCpf(cpf);
        this.nome = nome;
        this.endereco = endereco;
        setTelefone(telefone);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf.matches("\\d{11}")) {
            this.cpf = cpf;
        } else {
            System.out.println("CPF inválido. Deve conter exatamente 11 dígitos numéricos.");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone.matches("\\d{8,15}")) {
            this.telefone = telefone;
        } else {
            System.out.println("Telefone inválido. Deve conter apenas números e ter pelo menos 8 dígitos.");
        }
    }

    public void exibirDados() {
        System.out.println("CPF: " + cpf);
        System.out.println("Nome: " + nome);
        System.out.println("Endereço: " + endereco);
        System.out.println("Telefone: " + telefone);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                ", cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}

package org.ifal.ecommerce.domain.entity;

public class Produto {
    private int id;
    private String nome;
    private double valorUnitario;
    private int quantidade;

    public Produto() {}

    public Produto(int id, String nome, double valorUnitario, int quantidade) {
        this.id = id;
        this.nome = nome;
        setValorUnitario(valorUnitario);
        setQuantidade(quantidade);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        if (valorUnitario > 0) {
            this.valorUnitario = valorUnitario;
        } else {
            System.out.println("O valor do produto deve ser maior que zero.");
        }
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade >= 0) {
            this.quantidade = quantidade;
        } else {
            System.out.println("A quantidade não pode ser negativa.");
        }
    }

    public void aplicarDesconto(double percentual) {
        if (percentual > 0 && percentual <= 100) {
            double desconto = (valorUnitario * percentual) / 100;
            valorUnitario -= desconto;
        } else {
            System.out.println("Percentual de desconto inválido.");
        }
    }

    public void exibirDados() {
        System.out.println("Código: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Valor Unitário: R$ " + String.format("%.2f", valorUnitario));
        System.out.println("Quantidade em Estoque: " + quantidade);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", valorUnitario=R$ " + String.format("%.2f", valorUnitario) +
                ", quantidade=" + quantidade +
                '}';
    }
}
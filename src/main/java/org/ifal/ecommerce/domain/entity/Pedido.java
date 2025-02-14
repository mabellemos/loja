package org.ifal.ecommerce.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private List<Produto> produtos;
    private Funcionario vendedor;
    private Cliente cliente;
    private double valorTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Funcionario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Funcionario vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Pedido() {
        this.produtos = new ArrayList<>();
        this.valorTotal = 0.0;
    }

    public Pedido(Funcionario vendedor, Cliente cliente) {
        this.vendedor = vendedor;
        this.cliente = cliente;
    }

    public Pedido(int id, List<Produto> produtos, Funcionario vendedor, Cliente cliente, double valorTotal) {
        this.id = id;
        this.produtos = produtos;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.valorTotal = valorTotal;
    }

    public void adicionarProduto(Produto produto) {
        if (produto != null) {
            produtos.add(produto);
        } else {
            System.out.println("Produto inválido.");
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (Produto produto : produtos) {
            total += produto.getValorUnitario() * produto.getQuantidade();
        }
        this.valorTotal = total;
        return total;
    }

    public void exibirVenda() {
        System.out.println("===================================");
        System.out.println("Venda realizada por: " + vendedor.getNome());
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Produtos vendidos:");

        for (Produto produto : produtos) {
            System.out.println("- " + produto.getNome() + " | R$ " + String.format("%.2f", produto.getValorUnitario()) + " x " + produto.getQuantidade());
        }

        System.out.println("Total da venda: R$ " + String.format("%.2f", calcularTotal()));
        System.out.println("===================================");
    }

}
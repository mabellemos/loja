package org.ifal.ecommerce.domain.entity;

public class ItemPedido {
    private int id;
    private Pedido pedido;
    private Produto produto;
    private int quantidade;
    private double valor;

    public ItemPedido(int id, Pedido pedido, Produto produto, int quantidade, double valor) {
        this.id = id;
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                '}';
    }
}

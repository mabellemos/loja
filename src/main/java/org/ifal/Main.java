package org.ifal;

import org.ifal.ecommerce.app.dao.*;
import org.ifal.ecommerce.domain.entity.*;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoDao produtoDao = new ProdutoDao();
    private static final ClienteDao clienteDao = new ClienteDao();
    private static final PedidoDao pedidoDao = new PedidoDao();
    private static final FuncionarioDao funcionarioDao = new FuncionarioDao();
    private static final ItemPedidoDao itemPedidoDao = new ItemPedidoDao();

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu();
            System.out.print("\nEscolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Por favor, insira um número válido.");
                scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    cadastrarCliente();
                    break;
                case 3:
                    buscarProduto();
                    break;
                case 4:
                    listarProdutos();
                    break;
                case 5:
                    efetuarVenda();
                    break;
                case 6:
                    listarVendas();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Cadastrar Produto");
        System.out.println("2. Cadastrar Cliente");
        System.out.println("3. Buscar Produto por ID");
        System.out.println("4. Listar Todos os Produtos Disponíveis");
        System.out.println("5. Efetuar Venda");
        System.out.println("6. Listar Vendas Realizadas");
        System.out.println("0. Sair");
    }

    private static void cadastrarProduto() {
        System.out.print("Nome do Produto: ");
        String nome = scanner.nextLine();

        System.out.print("Valor Unitário: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Por favor, insira um valor numérico válido.");
            scanner.next();
        }
        double valorUnitario = scanner.nextDouble();

        System.out.print("Quantidade em Estoque: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, insira um número inteiro válido.");
            scanner.next();
        }
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        Produto produto = new Produto(0, nome, valorUnitario, quantidade);
        produtoDao.save(produto);

        System.out.println("Produto cadastrado com sucesso!");
    }

    private static void cadastrarCliente() {
        System.out.print("Nome do Cliente: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Cliente cliente = new Cliente(cpf, nome, endereco, telefone);
        clienteDao.save(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void buscarProduto() {
        System.out.print("Digite o ID do Produto: ");
        int id = scanner.nextInt();

        Produto produto = produtoDao.findById(id);
        if (produto != null) {
            System.out.println("\nProduto encontrado:");
            System.out.println(produto);
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void listarProdutos() {
        List<Produto> produtos = produtoDao.findAll();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("\nProdutos Disponíveis:");
        for (Produto p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Valor: R$ %.2f | Estoque: %d\n",
                    p.getId(), p.getNome(), p.getValorUnitario(), p.getQuantidade());
        }
    }

    private static void efetuarVenda() {
        System.out.print("CPF do Cliente: ");
        String cpfCliente = scanner.nextLine();

        Cliente cliente = clienteDao.findByCpf(cpfCliente);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.print("CPF do Funcionário (Vendedor): ");
        String cpfFuncionario = scanner.nextLine();

        Funcionario funcionario = funcionarioDao.findByCpf(cpfFuncionario);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        Map<Produto, Integer> itensVenda = new HashMap<>();

        while (true) {
            System.out.print("ID do Produto (0 para finalizar): ");
            int idProduto = scanner.nextInt();
            scanner.nextLine();

            if (idProduto == 0) break;

            Produto produto = produtoDao.findById(idProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            System.out.print("Quantidade desejada: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            if (quantidade > produto.getQuantidade()) {
                System.out.println("Estoque insuficiente.");
                continue;
            }

            itensVenda.put(produto, quantidade);
        }

        if (itensVenda.isEmpty()) {
            System.out.println("Nenhum produto adicionado à venda.");
            return;
        }

        double total = 0;

        for (Map.Entry<Produto, Integer> item : itensVenda.entrySet()) {
            Produto produto = item.getKey();
            int quantidade = item.getValue();
            total += produto.getValorUnitario() * quantidade;
            produto.setQuantidade(produto.getQuantidade() - quantidade);
            produtoDao.update(produto);
        }

        Pedido pedido = new Pedido(0, new ArrayList<>(itensVenda.keySet()), funcionario, cliente, total);
        pedidoDao.save(pedido);

        System.out.printf("Venda realizada com sucesso! Total: R$ %.2f\n", total);
    }

    private static void listarVendas() {
        List<ItemPedido> itens = itemPedidoDao.findAll();
        for (ItemPedido item : itens) {
            System.out.println("Pedido: " + item.getPedido().getId() +
                    ", Produto: " + item.getProduto().getNome() +
                    ", Quantidade: " + item.getQuantidade() +
                    ", Valor: R$ " + item.getValor());
        }

    }
}

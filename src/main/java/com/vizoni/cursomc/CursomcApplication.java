package com.vizoni.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vizoni.cursomc.domain.Categoria;
import com.vizoni.cursomc.domain.Cidade;
import com.vizoni.cursomc.domain.Cliente;
import com.vizoni.cursomc.domain.Endereco;
import com.vizoni.cursomc.domain.Estado;
import com.vizoni.cursomc.domain.ItemPedido;
import com.vizoni.cursomc.domain.Pagamento;
import com.vizoni.cursomc.domain.PagamentoComBoleto;
import com.vizoni.cursomc.domain.PagamentoComCartao;
import com.vizoni.cursomc.domain.Pedido;
import com.vizoni.cursomc.domain.Produto;
import com.vizoni.cursomc.domain.enums.EstadoPagamento;
import com.vizoni.cursomc.domain.enums.TipoCliente;
import com.vizoni.cursomc.repositories.CategoriaRepository;
import com.vizoni.cursomc.repositories.CidadeRepository;
import com.vizoni.cursomc.repositories.ClienteRepository;
import com.vizoni.cursomc.repositories.EnderecoRepository;
import com.vizoni.cursomc.repositories.EstadoRepository;
import com.vizoni.cursomc.repositories.ItemPedidoRepository;
import com.vizoni.cursomc.repositories.PagamentoRepository;
import com.vizoni.cursomc.repositories.PedidoRepository;
import com.vizoni.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	/* Este método vem do CommandLineRunner que permite já executar um código quando iniciar,
	 * está sendo usado para popular as categorias do banco (para que não comece vazio)*/
	@Override
	public void run(String... arg0) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
				
		/* associando as categorias aos produtos, as categorias sabem seus produtos, mas não o inverso*/
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1,cat2));
		
		/* cria uma array para não ter q dar um SAVE de cada vez em cada categoria */
		categoriaRepository.save(Arrays.asList(cat1,cat2));
		produtoRepository.save(Arrays.asList(p1,p2,p3));
		
		// ======================================================================
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		estadoRepository.save(Arrays.asList(est1,est2));
		cidadeRepository.save(Arrays.asList(c1,c2,c3));
		
		// =========================================================================
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("12412417432","63462355123"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "432432", "Esquina", "Zona 7", "87080015", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Brasil", "124", "Sala 500", "Centro", "1324234", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.save(Arrays.asList(cli1));
		enderecoRepository.save(Arrays.asList(e1,e2));
		
		// =============================================================================
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm"); // mascara de formatação pra instanciar uma data
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("12/08/2016 08:55"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);		
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 15:44"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedidoRepository.save(Arrays.asList(ped1,ped2));
		pagamentoRepository.save(Arrays.asList(pagto1,pagto2));
		
		// =======================================================================
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 80.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.save(Arrays.asList(ip1, ip2, ip3));
		
		
	}
}

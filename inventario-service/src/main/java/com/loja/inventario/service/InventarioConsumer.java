package com.loja.inventario.service;

import com.loja.inventario.domain.Produto;
import com.loja.inventario.repository.ProdutoRepository;
import com.loja.pedidos.domain.Pedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventarioConsumer {

    @Autowired
    private ProdutoRepository produtoRepository;

    @KafkaListener(topics = "novo-pedido", groupId = "inventario-group")
    public void processarPedido(Pedido pedido) {
        Optional<Produto> produtoOpt = produtoRepository.findById(pedido.getProdutoId());
        
        produtoOpt.ifPresent(produto -> {
            if (produto.getQuantidade() >= pedido.getQuantidade()) {
                produto.setQuantidade(produto.getQuantidade() - pedido.getQuantidade());
                produtoRepository.save(produto);
                
                // Publica evento de estoque reservado
                // (implementar producer similar ao de pedidos)
            } else {
                // Publica evento de estoque insuficiente
            }
        });
    }
}
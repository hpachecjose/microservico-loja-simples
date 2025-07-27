package com.loja.pedidos.service;

import com.loja.pedidos.domain.Pedido;
import com.loja.pedidos.dto.PedidoDTO;
import com.loja.pedidos.kafka.PedidoProducer;
import com.loja.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoProducer pedidoProducer;

    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(pedidoDTO.getClienteId());
        pedido.setProdutoId(pedidoDTO.getProdutoId());
        pedido.setQuantidade(pedidoDTO.getQuantidade());
        pedido.setStatus("PENDENTE");
        
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        // Publica evento no Kafka
        pedidoProducer.enviarPedido(pedidoSalvo);
        
        return pedidoSalvo;
    }

    public Optional<Pedido> buscarPedido(Long id) {
        return pedidoRepository.findById(id);
    }
}
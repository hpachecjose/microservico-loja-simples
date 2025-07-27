package com.loja.pedidos.kafka;

import com.loja.pedidos.domain.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducer {

    private static final String TOPIC = "novo-pedido";

    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedido(Pedido pedido) {
        this.kafkaTemplate.send(TOPIC, pedido);
    }
}
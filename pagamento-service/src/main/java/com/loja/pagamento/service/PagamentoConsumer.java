package com.loja.pagamento.service;

import com.loja.pagamento.domain.Transacao;
import com.loja.pagamento.repository.TransacaoRepository;
import com.loja.pedidos.domain.Pedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PagamentoConsumer {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @KafkaListener(topics = "novo-pedido", groupId = "pagamento-group")
    public void processarPedido(Pedido pedido) {
        Transacao transacao = new Transacao();
        transacao.setPedidoId(pedido.getId());
        transacao.setStatus("PROCESSANDO");
        transacao.setValor(calcularValor(pedido)); // Implementar cálculo
        
        transacaoRepository.save(transacao);
        
        // Simular processamento de pagamento
        if (Math.random() > 0.2) { // 80% de chance de sucesso
            transacao.setStatus("APROVADO");
        } else {
            transacao.setStatus("REJEITADO");
        }
        
        transacaoRepository.save(transacao);
        
        // Publicar evento de resultado do pagamento
    }
}
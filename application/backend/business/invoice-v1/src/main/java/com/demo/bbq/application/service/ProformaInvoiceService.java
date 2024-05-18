package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProformaInvoiceService {

  Mono<ProformaInvoiceResponseDTO> generateProformaInvoice(ServerRequest serverRequest, Flux<ProductRequestDTO> products);

}

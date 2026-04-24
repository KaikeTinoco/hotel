package com.desafio.hotel.specification.criteria;

import lombok.Builder;
import lombok.Data;

/**
 * Classe de critério para buscas dinâmicas.
 *
 * <p>Representa um critério de busca contendo chave, operação e valor
 * para construção de consultas JPA dinâmicas.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;


}

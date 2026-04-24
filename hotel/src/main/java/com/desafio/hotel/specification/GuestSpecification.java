package com.desafio.hotel.specification;

import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.specification.criteria.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Especificação para consultas dinâmicas de hóspedes.
 *
 * <p>Implementa Specification do Spring Data JPA para permitir buscas flexíveis
 * em entidades Guest baseadas em critérios de busca.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestSpecification implements Specification<Guest> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Guest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(criteria.getOperation().equalsIgnoreCase("%")){
            if(root.get(criteria.getKey()).getJavaType()==String.class){
                return criteriaBuilder.
                        like(root.<String>get(criteria.getKey()),
                                "%" + criteria.getValue() + "%");
            }else{
                return criteriaBuilder.equal(root.get(criteria.getKey()),criteria.getValue());
            }
        }
        return null;
    }
}

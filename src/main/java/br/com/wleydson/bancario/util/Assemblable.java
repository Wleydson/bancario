package br.com.wleydson.bancario.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Interface para facilitar a conversão de {@code entidade}s para {@code DTO}s.
 *
 *
 */
public interface Assemblable<E, D> {

    /**
     * <p>Deve definir como o {@code DTO} será preenchido a partir da {@code entidade}.</p>
     *
     * @param entity
     * @return uma instância de {@code DTO}.
     */
    D toDTO(E entity);

    /**
     * <p>Método que mapeia uma {@link List}<{@code entidade}> para uma {@link List}<{@code DTO}>.</p>
     * <p>Para a conversão, utiliza a implementação fornecida no método {@code toDTO}.</p>
     *
     * @param entities
     * @return uma {@link List}<{@code DTO}> obtida a partir de uma {@link List}<{@code entidade}>.
     */
    default List<D> toDTOList(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(toList());
    }

    /**
     * <p>Método que mapeia uma {@link Page}<{@code entidade}> para uma {@link Page}<{@code DTO}>.</p>
     *
     * @param pageable
     * @param page
     * @return uma {@link Page}<{@code DTO}> obtida a partir de uma {@link Page}<{@code entidade}>.
     */
    default Page<D> toDTOPage(Pageable pageable, Page<E> page) {
        if (page == null) {
            return null;
        }

        return new PageImpl<>(this.toDTOList(page.getContent()), pageable, page.getTotalElements());
    }
}

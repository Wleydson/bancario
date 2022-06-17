package br.com.wleydson.bancario.util;

/**
 * Interface para facilitar a conversão de {@code DTO (input)}s para {@code entidade}s.
 * 
 *
 */
public interface Disassemblable<D, E> {

	/**
	 * <p>Deve definir como a {@code entidade} será preenchido a partir do {@code DTO (input)}.</p>
	 * 
	 * @param input
	 * @return uma instância de {@code Entity}.
	 */
	E toEntity(D input);
	
}

package br.com.wleydson.bancario.config;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageHelper {
	
	private final MessageSource messageSource;

	public String getMensagem(String chave) {
		return messageSource.getMessage(chave, null, LocaleContextHolder.getLocale());
	}
}

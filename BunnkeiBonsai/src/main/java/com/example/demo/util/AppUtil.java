package com.example.demo.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * アプリケーション共通クラス
 */
public class AppUtil {
	/**
	 * 
	 * @param messageSource メッセージソース	
	 * @param key	メッセージキー	
	 * @param params　置換文字群
	 * @return	メッセージ
	 */
	public static String getMessage(MessageSource messageSource, String key, Object... params) {
		Locale locale = LocaleContextHolder.getLocale(); // 💡ここが重要！
		return messageSource.getMessage(key, params, locale);
	}
}

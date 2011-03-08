package org.dspace.webmvc.theme;

import org.springframework.context.NoSuchMessageException;
import org.springframework.ui.context.Theme;

import java.util.Locale;

public final class SpringThemeUtils {
    private SpringThemeUtils() {
    }

    public static String getProperty(Theme theme, String property, Locale locale) {
        return theme == null ? null : theme.getMessageSource().getMessage(property, null, locale);
    }

    public static String getProperty(Theme theme, String property, Locale locale, String defaultValue) {
        try {
            return theme == null ? null : theme.getMessageSource().getMessage(property, null, locale);
        } catch (NoSuchMessageException nsme) {
            return defaultValue;
        }
    }
}

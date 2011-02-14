package org.dspace.webmvc.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

public class NameReplacingMessageSource implements MessageSource {
    private MessageSource messageSource;
    private MessageSource nameSource;
    private String openTag = "${";
    private String closeTag = "}";

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setNameSource(MessageSource nameSource) {
        this.nameSource = nameSource;
    }

    public void setOpenTag(String openTag) {
        this.openTag = openTag;
    }

    public void setCloseTag(String closeTag) {
        this.closeTag = closeTag;
    }

    public String getMessage(final MessageSourceResolvable resolvable, final Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(resolvable, locale);
    }

    public String getMessage(final String code, final Object[] args, final Locale locale) throws NoSuchMessageException {
        String msg = messageSource.getMessage(code, args, locale);

        if (msg != null) {
            return replaceNames(msg, locale);
        }

        return msg;
    }

    public String getMessage(final String code, final Object[] args, final String defaultMessage, final Locale locale) {
        String msg = messageSource.getMessage(code, args, defaultMessage, locale);

        if (msg != null) {
            return replaceNames(msg, locale);
        }

        return msg;
    }

    private String replaceNames(final String msg, Locale locale) {
        String localMsg = msg;
        int openPos = -1;

        do {
            openPos = localMsg.indexOf(openTag, openPos);
            if (openPos < 0) {
                break;
            }

            int closePos = localMsg.indexOf(closeTag, openPos);
            if (closePos < 0) {
                break;
            }

            String subCode = localMsg.substring(openPos + openTag.length(), closePos);
            String replacement = getName(subCode, locale);

            if (replacement != null) {
                localMsg = new StringBuilder(msg.substring(0, openPos))
                        .append(replacement)
                        .append(msg.substring(closePos + closeTag.length()))
                        .toString();
            }

            openPos++;
        } while (openPos < localMsg.length());

        return localMsg;
    }

    private String getName(final String code, final Locale locale) {
        if (nameSource != null) {
            try {
                String msg = nameSource.getMessage(code, null, locale);

                if (msg != null) {
                    return replaceNames(msg, locale);
                }
            } catch (NoSuchMessageException e) {
                return code;
            }
        }

        return code;
    }
}
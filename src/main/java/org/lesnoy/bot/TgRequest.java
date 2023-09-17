package org.lesnoy.bot;

import org.apache.shiro.session.Session;

public record TgRequest(String request, String username, Session session) {
}

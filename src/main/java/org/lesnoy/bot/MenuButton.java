package org.lesnoy.bot;

import java.util.Arrays;

public enum MenuButton {
    START("/start"),
    PRODUCT_MENU("Меню продуктов"),
    ADD_DISHES("Добавить прием пищи"),
    CALORIES_DAILY("Суточное КБЖУ"),
    UPDATE_CALORIES_INFO("Изменить норму КБЖУ");

    private final String massage;

    MenuButton(String massage) {
        this.massage = massage;
    }
    public String getMassage() {
        return massage;
    }

    public static MenuButton parseString(String value) throws Exception {
        return Arrays.stream(MenuButton.values())
                .filter(btn -> btn.getMassage().equals(value))
                .findFirst().orElseThrow(Exception::new);

    }
}

package org.lesnoy.product;

import java.util.Arrays;

public enum ProductType {
    UNTYPED("Без типа"),
    MEAT("Мясо"),
    VEGETABLES("Овощи"),
    FRUITS("Фрукты"),
    DAIRY_EGGS("Молочные продукты и яйца"),
    CEREALS("Крупы"),
    SWEETS("Сладости"),
    DRINKS("Напитки");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String[] getAllNames() {
        String[] names = new String[ProductType.values().length];
        for (int i = 0; i < names.length; i++) {
            names[i] = ProductType.values()[i].getName();
        }
        return names;
    }

    public static ProductType getTypeByName(String name) throws Exception {
        return Arrays.stream(ProductType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElseThrow(Exception::new);
    }
}

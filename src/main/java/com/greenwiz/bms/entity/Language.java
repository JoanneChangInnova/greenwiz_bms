package com.greenwiz.bms.entity;

public enum Language {

    /**
     * 繁體中文
     */
    CHT("CHT"),

    /**
     * 英文
     */
    ENG("ENG");

    private String value;

    Language(String value) {
        this.value = value;
    }
}

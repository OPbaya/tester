package com.api.tester.model;

public class TestCase {

    private String name;
    private String type;

    public TestCase() {
    }
    public TestCase(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
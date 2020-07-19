package com.mateus.burble.argument;

public class Argument {

    private final String argName;
    private final ArgumentType type;
    private final boolean obligatory;
    private ArgumentElement element;

    public Argument(String argName, ArgumentType type, boolean obligatory) {
        this.argName = argName;
        this.type = type;
        this.obligatory = obligatory;
    }

    public void setContent(ArgumentElement element) {
        this.element = element;
    }

    public void setContent(String content) {
        this.element = new ArgumentElement(content);
    }
    public String getArgName() {
        return argName;
    }

    public ArgumentType getType() {
        return type;
    }

    public boolean isObligatory() {
        return obligatory;
    }

    public ArgumentElement getElement() {
        return element;
    }
}

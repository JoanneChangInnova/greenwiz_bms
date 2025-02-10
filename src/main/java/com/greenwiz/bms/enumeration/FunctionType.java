package com.greenwiz.bms.enumeration;

public interface FunctionType {

    String getFunctionType();

    enum Monitor implements FunctionType {
        ONE_P_N("1P+N"),
        TWO_P("2P"),
        TWO_P_N("2P+N"),
        THREE_P("3P"),
        THREE_P_N("3P+N");

        private final String functionType;

        Monitor(String functionType) {
            this.functionType = functionType;
        }

        @Override
        public String getFunctionType() {
            return functionType;
        }
    }

    enum SWB implements FunctionType {
        ON("ON"),

        OFF("OFF")
        ;

        private final String functionType;

        SWB(String functionType) {
            this.functionType = functionType;
        }

        @Override
        public String getFunctionType() {
            return functionType;
        }
    }

    /**
     * AMA-Fans 的功能模式。
     */
    enum AmaFans implements FunctionType {
        THREE("3"),
        TWO("2"),
        ONE("1"),
        OFF("OFF");

        private final String functionType;

        AmaFans(String displayName) {
            this.functionType = displayName;
        }

        @Override
        public String getFunctionType() {
            return functionType;
        }
    }

    /**
     * CX-IR0001S 的功能模式。
     */
    enum CXIR0001S implements FunctionType {
        AUTO("Auto"),
        ONLY_AIR("Only Air"),
        STRONG_AIR("Strong Air"),
        OFF("OFF");

        private final String functionType;

        CXIR0001S(String functionType) {
            this.functionType = functionType;
        }

        @Override
        public String getFunctionType() {
            return functionType;
        }
    }
}

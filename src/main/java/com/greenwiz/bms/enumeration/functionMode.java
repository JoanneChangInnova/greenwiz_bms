package com.greenwiz.bms.enumeration;

public interface functionMode {

    String getFunctionMode();

    enum Monitor implements functionMode {
        ONE_P_N("1P+N"),
        TWO_P("2P"),
        TWO_P_N("2P+N"),
        THREE_P("3P"),
        THREE_P_N("3P+N");

        private final String functionMode;

        Monitor(String functionMode) {
            this.functionMode = functionMode;
        }

        @Override
        public String getFunctionMode() {
            return functionMode;
        }
    }

//    enum SWB implements functionMode {
//        ON("ON"),
//
//        OFF("OFF")
//        ;
//
//        private final String functionMode;
//
//        SWB(String functionMode) {
//            this.functionMode = functionMode;
//        }
//
//        @Override
//        public String getfunctionMode() {
//            return functionMode;
//        }
//    }
//
//    /**
//     * AMA-Fans 的功能模式。
//     */
//    enum AmaFans implements functionMode {
//        THREE("3"),
//        TWO("2"),
//        ONE("1"),
//        OFF("OFF");
//
//        private final String functionMode;
//
//        AmaFans(String displayName) {
//            this.functionMode = displayName;
//        }
//
//        @Override
//        public String getfunctionMode() {
//            return functionMode;
//        }
//    }
//
//    /**
//     * CX-IR0001S 的功能模式。
//     */
//    enum CXIR0001S implements functionMode {
//        AUTO("Auto"),
//        ONLY_AIR("Only Air"),
//        STRONG_AIR("Strong Air"),
//        OFF("OFF");
//
//        private final String functionMode;
//
//        CXIR0001S(String functionMode) {
//            this.functionMode = functionMode;
//        }
//
//        @Override
//        public String getfunctionMode() {
//            return functionMode;
//        }
//    }
}

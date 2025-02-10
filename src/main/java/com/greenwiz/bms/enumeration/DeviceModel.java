package com.greenwiz.bms.enumeration;

public interface DeviceModel {
    String getDeviceModel();

    public enum Monitor implements DeviceModel{
        METSEPM2220("METSEPM2220"),

        METSEPM1125HCL05RD("METSEPM1125HCL05RD"),

        IEM2455_230V_100A("IEM2455-230V-100A");

        private final String deviceModel;

        Monitor(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        @Override
        public String getDeviceModel() {
            return deviceModel;
        }
    }

    public enum Controller implements DeviceModel{
        SWB("SWB"),

        CX_IR0001S("CX-IR0001S"),

        AMA_Fans("AMA-Fans");

        private final String deviceModel;

        Controller(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        @Override
        public String getDeviceModel() {
            return deviceModel;
        }
    }
}

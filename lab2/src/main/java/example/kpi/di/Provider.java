package example.kpi.di;

import example.kpi.model.result.AppConfiguration;

public class Provider {
    private static AppConfiguration appConfiguration;

    public static void setAppConfiguration(AppConfiguration appConfiguration) {
        Provider.appConfiguration = appConfiguration;
    }


    public static AppConfiguration appConfiguration() {
        return Provider.appConfiguration;
    }
}

//package mbbank.auto.mobile;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.intuit.karate.RuntimeHook;
//import com.intuit.karate.Suite;
//import com.intuit.karate.core.*;
//import com.intuit.karate.driver.WebDriver;
//import com.intuit.karate.http.ResourceType;
//import com.intuit.karate.http.Response;
//import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.remote.MobileCapabilityType;
//import mbbank.auto.base.utils.Commons;
//import mbbank.auto.keywords.BaseMobileActions;
//import mbbank.auto.keywords.MobileActions;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.json.TypeToken;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Type;
//import java.net.MalformedURLException;
//import java.util.*;
//
//import static mbbank.auto.keywords.MobileActions.*;
//
//public class MobileDefaultRuntimeHook implements RuntimeHook {
//
//    private String deviceKey;
//    private final String deviceConfigPath;
//    private final String screenshotStrategy;
//
//    private Suite.LoopScenarioStrategy loopScenario;
//
//    private int timeout;
//    private Map<String, Map<String, String>> deviceConfigs;
//    private static Map<String, String> deviceSessions = new HashMap<>();
//
//    private Map<String, List<String>> statusMap;
//
//    private static final Object lockBefore = new Object();
//
//    private static final Object lockAfter = new Object();
//
//    public static Map<String, Map<String, String>> deviceMap = new LinkedHashMap<>();
//
//    public static String DEFAULT_DEVICE_ID_VALUE = "deviceId";
//    public static String STATUS_FIELD = "status";
//
//    public enum SerenityScreenshotStrategy {
//        FOR_EACH_ACTION,
//        BEFORE_AND_AFTER_EACH_STEP,
//        AFTER_EACH_STEP,
//        FOR_FAILURES,
//        DISABLED
//    }
//
//    public MobileDefaultRuntimeHook(String deviceConfigPath) {
//        this.deviceKey = DEFAULT_DEVICE_ID_VALUE;
//        this.timeout = 0;
//        String deviceConfig = System.getProperty("device");
//        if (deviceConfig == null || deviceConfig.isEmpty()) deviceConfig = deviceConfigPath;
//        this.deviceConfigPath = deviceConfig;
//        this.screenshotStrategy = BaseMobileActions.environmentVariables.getProperty("serenity.take.screenshots");
//        this.loopScenario = Suite.LoopScenarioStrategy.DISABLED;
//        this.statusMap = new LinkedHashMap<>();
//        readDeviceConfigs();
//    }
//
//    public MobileDefaultRuntimeHook withDeviceKey(String deviceKey) {
//        this.deviceKey = deviceKey;
//        return this;
//    }
//
//    public MobileDefaultRuntimeHook withTimeout(int millis) {
//        this.timeout = millis;
//        return this;
//    }
//
//    public MobileDefaultRuntimeHook withLoopScenario() {
//        this.loopScenario = Suite.LoopScenarioStrategy.ANY_SCENARIO;
//        return this;
//    }
//
//    public MobileDefaultRuntimeHook withLoopScenario(Suite.LoopScenarioStrategy strategy) {
//        this.loopScenario = strategy;
//        return this;
//    }
//
//    private byte[] tmpBytes;
//
//    protected void readDeviceConfigs() {
//        Gson gson = new Gson();
//        try (FileReader reader = new FileReader(this.deviceConfigPath)) {
//            Type listType = new TypeToken<List<Map<String, String>>>() {
//            }.getType();
//            List<Map<String, String>> list = gson.fromJson(reader, listType);
//
//            deviceMap = new LinkedHashMap<>();
//            for (Map<String, String> device : list) {
//                device.put(STATUS_FIELD, "False");
//                String deviceId = device.get(this.deviceKey);
//                deviceMap.put(deviceId, device);
//                deviceSessions.put(deviceId, null);
//            }
//            this.deviceConfigs = deviceMap;
//        } catch (JsonSyntaxException e) {
//            Commons.logAndReport("File Json Syntax Error: " + this.deviceConfigPath);
//        } catch (IOException e) {
//            Commons.logAndReport("File not found: " + this.deviceConfigPath);
//        }
//    }
//
//    @Override
//    public boolean beforeScenario(ScenarioRuntime sr) {
//        boolean superResult = RuntimeHook.super.beforeScenario(sr);
//        if (!superResult) return superResult;
//
//        String scenarioId = sr.scenario.getUniqueId();
//        synchronized (lockBefore) {
//            if (!statusMap.containsKey(scenarioId)) {
//                List<String> devices = new ArrayList<>();
//                devices.addAll(deviceConfigs.keySet());
//                statusMap.put(scenarioId, devices);
//            }
//
//
//            //find available device
////            if (statusMap.get(scenarioId).isEmpty()) {
////                Commons.logAndReport("No devices to run");
////                return false;
////            }
//        }
//
//        int count = 0;
//        String foundDevice = "";
//        while (Commons.isBlankOrEmpty(foundDevice) && count <= this.timeout) {
//
//            synchronized (lockBefore) {
//                for (String deviceId : statusMap.get(scenarioId)) {
//                    Map<String, String> theDevice = deviceConfigs.get(deviceId);
//                    String udid = theDevice.get(MobileCapabilityType.UDID);
//                    String token = theDevice.get(MobileCapabilityType.DEVICE_NAME);
//                    boolean result = checkDeviceUsing(sr, udid, token, deviceId);
//                    if (!result) {
//                        foundDevice = deviceId;
//                        setCurrentDevice(deviceId);
//                        break;
//                    }
//                }
//            }
//            if (Commons.isBlankOrEmpty(foundDevice)) {
//                count += 15000;  //15 sec
//                if (this.timeout == 0) {
//                    Commons.delayInMilliseconds(15000);
//                }
//            }
//        }
//
//        if (!BaseMobileActions.APPIUM_QUIT_DRIVER) foundDevice = getCurrentDevice();
//
//        synchronized (lockBefore) {
//
//            if (foundDevice.isEmpty()) {
//                Commons.logAndReport("All devices are being used");
//                return false;
//            }
//
//            Map<String, String> theDevice = deviceConfigs.get(foundDevice);
//
//            theDevice.forEach((key, value) -> {
//                sr.engine.setVariable(key, value);
//            });
//
//            theDevice.put(STATUS_FIELD, "True");
//            deviceConfigs.put(foundDevice, theDevice);
//            statusMap.get(scenarioId).remove(foundDevice);
//        }
//        if (getDriver() == null) {
//            MobileActions actions = new MobileActions();
//            try {
//                actions.mobileConnectToServer();
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//
//            //TODO: Save SessionID to map deviceConfigs
//            deviceSessions.put(BaseMobileActions.getEngineVariable("deviceId"), BaseMobileActions.APPIUM_SERVER_URL + "/session/" + getDriver().getSessionId().toString());
//        }
//
//        //add log to report
//        sr.result.addFakeStepResult("Run Scenario with deviceId: " + foundDevice, null);
//        return true;
//    }
//
//    @Override
//    public void afterScenario(ScenarioRuntime sr) {
//        if (BaseMobileActions.APPIUM_QUIT_DRIVER) {
//            quitAppiumDriver();
//        }
//        synchronized (lockAfter) {
//            String deviceId = sr.engine.getVariable(deviceKey).toString();
//            Map<String, String> theDevice = deviceConfigs.get(deviceId);
//            theDevice.put(STATUS_FIELD, "False");
//            deviceConfigs.put(deviceId, theDevice);
//        }
//        RuntimeHook.super.afterScenario(sr);
//    }
//
//    @Override
//    public boolean beforeFeature(FeatureRuntime fr) {
//        return RuntimeHook.super.beforeFeature(fr);
//    }
//
//    @Override
//    public void beforeSuite(Suite suite) {
//        RuntimeHook.super.beforeSuite(suite);
//        if (loopScenario != Suite.LoopScenarioStrategy.DISABLED) {
//            //TODO
//            suite.setLoopScenarioStrategy(loopScenario);
//            suite.setLoopScenarioCount(deviceConfigs.size());
//        }
//    }
//
//    @Override
//    public boolean beforeStep(Step step, ScenarioRuntime sr) {
//        tmpBytes = null;
//        if (screenshotStrategy.equalsIgnoreCase(SerenityScreenshotStrategy.FOR_EACH_ACTION.name())
//                || screenshotStrategy.equalsIgnoreCase(SerenityScreenshotStrategy.BEFORE_AND_AFTER_EACH_STEP.name())) {
//            if (getDriver() != null) {
//                try {
//                    tmpBytes = takeScreenshot(getDriver(), false);
//                } catch (Exception e) {
//                    Commons.logAndReport("Error when screenshot: " + e.getMessage());
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void afterSuite(Suite suite) {
//        RuntimeHook.super.afterSuite(suite);
//        quitAppiumDriver();
//    }
//
//    @Override
//    public void afterStep(StepResult result, ScenarioRuntime sr) {
//        if (result.isFailed()) {
//            if (getDriver() != null) {
//                try {
//                    byte[] bytes = takeScreenshot(getDriver(), false);
//                    Embed embed = sr.saveToFileAndCreateEmbed(bytes, ResourceType.PNG);
//                    result.addEmbed(embed);
//                } catch (Exception e) {
//                    Commons.logAndReport("Error when screenshot: " + e.getMessage());
//                }
//            }
//            return;
//        }
//        Method method = result.getResult().getMatchingMethod().getMethod();
//        AutoScreenshot autoScreenshot = method.getAnnotation(AutoScreenshot.class);
//        if (autoScreenshot == null || !autoScreenshot.enable()) {
//            return;
//        }
//        if (tmpBytes != null) {
//            Embed embed = sr.saveToFileAndCreateEmbed(tmpBytes, ResourceType.PNG);
//            result.addEmbed(embed);
//        }
//        if (screenshotStrategy.equalsIgnoreCase(SerenityScreenshotStrategy.AFTER_EACH_STEP.name())
//                || screenshotStrategy.equalsIgnoreCase(SerenityScreenshotStrategy.FOR_EACH_ACTION.name())
//                || screenshotStrategy.equalsIgnoreCase(SerenityScreenshotStrategy.BEFORE_AND_AFTER_EACH_STEP.name())) {
//            if (getDriver() != null) {
//                try {
//                    byte[] bytes = takeScreenshot(getDriver(), false);
//                    Embed embed = sr.saveToFileAndCreateEmbed(bytes, ResourceType.PNG);
//                    result.addEmbed(embed);
//                } catch (Exception e) {
//                    Commons.logAndReport("Error when screenshot: " + e.getMessage());
//                }
//            }
//        }
//    }
//
//    protected byte[] takeScreenshot(AppiumDriver driver, boolean embed) throws IOException {
//        byte[] bytes = driver.getScreenshotAs(OutputType.BYTES);
//        if (embed) {
//            ((WebDriver) ((org.openqa.selenium.WebDriver) driver)).getRuntime().embed(bytes, ResourceType.PNG);
//        }
//        return bytes;
//    }
//
//    protected void quitAppiumDriver() {
//        AppiumDriver currentDriver = getDriver();
//
//        if (currentDriver != null) {
//            try {
//                if (currentDriver.getSessionId() != null) {
//                    Commons.logAndReport("Closing AppiumDriver session...");
//                    currentDriver.quit();
//                }
//            } catch (Exception e) {
//                Commons.logAndReport("Error while closing AppiumDriver: " + e.getMessage());
//            } finally {
//                MobileActions.removeDriver();
//            }
//        }
//    }
//
//    public int getDeviceCount() {
//        return deviceConfigs.size();
//    }
//
//    private static final String appiumDvfUrl = "http://10.1.27.84:4000/wd/hub";
//    private static final String dvfApiUrl = "http://10.1.27.86:7100/api/v1/";
//
//    public boolean checkDeviceUsing(ScenarioRuntime sr, String udid, String token, String deviceId) {
//        if (BaseMobileActions.APPIUM_SERVER_URL.contains(appiumDvfUrl)) {
//            return checkDVFStatus(sr, udid, token);
//        } else {
//            return checkAppiumStatus(sr, deviceId);
//        }
//    }
//
//    private boolean checkDVFStatus(ScenarioRuntime sr, String udid, String token) {
//        sr.engine.getRequestBuilder()
//                .url(dvfApiUrl + "devices/" + udid)
//                .header("Authorization", "Bearer " + token);
//        Response response = sr.engine.httpInvoke();
//        String using = response.json().get("device.using").toString().toLowerCase();
//        return using.equals("true");
//    }
//
//    private boolean checkAppiumStatus(ScenarioRuntime sr, String deviceId) {
//        String sessionUrl = deviceSessions.get(deviceId);
//        if (sessionUrl != null) {
//            if (sessionUrl.equals("init")) return true;
//            sr.engine.getRequestBuilder().url(sessionUrl);
//            Response response = sr.engine.httpInvoke();
//            return !response.getBodyAsString().matches(".+\"error\\\"\\s*:\\s*\\\"invalid session id\\\".+");
//        } else {
//            deviceSessions.put(deviceId, "init");
//        }
//        return false;
//    }
//}
package allureReport;

import listeners.ReportListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

import java.io.ByteArrayInputStream;

public class AllureReport implements ReportListener {
    @Override
    public void onAssertPass(String s) {
        Allure.step(s, Status.PASSED);
    }

    @Override
    public void onAssertFail(String s, byte[] bytes) {
        Allure.getLifecycle().updateTestCase(tc -> tc.setStatus(Status.FAILED));
        Allure.step(s, Status.FAILED);
        Allure.addAttachment("Screenshot Fail", new ByteArrayInputStream(bytes));
    }

    @Override
    public void onStepInfo(String s) {
        Allure.step(s, Status.PASSED);
    }

    @Override
    public void onApiStep(String stepName, String requestBody, String responseBody) {
        Allure.step(stepName, () -> {
            if (requestBody != null && !requestBody.isEmpty()) {
                Allure.addAttachment("📤 Request Body", "application/json", requestBody);
            }

            // Đính kèm Response Body (định dạng JSON)
            if (responseBody != null && !responseBody.isEmpty()) {
                Allure.addAttachment("📥 Response Body", "application/json", responseBody);
            }
        });
    }
}

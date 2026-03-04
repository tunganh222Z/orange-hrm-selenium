package allureReport;

import core.strategy.ReportListener;
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
        // 1. Ép trạng thái Test Case hiện tại thành FAILED (để nhuộm đỏ kết quả cuối cùng)
        Allure.getLifecycle().updateTestCase(tc -> tc.setStatus(Status.FAILED));

        // 2. Ép trạng thái của Step hiện tại thành FAILED (trước khi tạo sub-step)
        Allure.getLifecycle().updateStep(step -> step.setStatus(Status.FAILED));
        Allure.step(s, Status.FAILED);
        Allure.addAttachment("Screenshot Fail", new ByteArrayInputStream(bytes));
    }

    @Override
    public void onStepInfo(String s) {
        Allure.step(s, Status.PASSED);
    }
}

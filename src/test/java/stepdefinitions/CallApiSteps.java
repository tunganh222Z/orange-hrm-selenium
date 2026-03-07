package stepdefinitions;

import core.api.builder.CoreRequestBuilder;
import core.api.client.CallApi;
import core.api.response.ApiResponse;
import io.cucumber.java.en.Given;

public class CallApiSteps {

    @Given("Call api get {}")
    public void callApiGet(String endpoint) {
        CallApi callApi = new CallApi(new CoreRequestBuilder());
        callApi.getBuilderRequest().setUrl("https://simple-books-api.click").setAccessToken("123");
        ApiResponse res = callApi.requestGet(endpoint);}

}

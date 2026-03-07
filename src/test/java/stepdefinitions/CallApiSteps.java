package stepdefinitions;

import apiEngine.builder.CoreRequestBuilder;
import apiEngine.client.CallApi;
import apiEngine.response.ApiResponse;
import io.cucumber.java.en.Given;

public class CallApiSteps {

    @Given("Call api get {}")
    public void callApiGet(String endpoint) {
        CallApi callApi = new CallApi(new CoreRequestBuilder());
        callApi.getBuilderRequest().setUrl("https://simple-books-api.click").setAccessToken("123");
        ApiResponse res = callApi.requestGet(endpoint);}

}

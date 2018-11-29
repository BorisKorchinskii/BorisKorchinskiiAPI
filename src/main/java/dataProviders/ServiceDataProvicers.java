package dataProviders;

import org.testng.annotations.DataProvider;

import static io.restassured.http.Method.*;
import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;
import static org.apache.http.HttpStatus.SC_OK;

public class ServiceDataProvicers {

    @DataProvider(name = "methods provider")
    public Object[][] methodsProvider() {
        return new Object[][]{
                {GET, SC_OK},
                {POST, SC_OK},
                {PUT, SC_METHOD_NOT_ALLOWED},
                {DELETE, SC_METHOD_NOT_ALLOWED},
                {PATCH, SC_METHOD_NOT_ALLOWED},
        };
    }
}

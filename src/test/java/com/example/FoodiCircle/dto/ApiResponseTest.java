package com.example.FoodiCircle.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void success_withMessageAndData_returnsCorrectResponse() {
        ApiResponse<String> response = ApiResponse.success("OK", "hello");

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getData()).isEqualTo("hello");
    }

    @Test
    void success_withMessageOnly_returnsNullData() {
        ApiResponse<Void> response = ApiResponse.success("OK");

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getData()).isNull();
    }

    @Test
    void constructor_setsAllFields() {
        ApiResponse<Integer> response = new ApiResponse<>(404, "Not Found", 42);

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("Not Found");
        assertThat(response.getData()).isEqualTo(42);
    }
}

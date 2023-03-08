package com.kgromov.protobuf.demo;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class HelloWorldTest {

    @Test
    public void buildDefaultRequest_valuesAreEmpty() {
        HelloWorldRequest defaultInstance = HelloWorldRequest.getDefaultInstance();
        String requestId = defaultInstance.getRequestId();
        String message = defaultInstance.getMessage();

        assertThat(defaultInstance.getRequestId()).isNullOrEmpty();
        assertThat(defaultInstance.getMessage()).isNullOrEmpty();
    }

    @Test
    public void buildRequestFromBytes_DeserializedValuesAreTheSame() throws InvalidProtocolBufferException {
        HelloWorldRequest request = HelloWorldRequest.newBuilder()
                .setRequestId("My request id")
                .setMessage("Some message")
                .build();
        byte[] requestBytes = request.toByteArray();
        HelloWorldRequest parsedRequest = HelloWorldRequest.parseFrom(requestBytes);

        assertThat(parsedRequest.getRequestId()).isEqualTo(request.getRequestId());
        assertThat(parsedRequest.getMessage()).isEqualTo(request.getMessage());
    }

    @Test
    public void buildRequestFrom_writeBytesToFile_deserializeSuccessfully() throws IOException {
        //when
        HelloWorldRequest request = HelloWorldRequest.newBuilder()
                .setRequestId("My request id")
                .setMessage("Some message")
                .build();
        try(FileOutputStream fos = new FileOutputStream("hello-world")) {
            request.writeTo(fos);
        }

        //then
        try( FileInputStream fis = new FileInputStream("hello-world")) {
            HelloWorldRequest deserializedRequest = HelloWorldRequest.newBuilder().mergeFrom(fis).build();

            assertThat(deserializedRequest.getRequestId()).isEqualTo(request.getRequestId());
            assertThat(deserializedRequest.getMessage()).isEqualTo(request.getMessage());
        }
    }
}
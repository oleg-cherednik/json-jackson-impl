/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package ru.olegcherednik.jackson_utils;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author Oleg Cherednik
 * @since 03.11.2023
 */
@Test
public class ReadBroadbandTest {

    public void shouldDeserializeModelWithMap() throws IOException {
        assertThatCode(() -> {
            String json = getResourceAsString("/broadband.json");
            JacksonUtils.readValue(json, DataModel.class);
        }).doesNotThrowAnyException();
    }

    private static String getResourceAsString(String name) throws IOException {
        try (InputStream in = ReadBroadbandTest.class.getResourceAsStream(name)) {
            return IOUtils.toString(Objects.requireNonNull(in), StandardCharsets.UTF_8);
        }
    }

    @Getter
    @Setter
    public static class DataModel {

        private Integer code;
        private String message;
        private Data data;

        @Getter
        @Setter
        public static class Data {

            private List<Result> results;

        }

        @Getter
        @Setter
        public static class Result {

            private String lineId;
            private LineDiagnostics lineDiagnostics;
            private Integer analysisDay;

        }

        @Getter
        @Setter
        public static class LineDiagnostics {

            private Map<String, LineDiagnostic> lineDiagnostics = new HashMap<>();

            @JsonAnySetter
            public void setLineDiagnostic(String key, LineDiagnostic value) {
                lineDiagnostics.put(key, value);
            }

        }

        @Getter
        @Setter
        public static class LineDiagnostic {

            private Ap ap;
            @JsonProperty("interface")
            private Object anInterface;
            private Object station;
            private Object multiWan;

        }

        @Getter
        @Setter
        @SuppressWarnings("NewClassNamingConvention")
        public static class Ap {

            @JsonProperty("broadbanddsthroughput")
            private BroadbandDsThroughput broadbandDsThroughput;
            @JsonProperty("broadbandusthroughput")
            private BroadbandUsThroughput broadbandUsThroughput;

        }

        @Getter
        @Setter
        public static class BroadbandDsThroughput {

            private List<BroadbandThroughput> broadbandDsThroughput;

        }

        @Getter
        @Setter
        public static class BroadbandUsThroughput {

            private List<BroadbandThroughput> broadbandUsThroughput;

        }

        @Getter
        @Setter
        public static class BroadbandThroughput {

            private Double average;
            private Double std;
            private Double detection;
            private Integer numErrorFreeSamples;
            private Integer sampleMaxPercentile;
            private Long latestSampleTimestamp;
            private Integer sampleMax;
            private String url;
            private Integer videoQuality;
            private Integer serviceDetection;
            private List<Integer> percentile;
            private Integer latestSample;
            private String primaryIp;
            @JsonProperty("speedTestTrafficMB")
            private Double speedTestTrafficMb;

        }

    }

}

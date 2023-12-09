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

package ru.olegcherednik.json.jackson.utils;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 03.11.2023
 */
@Test
@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
public class ReadBroadbandTest {

    public void shouldDeserializeModelWithMap() throws IOException {
        String json = ResourceData.getResourceAsString("/broadband.json");
        DataModel actual = Json.readValue(json, DataModel.class);
        DataModel expected = getDataModel();
        assertThat(actual).isEqualTo(expected);
    }

    private static DataModel getDataModel() {
        DataModel dataModel = new DataModel();
        dataModel.setCode(1000);
        dataModel.setMessage("Success");
        dataModel.setData(getData());
        return dataModel;
    }

    private static DataModel.Data getData() {
        DataModel.Data data = new DataModel.Data();
        data.setResults(Collections.singletonList(getResult()));
        return data;
    }

    private static DataModel.Result getResult() {
        DataModel.Result result = new DataModel.Result();
        result.setLineId("C4000LG2020253739");
        result.setLineDiagnostics(getLineDiagnostics());
        result.setAnalysisDay(20231024);
        return result;
    }

    private static DataModel.LineDiagnostics getLineDiagnostics() {
        DataModel.LineDiagnostics lineDiagnostics = new DataModel.LineDiagnostics();
        lineDiagnostics.setLineDiagnostic("C4000LG2020253739", getLineDiagnostic());
        return lineDiagnostics;
    }

    private static DataModel.LineDiagnostic getLineDiagnostic() {
        DataModel.LineDiagnostic lineDiagnostic = new DataModel.LineDiagnostic();
        lineDiagnostic.setAp(getAp());
        lineDiagnostic.setAnInterface(Collections.emptyMap());
        lineDiagnostic.setStation(Collections.emptyMap());
        lineDiagnostic.setMultiWan(Collections.emptyMap());
        return lineDiagnostic;
    }

    private static DataModel.Ap getAp() {
        DataModel.Ap ap = new DataModel.Ap();
        ap.setBroadbandDsThroughput(getBroadbandDsThroughput());
        ap.setBroadbandUsThroughput(getBroadbandUsThroughput());
        return ap;
    }

    private static DataModel.BroadbandDsThroughput getBroadbandDsThroughput() {
        DataModel.BroadbandDsThroughput broadbandDsThroughput = new DataModel.BroadbandDsThroughput();
        broadbandDsThroughput.setBroadbandDsThroughput(Collections.singletonList(getBroadbandThroughputDs()));
        return broadbandDsThroughput;
    }

    private static DataModel.BroadbandThroughput getBroadbandThroughputDs() {
        DataModel.BroadbandThroughput broadbandThroughput = new DataModel.BroadbandThroughput();
        broadbandThroughput.setAverage(104830.);
        broadbandThroughput.setStd(0.);
        broadbandThroughput.setDetection(0.);
        broadbandThroughput.setNumErrorFreeSamples(1);
        broadbandThroughput.setSampleMaxPercentile(107673);
        broadbandThroughput.setLatestSampleTimestamp(1698160893000L);
        broadbandThroughput.setSampleMax(107673);
        broadbandThroughput.setUrl("http://blah-blah.com");
        broadbandThroughput.setVideoQuality(7);
        broadbandThroughput.setServiceDetection(-1);
        broadbandThroughput.setPercentile(Collections.singletonList(104830));
        broadbandThroughput.setLatestSample(104830);
        broadbandThroughput.setPrimaryIp("205.171.3.100");
        broadbandThroughput.setSpeedTestTrafficMb(56.9910995);
        return broadbandThroughput;
    }

    private static DataModel.BroadbandUsThroughput getBroadbandUsThroughput() {
        DataModel.BroadbandUsThroughput broadbandUsThroughput = new DataModel.BroadbandUsThroughput();
        broadbandUsThroughput.setBroadbandUsThroughput(Collections.singletonList(getBroadbandThroughputUs()));
        return broadbandUsThroughput;
    }

    private static DataModel.BroadbandThroughput getBroadbandThroughputUs() {
        DataModel.BroadbandThroughput broadbandThroughput = new DataModel.BroadbandThroughput();
        broadbandThroughput.setAverage(37828.);
        broadbandThroughput.setStd(0.);
        broadbandThroughput.setNumErrorFreeSamples(1);
        broadbandThroughput.setSampleMaxPercentile(38393);
        broadbandThroughput.setLatestSampleTimestamp(1698160893000L);
        broadbandThroughput.setSampleMax(38393);
        broadbandThroughput.setUrl("http://blah-blah.com");
        broadbandThroughput.setServiceDetection(-1);
        broadbandThroughput.setPercentile(Collections.singletonList(37828));
        broadbandThroughput.setLatestSample(37828);
        broadbandThroughput.setPrimaryIp("205.171.3.100");
        broadbandThroughput.setSpeedTestTrafficMb(21.8231345);
        return broadbandThroughput;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class DataModel {

        private Integer code;
        private String message;
        private Data data;

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class Data {

            private List<Result> results;

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class Result {

            private String lineId;
            private LineDiagnostics lineDiagnostics;
            private Integer analysisDay;

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class LineDiagnostics {

            private Map<String, LineDiagnostic> lineDiagnostics = new HashMap<>();

            @JsonAnySetter
            public void setLineDiagnostic(String key, LineDiagnostic value) {
                lineDiagnostics.put(key, value);
            }

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class LineDiagnostic {

            private Ap ap;
            @JsonProperty("interface")
            private Object anInterface;
            private Object station;
            private Object multiWan;

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        @SuppressWarnings({ "NewClassNamingConvention", "PMD.ShortClassName" })
        public static class Ap {

            @JsonProperty("broadbanddsthroughput")
            private BroadbandDsThroughput broadbandDsThroughput;
            @JsonProperty("broadbandusthroughput")
            private BroadbandUsThroughput broadbandUsThroughput;

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class BroadbandDsThroughput {

            private List<BroadbandThroughput> broadbandDsThroughput;

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class BroadbandUsThroughput {

            private List<BroadbandThroughput> broadbandUsThroughput;

        }

        @Getter
        @Setter
        @EqualsAndHashCode
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

/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonException;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.api.enumid.EnumId;
import ru.olegcherednik.json.api.enumid.EnumIdJsonCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author Oleg Cherednik
 * @since 17.10.2021
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class EnumIdTest {

    public void shouldRetrieveJsonWhenEnumIdValue() {
        Data data = new Data(Auto.AUDI, Color.RED);
        String json = Json.writeValue(data);
        assertThat(json).isEqualTo("{\"notNullAuto\":\"audi\",\"notNullColor\":\"Red\"}");
    }

    @SuppressWarnings("ConstantConditions")
    public void shouldParseJsonWhenEnumIdValue() {
        String json = "{\"notNullAuto\":\"bmw\",\"notNullColor\":\"Green\"}";
        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.notNullAuto).isSameAs(Auto.BMW);
        assertThat(actual.notNullColor).isSameAs(Color.GREEN);
        assertThat(actual.nullAuto).isNull();
        assertThat(actual.nullColor).isNull();
    }

    public void shouldRetrieveJsonWithNullWhenEnumIdValueAndSerializeNull() {
        String json = Json.createWriter(JsonSettings.builder().serializeNull(true).build())
                          .writeValue(new Data(Auto.MERCEDES, Color.BLUE));
        assertThat(json).isEqualTo("{\"notNullAuto\":\"mercedes\",\"notNullColor\":\"Blue\","
                                           + "\"nullAuto\":null,\"nullColor\":null}");
    }

    public void shouldRetrieveJsonWithNullWhenEnumIdValueAndSerializeNullAngGetters() {
        Book data = new Book();
        data.setNotNullAuto(Auto.MERCEDES);
        data.setNotNullColor(Color.BLUE);

        String json = Json.createWriter(JsonSettings.builder().serializeNull(true).build())
                          .writeValue(data);
        assertThat(json).isEqualTo("{\"notNullAuto\":\"mercedes\",\"notNullColor\":\"Blue\","
                                           + "\"nullAuto\":null,\"nullColor\":null}");
    }

    public void shouldThrowJsonExceptionWhenReadEnumIdNoFactoryMethod() {
        String json = Json.writeValue(City.SAINT_PETERSBURG);
        assertThat(json).isEqualTo("\"Saint-Petersburg\"");

        assertThatCode(() -> Json.readValue(json, City.class))
                .isExactlyInstanceOf(JsonException.class);
    }

    public void shouldUseJsonCreatorAnnotatedMethodWhenParseIdAlsoExists() {
        String json = "\"Square_jsonCreator\"";
        Shape actual = Json.readValue(json, Shape.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isSameAs(Shape.SQUARE);
    }

    public void shouldThrowJsonExceptionWhenDeserializeWithMultipleJsonCreatorMethods() {
        String json = Json.writeValue(Vodka.SMIRNOFF);
        assertThat(json).isEqualTo("\"smirnoff\"");
        assertThatCode(() -> Json.readValue(json, Vodka.class))
                .isExactlyInstanceOf(JsonException.class);
    }

    public void shouldThrowJsonExceptionWithOriginalMessageWhenUseCustomFactoryMethod() {
        String json = Json.writeValue(People.OLEG_CHEREDNIK);
        assertThat(json).isEqualTo("\"oleg-cherednik\"");
        assertThatCode(() -> Json.readValue(json, People.class))
                .isExactlyInstanceOf(JsonException.class);
    }

    public void shouldIgnoreNotCorrectFactoryMethodWhenMultiplePotentialFactoryMethodsExist() {
        String json = Json.writeValue(Country.RUSSIAN_FEDERATION);
        assertThat(json).isEqualTo("\"russian-federation\"");
        Country actual = Json.readValue(json, Country.class);
        assertThat(actual).isSameAs(Country.RUSSIAN_FEDERATION);
    }

    public void shouldUseNameWhenNoGetId() {
        assertThat(Json.writeValue(Shape.SQUARE)).isEqualTo("\"SQUARE\"");
    }

    public void shouldParseByNameCaseInsensitive() {
        assertThat(EnumId.parseName(People.class, "OLEG_CHEREDNIK")).isSameAs(People.OLEG_CHEREDNIK);
        assertThat(EnumId.parseName(People.class, "Oleg_Cherednik")).isSameAs(People.OLEG_CHEREDNIK);
    }

    public void shouldThrowExceptionWhenConstantByNameWasNotFound() {
        assertThatCode(() -> EnumId.parseName(People.class, "UNKNOWN"))
                .isExactlyInstanceOf(EnumConstantNotPresentException.class);
    }

    public void shouldRetrieveDefaultValueWhenConstantByNameWasNotFound() {
        People actual = EnumId.parseName(People.class, "UNKNOWN", People.OLEG_CHEREDNIK);
        assertThat(actual).isSameAs(People.OLEG_CHEREDNIK);
    }

    public void shouldRetrieveFoundConstantWhenFindByIdOrName() {
        assertThat(EnumId.parseIdOrName(People.class, "OLEG_CHEREDNIK")).isSameAs(People.OLEG_CHEREDNIK);
        assertThat(EnumId.parseIdOrName(People.class, "Oleg_Cherednik")).isSameAs(People.OLEG_CHEREDNIK);
        assertThat(EnumId.parseIdOrName(People.class, "oleg-cherednik")).isSameAs(People.OLEG_CHEREDNIK);
        assertThat(EnumId.parseIdOrName(People.class, "Oleg-Cherednik")).isSameAs(People.OLEG_CHEREDNIK);
    }

    public void shouldThrowExceptionWhenConstantByNameOrIdWasNotFound() {
        assertThatCode(() -> EnumId.parseIdOrName(People.class, "UNKNOWN"))
                .isExactlyInstanceOf(EnumConstantNotPresentException.class);
    }

    public void shouldRetrieveDefaultValueWhenConstantByIdWasNotFound() {
        People actual = EnumId.parseId(People.class, "UNKNOWN", People.OLEG_CHEREDNIK);
        assertThat(actual).isSameAs(People.OLEG_CHEREDNIK);
    }

    public void shouldThrowExceptionWhenConstantByIdWasNotFound() {
        assertThatCode(() -> EnumId.parseId(People.class, "UNKNOWN"))
                .isExactlyInstanceOf(EnumConstantNotPresentException.class);
    }

    public void shouldReadWriteConstantWithNullId() {
        Data data = new Data(Auto.AUDI, Color.NONE);
        String json = Json.writeValue(data);
        assertThat(json).isEqualTo("{\"notNullAuto\":\"audi\"}");

        json = "{\"notNullAuto\":\"audi\",\"notNullColor\":null}";
        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(new Data(Auto.AUDI, Color.NONE));
    }

    public void shouldRetrieveDefaultWhenObjectNull() {
        assertThat(EnumId.getId(Auto.AUDI, Auto.BMW)).isSameAs(Auto.AUDI.getId());
        assertThat(EnumId.getId(null, Auto.BMW)).isSameAs(Auto.BMW.getId());
    }

    @EqualsAndHashCode
    private static final class Data {

        private final Auto notNullAuto;
        private final Color notNullColor;
        private final Auto nullAuto = null;
        private final Color nullColor = null;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Data(@JsonProperty("notNullAuto") Auto notNullAuto,
                     @JsonProperty("notNullColor") Color notNullColor) {
            this.notNullAuto = notNullAuto;
            this.notNullColor = notNullColor;
        }

    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum Auto implements EnumId {

        AUDI("audi"),
        BMW("bmw"),
        MERCEDES("mercedes");

        private final String id;

        @EnumIdJsonCreator
        @SuppressWarnings("unused")
        public static Auto parseId(String id) {
            return EnumId.parseId(Auto.class, id);
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum Color implements EnumId {

        RED("Red"),
        GREEN("Green"),
        BLUE("Blue"),
        NONE(null);

        private final String id;

        @SuppressWarnings("unused")
        public static Color parseId(String id) {
            return EnumId.parseId(Color.class, id);
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum City implements EnumId {

        SAINT_PETERSBURG("Saint-Petersburg");

        private final String id;

    }

    @SuppressWarnings("unused")
    private enum Shape implements EnumId {

        SQUARE;

        @EnumIdJsonCreator
        public static Shape jsonCreator(String id) {
            return "Square_jsonCreator".equals(id) ? SQUARE : null;
        }

        public static Shape parseId(String id) {
            return "Square_parseId".equals(id) ? SQUARE : null;
        }
    }

    @SuppressWarnings("unused")
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum Vodka implements EnumId {

        SMIRNOFF("smirnoff");

        private final String id;

        @EnumIdJsonCreator
        public static Vodka one(String id) {
            return EnumId.parseId(Vodka.class, id);
        }

        @EnumIdJsonCreator
        public static Vodka two(String id) {
            return EnumId.parseId(Vodka.class, id);
        }

    }

    @SuppressWarnings("unused")
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum People implements EnumId {

        OLEG_CHEREDNIK("oleg-cherednik");

        private final String id;

        @EnumIdJsonCreator
        public static People one(String id) {
            throw new JsonException("Factory method problem");
        }
    }

    @SuppressWarnings("unused")
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum Country implements EnumId {

        RUSSIAN_FEDERATION("russian-federation");

        private final String id;

        @EnumIdJsonCreator
        public static Country one(int id) {
            throw new JsonException("Factory method (int) problem");
        }

        @EnumIdJsonCreator
        public static Country two(String id, int param) {
            throw new JsonException("Factory method (two arguments) problem");
        }

        @EnumIdJsonCreator
        public static Country three(String id) {
            return EnumId.parseId(Country.class, id);
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    private static final class Book {

        private Auto notNullAuto;
        private Color notNullColor;
        private Auto nullAuto;
        private Color nullColor;

    }

}

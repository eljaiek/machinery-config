package com.github.eljaiek.machinery.config.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.collections.impl.collector.Collectors2.makeString;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@TestMethodOrder(MethodName.class)
class PropertyImplTests {

  final String key = "time.unit";
  final String value = DAYS.toString();

  @Test
  void asTextShouldNotReturnEmpty() {
    // Given
    var property = new PropertyImpl("email.sender", "jhon.doe@domain.com", x -> {});

    // Then
    assertThat(property.asText()).isEqualTo("jhon.doe@domain.com");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void asTextShouldReturnEmptyString(String value) {
    // Given
    var property = new PropertyImpl("email.sender", value, x -> {});

    // Then
    assertThat(property.asText()).isEmpty();
  }

  @Test
  void asIntShouldNotReturnZero() {
    // Given
    var property = new PropertyImpl("server.port", "80", x -> {});

    // Then
    assertThat(property.asInt()).isEqualTo(80);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void asIntShouldReturnZero(String value) {
    // Given
    var property = new PropertyImpl("server.port", value, x -> {});

    // Then
    assertThat(property.asInt()).isZero();
  }

  @Test
  void asLongShouldNotReturnZero() {
    // Given
    var property = new PropertyImpl("server.port", "8000", x -> {});

    // Then
    assertThat(property.asLong()).isEqualTo(8000L);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = "  ")
  void asLongShouldReturnZero(String value) {
    // Given
    var property = new PropertyImpl("server.port", value, x -> {});

    // Then
    assertThat(property.asLong()).isZero();
  }

  @Test
  void asFloatShouldNotReturnZero() {
    // Given
    var property = new PropertyImpl("margin.top", "0.5", x -> {});

    // Then
    assertThat(property.asFloat()).isEqualTo(0.5F);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void asFloatShouldReturnZero(String value) {
    // Given
    var property = new PropertyImpl("margin.top", value, x -> {});

    // Then
    assertThat(property.asFloat()).isEqualTo(0.0F);
  }

  @ParameterizedTest
  @ValueSource(strings = {"True", "80"})
  void asBooleanShouldReturnTrue(String value) {
    // Given
    var property = new PropertyImpl("mail.enable", value, x -> {});

    // Then
    assertThat(property.asBoolean()).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"0", "  "})
  void asBooleanShouldReturnFalse(String value) {
    // Given
    var property = new PropertyImpl("mail.enable", value, x -> {});

    // Then
    assertThat(property.asBoolean()).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "0.0", "12", "12.004", "-1"})
  void isNumberShouldReturnTrue(String value) {
    // Given
    var property = new PropertyImpl("margin.top", value, x -> {});

    // Then
    assertThat(property.isNumeric()).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "TRUE", "12FE", "ER12.004", "FDefer*"})
  void isNumberShouldReturnFalse(String value) {
    // Given
    var property = new PropertyImpl("margin.top", value, x -> {});

    // Then
    assertThat(property.isNumeric()).isFalse();
  }

  @Test
  void mapShouldNotReturnEmpty() {
    // Given
    var property = new PropertyImpl(key, value, x -> {});

    // Then
    assertThat(property.map(TimeUnit::valueOf)).isPresent().get().isEqualTo(DAYS);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void mapShouldReturnEmpty(String value) {
    // Given
    var property = new PropertyImpl(key, value, x -> {});

    // Then
    assertThat(property.map(TimeUnit::valueOf)).isEmpty();
  }

  @Test
  void asListShouldNotReturnEmptyList() {
    // Given
    var timeUnits = TimeUnit.values();
    var value = Stream.of(timeUnits).map(TimeUnit::toString).collect(makeString(" "));
    var property = new PropertyImpl(key, value, x -> {});

    // Then
    assertThat(property.asList(TimeUnit::valueOf)).contains(timeUnits);
  }

  @Test
  void asListShouldReturnListWithOnlyOneElement() {
    // Given
    var property = new PropertyImpl(key, value, x -> {});

    // Then
    assertThat(property.asList(TimeUnit::valueOf)).hasSize(1);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = "  ")
  void asListShouldReturnEmptyList(String value) {
    // Given
    var property = new PropertyImpl(key, value, x -> {});

    // Then
    assertThat(property.asList(String::toUpperCase)).isEmpty();
  }

  @Test
  void asListShouldReturnNotReturnEmptyListUsingCustomSplitSeparator() {
    // Given
    var timeUnits = TimeUnit.values();
    var value = Stream.of(timeUnits).map(TimeUnit::toString).collect(makeString(","));
    var property = new PropertyImpl(key, value, x -> {});

    // Then
    assertThat(property.asList(TimeUnit::valueOf, ",")).contains(timeUnits);
  }
}

package com.github.eljaiek.machinery.config.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = RedisModuleTestConfiguration.class)
class RedisPropertyRepositoryTest {

  final String key = "mail.server.alias";
  final String value = "Administrator";
  final String namespace = "mail.server";

  @Autowired RedisPropertyRepository propertyRepository;
  @Autowired PropertyHashRepository propertyHashRepository;

  @AfterEach
  void tearDown() {
    propertyHashRepository.deleteAll();
  }

  @Test
  void getValueShouldReturnExpectedValue() {
    // When
    propertyHashRepository.save(new PropertyHash(key, value));

    // Then
    assertThat(propertyRepository.getValue(key)).isEqualTo(value);
  }

  @Test
  void findAllByNamespaceShouldExpectedValues() {
    // Given
    var expected =
        Map.of(
            key,
            value,
            "mail.server.enabled",
            "true",
            "mail.server.host",
            "smtp.googlemail.com",
            "mail.server.port",
            "587",
            "mail.server.user.name",
            "admin@gmail.com",
            "mail.server.user.password",
            "admin123");

    // When
    propertyRepository.save(expected);
    propertyRepository.save("server.url", "https://localhost");
    var actual = propertyRepository.findAllByNamespace(namespace);

    // Then
    assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
  }

  @Test
  void saveShouldUpdateValueOfExistingProperty() {
    // When
    propertyRepository.save(key, value);
    propertyRepository.save(key, "Admin");
    var entity = propertyHashRepository.findById(key).orElseThrow();

    // Then
    assertThat(entity.getValue()).isEqualTo("Admin");
  }
}

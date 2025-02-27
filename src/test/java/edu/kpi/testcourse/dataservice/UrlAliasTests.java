package edu.kpi.testcourse.dataservice;

import edu.kpi.testcourse.urlservice.UrlService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static edu.kpi.testcourse.dataservice.UserTests.Generator;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;

public class UrlAliasTests extends DataServiceImplTest {

  @BeforeEach
  void addTestUser() {
    dataService.addUser(testUser);
  }

  @Test
  void addAlias() {
    var result = dataService.addUrlAlias(testUrlAlias);

    assertThat(result).isTrue();
  }

  @Test
  void addAliasIfAliasExists(){
    var firstResult = dataService.addUrlAlias(testUrlAlias);
    var secondResult = dataService.addUrlAlias(testUrlAlias);

    assertThat(secondResult).isFalse();
  }

  @Test
  void addAliasIfUserNotExists() {
    var urlAlias = new UrlAlias("test", "test", "notExists");
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> dataService.addUrlAlias(urlAlias));
  }

  @Test
  void getAlias() {
    dataService.addUrlAlias(testUrlAlias);
    var result = dataService.getUrlAlias(testUrlAlias.getAlias());

    assertThat(result.getUrl()).isEqualTo(testUrlAlias.getUrl());
  }

  @Test
  void getAliasIfAliasNotFound() {
    var result = dataService.getUrlAlias("wrongAlias");

    assertThat(result).isNull();
  }


  @Test
  void deleteAliasReturnsTrue() {
    dataService.addUrlAlias(testUrlAlias);
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias(), testUser.getEmail());

    assertThat(result).isTrue();
  }

  @Test
  void deleteAliasIfAliasNotFound() {
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias(), testUser.getEmail());

    assertThat(result).isFalse();
  }

  @Test
  void deleteAliasIfWrongUser() {
    dataService.addUrlAlias(testUrlAlias);
    var result = dataService.deleteUrlAlias(testUrlAlias.getAlias(), "wrongEmail");

    assertThat(result).isFalse();
  }

  @Test
  void deleteAlias() {
    dataService.addUrlAlias(testUrlAlias);
    dataService.deleteUrlAlias(testUrlAlias.getAlias(), testUser.getEmail());
    var result = dataService.getUrlAlias(testUrlAlias.getAlias());

    assertThat(result).isNull();
  }

  @Test
  void getUserAliases() {
    var user2 = "user2";
    dataService.addUser(new User(testUser.getEmail(), ""));
    dataService.addUser(new User(user2, ""));

    var testUserAliases = Arrays.asList("alias1", "alias2", "alias3");
    for (var alias: testUserAliases) {
      dataService.addUrlAlias(new UrlAlias(alias, "url", testUser.getEmail()));
    }
    dataService.addUrlAlias(new UrlAlias("alias4", "url", user2));
    dataService.addUrlAlias(new UrlAlias("alias5", "url", user2));

    var testUserUrlAliases = dataService.getUserAliases(testUser.getEmail());
    assertThat(testUserUrlAliases.size()).isEqualTo(3);
    assertThat(testUserUrlAliases.stream().map(UrlAlias::getAlias)
      .collect(Collectors.toList()).containsAll(testUserAliases)).isTrue();
  }


/*Uncontested(Honchar)*/

  @Test
  void AddRandomUrlAliasinDB() {
    var user1 = Generator();
    dataService.addUser(new User(user1,Generator()));

    var result = dataService.addUrlAlias(new UrlAlias(Generator(),Generator(),user1));
    assertThat(result).isNotNull();
  }

  @Test
  void AddRandomUrlAliasindbAndDelete() {

    User testUser1 = new User(Generator(), Generator());
    UrlAlias testUrlAlias1 = new UrlAlias(Generator(), Generator(), testUser1.getEmail());
    dataService.deleteUrlAlias(testUrlAlias1.getAlias(), testUser1.getEmail());
    var result = dataService.getUrlAlias(testUrlAlias1.getAlias());

    assertThat(result).isNull();


  }

}

package de.turing85.endpoint;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import de.turing85.persistence.CashMySQLRepository;
import de.turing85.persistence.CashPostgresRepository;
import de.turing85.persistence.entity.mysql.CashMySQL;
import de.turing85.persistence.entity.postgres.CashPostgres;
import io.quarkus.agroal.DataSource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@QuarkusTest
@TestHTTPEndpoint(CashEndpoint.class)
class CashEndpointTest {

  @Inject
  @DataSource("mysqldatasource")
  CashMySQLRepository mySqlRepository;

  @Inject
  @DataSource("postgresqldatasource")
  CashPostgresRepository postgresRepository;

  @Test
  void testPostAndGet() {
    // @formatter:off
    long[] ids = RestAssured
        .when().post()
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().as(long[].class);

    CashMySQL cashMySqlFromHttp = RestAssured
        .when().get("/mysql/%d".formatted(ids[0]))
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().as(CashMySQL.class);
    // @formatter:on
    CashMySQL cashMySqlFromDb = mySqlRepository.findById(ids[0]);
    assertThat(cashMySqlFromHttp).isEqualTo(cashMySqlFromDb);

    // @formatter:off
    CashPostgres cashPostgresFromHttp = RestAssured
        .when().get("/postgres/%d".formatted(ids[0]))
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().as(CashPostgres.class);
    // @formatter:on
    CashPostgres cashPostgresFromDb = postgresRepository.findById(ids[0]);
    assertThat(cashPostgresFromHttp).isEqualTo(cashPostgresFromDb);
  }
}

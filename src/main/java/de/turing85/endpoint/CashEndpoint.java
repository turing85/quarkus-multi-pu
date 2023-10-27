package de.turing85.endpoint;

import java.util.Date;
import java.util.Random;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import de.turing85.persistence.CashMySQLRepository;
import de.turing85.persistence.CashPostgresRepository;
import de.turing85.persistence.entity.mysql.CashMySQL;
import de.turing85.persistence.entity.postgres.CashPostgres;
import io.quarkus.agroal.DataSource;

@Path("cash")
@Produces(MediaType.APPLICATION_JSON)
public class CashEndpoint {
  private static final Random RANDOM = new Random();

  private final CashMySQLRepository mySqlRepository;
  private final CashPostgresRepository postgresRepository;

  public CashEndpoint(@DataSource("mysqldatasource") CashMySQLRepository mySqlRepository,
      @DataSource("postgresqldatasource") CashPostgresRepository postgresRepository) {
    this.mySqlRepository = mySqlRepository;
    this.postgresRepository = postgresRepository;
  }


  @POST
  @Transactional
  public int[] generateRandomEntry() {
    Date now = new Date();
    int amount = RANDOM.nextInt(1000);
    String createdBy = "foo" + RANDOM.nextInt();
    String updatedBy = "bar" + RANDOM.nextInt();
    // @formatter:off
    CashMySQL cashMySQL = CashMySQL.builder()
        .amount(amount)
        .createdBy(createdBy)
        .updatedBy(updatedBy)
        .createTime(now)
        .updateTime(now)
        .build();
    // @formatter:on
    mySqlRepository.persist(cashMySQL);

    // @formatter:off
    CashPostgres cashPostgres = CashPostgres.builder()
        .amount(amount)
        .createdBy(createdBy)
        .updatedBy(updatedBy)
        .createTime(now)
        .updateTime(now)
        .build();
    // @formatter:on
    postgresRepository.persist(cashPostgres);
    return new int[] {cashMySQL.getId(), cashPostgres.getId()};
  }

  @GET
  @Path("mysql/{id}")
  public CashMySQL getFromMySqlById(@PathParam("id") long id) {
    return mySqlRepository.findById(id);
  }

  @GET
  @Path("postgres/{id}")
  public CashPostgres getFromPostgresById(@PathParam("id") long id) {
    return postgresRepository.findById(id);
  }
}

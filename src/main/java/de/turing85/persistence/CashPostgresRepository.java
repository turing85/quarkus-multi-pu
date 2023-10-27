package de.turing85.persistence;

import jakarta.enterprise.context.ApplicationScoped;

import de.turing85.persistence.entity.postgres.CashPostgres;
import io.quarkus.agroal.DataSource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
@DataSource("postgresqldatasource")
public class CashPostgresRepository implements PanacheRepository<CashPostgres> {
}

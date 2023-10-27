package de.turing85.persistence;

import jakarta.enterprise.context.ApplicationScoped;

import de.turing85.persistence.entity.mysql.CashMySQL;
import io.quarkus.agroal.DataSource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
@DataSource("mysqldatasource")
public class CashMySQLRepository implements PanacheRepository<CashMySQL> {
}

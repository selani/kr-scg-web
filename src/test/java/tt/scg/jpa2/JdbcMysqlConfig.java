package tt.scg.jpa2;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "tt.scg.jpa", entityManagerFactoryRef = "mysqlFactoryBean", transactionManagerRef = "mysqlTransactionManager")
public class JdbcMysqlConfig {
	@Bean(name = "mysqlDataSource")
	@ConfigurationProperties(prefix = "spring.conn_msysql_test")
	public DataSource getDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "mysqlFactoryBean")
	public LocalContainerEntityManagerFactoryBean getFactoryBean(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(getDataSource()).packages("com.web.domain.mysql").build();
	}

	@Bean(name = "mysqlTransactionManager")
	PlatformTransactionManager getTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(getFactoryBean(builder).getObject());
	}
}

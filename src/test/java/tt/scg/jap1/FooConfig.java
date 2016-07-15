package tt.scg.jap1;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory1", basePackages = { "tt.scg.jap1" })
public class FooConfig {

	@Primary
	@Bean(name="datasource_1")
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource getDataSource() {
		return null;
	}
}

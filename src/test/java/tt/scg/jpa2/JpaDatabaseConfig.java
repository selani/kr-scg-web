package tt.scg.jpa2;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public interface JpaDatabaseConfig {
	DataSource getDataSource();

	LocalContainerEntityManagerFactoryBean getFactoryBean(EntityManagerFactoryBuilder builder);

	PlatformTransactionManager getTransactionManager(EntityManagerFactoryBuilder builder);
}

package com.ntg.sadmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Configuration
public class DBMigrationStart {

	private static final Logger logger = LoggerFactory.getLogger(DBMigrationStart.class);

	@EventListener(ApplicationReadyEvent.class)
	@Order(1)
	public void runScriptsgAfterStartup() {

		logger.info("*********************START RUNNING SCRIPTS******************************");
		if (DBMigration.DBMigrationInst != null) {
			DBMigration.DBMigrationInst.StartDBMigrationJobs();
		}
	}

}

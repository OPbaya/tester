package com.api.tester.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.api.tester.repository")
// due to "extends AbstractMongoClientConfiguration" it was unable to connect to the atlas(was conecting to compass), so I removed it and it works fine without it
public class MongoConfig  {

    // @Override
    // protected String getDatabaseName() {
    //     return "api_tester_db";
    // }
}

// ```

//     **What this does:**-`@EnableMongoRepositories`—

//     tells Spring
//     where to look for
//     your MongoDB repositories,in this case`HistoryRepository.java`-`getDatabaseName()` — tells Spring which database to use inside your Atlas cluster

// ---

// **Step 9 — Verify the connection**

// Restart your Spring Boot server. In the terminal you should see something like:
// ```
// Cluster created with settings {hosts=[cluster0.xxxxx.mongodb.net:27017]...}
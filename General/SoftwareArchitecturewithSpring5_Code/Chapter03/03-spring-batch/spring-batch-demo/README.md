# Spring Batch

This project contains a demo which shows how to use Spring Batch. The provided example will load all the provided information in the file java-champions.csv into a database table.


In order to run the project, you will need to execute the class `DemoSpringBatchApplication` which will produce the following output:


```sh

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
...

// The Job configuration is done
2018-08-16 22:48:51.562  WARN 2628 --- [           main] o.s.c.a.ConfigurationClassEnhancer       : @Bean method ScopeConfiguration.stepScope is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean javadoc for complete details.
2018-08-16 22:48:51.584  WARN 2628 --- [           main] o.s.c.a.ConfigurationClassEnhancer       : @Bean method ScopeConfiguration.jobScope is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean javadoc for complete details.
2018-08-16 22:48:52.001  INFO 2628 --- [           main] com.zaxxer.hikari.HikariDataSource       : testdb - Starting...
2018-08-16 22:48:52.182  INFO 2628 --- [           main] com.zaxxer.hikari.HikariDataSource       : testdb - Start completed.

...
// The db schema to load the data is created
2018-08-16 22:48:52.213  INFO 2628 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executing SQL script from URL [file:/Users/moe/Documents/Proyectos/spring-architectures-with-spring/chapter03/03-spring-batch/spring-batch-demo/out/production/resources/schema.sql]
2018-08-16 22:48:52.256  INFO 2628 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executed SQL script from URL [file:/Users/moe/Documents/Proyectos/spring-architectures-with-spring/chapter03/03-spring-batch/spring-batch-demo/out/production/resources/schema.sql] in 43 ms.

...

// The jobs starts to run
2018-08-16 22:48:54.918  INFO 2628 --- [           main] o.s.b.a.b.JobLauncherCommandLineRunner   : Running default command line with: []

// The file is loaded
2018-08-16 22:48:55.276  INFO 2628 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=javaChampionsLoaderJob]] launched with the following parameters: [Job: [FlowJob: [name=javaChampionsLoaderJob]] launched with the following parameters: [{}]]
2018-08-16 22:48:55.325  INFO 2628 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [LoadJavaChampionsFromFile]

// The information is mapped as POJOs and persisted into the DB
2018-08-16 22:48:55.449  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Oezcan, lastName=Acar, country=Turkey, year=2009)
2018-08-16 22:48:55.450  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Dan, lastName=Allen, country=USA, year=2013)
2018-08-16 22:48:55.450  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Dion, lastName=Almaer, country=USA, year=2005)
2018-08-16 22:48:55.450  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Andres, lastName=Almiray, country=Mexico, year=2010)
2018-08-16 22:48:55.450  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Deepak, lastName=Alur, country=USA, year=2006)
2018-08-16 22:48:55.450  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Gail C., lastName=Anderson, country=USA, year=2016)
2018-08-16 22:48:55.451  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Paul L., lastName=Anderson, country=USA, year=2016)
2018-08-16 22:48:55.451  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Fernando, lastName=Anselmo, country=Brazil, year=2006)
2018-08-16 22:48:55.451  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Paris, lastName=Apostolopoulos, country=Greece, year=2007)
2018-08-16 22:48:55.451  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Panos, lastName=Konstantinidis, country=Greece, year=2007)
2018-08-16 22:48:55.515  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Anton, lastName=Arhipov, country=Estonia, year=2014)
2018-08-16 22:48:55.521  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Sergey, lastName=Astakhov, country=Russia, year=2005)
2018-08-16 22:48:55.521  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Calvin, lastName=Austin, country=USA, year=2005)
2018-08-16 22:48:55.521  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Hovhannes, lastName=Avoyan, country=Armenia, year=2015)
2018-08-16 22:48:55.521  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Lucio, lastName=Benfante, country=Italy, year=2006)
2018-08-16 22:48:55.521  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Geert, lastName=Bevin, country=Belgium, year=2006)
2018-08-16 22:48:55.521  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Emmanuel, lastName=Bernard, country=France, year=2017)
2018-08-16 22:48:55.522  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Adam, lastName=Bien, country=Germany, year=2007)
2018-08-16 22:48:55.522  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Xu, lastName=Bin, country=China, year=2005)
2018-08-16 22:48:55.522  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=David, lastName=Blevins, country=USA, year=2015)
2018-08-16 22:48:55.541  INFO 2628 --- [           main] c.p.d.configuration.BatchConfiguration   : Java Champion found: JavaChampion(firstName=Joshua, lastName=Bloch, country=USA, year=2005)

// The Job is completed
2018-08-16 22:48:55.571  INFO 2628 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=javaChampionsLoaderJob]] completed with the following parameters: [Job: [FlowJob: [name=javaChampionsLoaderJob]] completed with the following parameters: [{}] and the following status: [COMPLETED]] and the following status: [COMPLETED]
```
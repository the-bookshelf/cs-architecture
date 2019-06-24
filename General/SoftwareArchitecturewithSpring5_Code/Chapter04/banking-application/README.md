# Server

This project represents the server within a server-client architecture.

You can run the project using the main class:

- BankingApplication

You can visit the API documentation using the provided swagger ui:

`
http://localhost:8080/swagger-ui.html
`


This project also contains the client that can be later consumed.
First check what your IP address is and the replace it in the BankClient file

```java
public static final String SERVICE_BASE_URL = "http://192.168.100.4:8080/";
```

Later, inside the banking-client project, run the following gradle task

```js
$ ../gradlew install
```

It will publish the artifact in the maven local repository so can later us it.


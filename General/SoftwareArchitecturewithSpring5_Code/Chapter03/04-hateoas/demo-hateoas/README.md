# Spring HATEOAS

This project shows how to use Spring HATEOAS to allow discovering the API operations available.


You can query two endpoints that were created as examples:


- Query a customer information

`
GET http://localhost:8080/customers/1
`

It will produce the following output:

```json
{
    "name": "Rene Enriquez",
    "customerId": 1,
    "_links": {
        "self": {
            "href": "http://localhost:8080/customers/1"
        },
        "bankStatements": {
            "href": "http://localhost:8080/customer/1/bankStatements"
        }
    }
}
```

- Check the bank statements

`
GET http://localhost:8080/customer/1/bankStatements
`

It will produce the following output:

```json
{
    "_embedded": {
        "bankStatementList": [
            {
                "bankStatementId": 1,
                "information": "Some information here",
                "_links": {
                    "markAsFailed": {
                        "href": "http://localhost:8080/customer/1/bankStatements/1/markAsFailed"
                    },
                    "resend": {
                        "href": "http://localhost:8080/customer/1/bankStatements/1/resend"
                    }
                }
            },
            {
                "bankStatementId": 2,
                "information": "Some information here",
                "_links": {
                    "markAsFailed": {
                        "href": "http://localhost:8080/customer/1/bankStatements/2/markAsFailed"
                    },
                    "resend": {
                        "href": "http://localhost:8080/customer/1/bankStatements/2/resend"
                    }
                }
            },
            {
                "bankStatementId": 3,
                "information": "Some information here",
                "_links": {
                    "markAsFailed": {
                        "href": "http://localhost:8080/customer/1/bankStatements/3/markAsFailed"
                    },
                    "resend": {
                        "href": "http://localhost:8080/customer/1/bankStatements/3/resend"
                    }
                }
            }
        ]
    }
}
```
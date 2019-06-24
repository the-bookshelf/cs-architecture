# Reactive endpoints


This project shows how reactive endpoints can be created using Spring. In order to try the examples follow these steps:


1. Run the class `ReactiveEndpointsApplication`

2. Execute this curl command 

```sh
$ curl  http://localhost:8080/jlo/comments
```

Then you will see how the data will be retrieved as a stream:

```json
data:{"comment":"jlo is Cool","date":"2018-08-17T04:13:20.940+0000"}

data:{"comment":"jlo is Awesome","date":"2018-08-17T04:13:21.944+0000"}

data:{"comment":"jlo is Great","date":"2018-08-17T04:13:22.942+0000"}

data:{"comment":"jlo is Awesome","date":"2018-08-17T04:13:23.943+0000"}

data:{"comment":"jlo is Bad","date":"2018-08-17T04:13:24.942+0000"}

data:{"comment":"jlo is Great","date":"2018-08-17T04:13:25.942+0000"}

data:{"comment":"jlo is Bad","date":"2018-08-17T04:13:26.942+0000"}

data:{"comment":"jlo is Bad","date":"2018-08-17T04:13:27.942+0000"}

data:{"comment":"jlo is Great","date":"2018-08-17T04:13:28.942+0000"}
```
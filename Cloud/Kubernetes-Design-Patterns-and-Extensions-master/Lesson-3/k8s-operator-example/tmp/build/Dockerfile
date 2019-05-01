FROM alpine:3.6

ADD tmp/_output/bin/k8s-operator-example /usr/local/bin/k8s-operator-example

RUN adduser -D k8s-operator-example
USER k8s-operator-example

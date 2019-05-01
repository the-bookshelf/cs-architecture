FROM golang:1.9.5 as builder
ADD . /go/src/github.com/trainingbypackt/k8s-scheduler-example/
WORKDIR /go/src/github.com/trainingbypackt/k8s-scheduler-example
RUN go build -v -o scheduler

FROM ubuntu
COPY --from=builder /go/src/github.com/trainingbypackt/k8s-scheduler-example/scheduler /scheduler
CMD ["./scheduler"]
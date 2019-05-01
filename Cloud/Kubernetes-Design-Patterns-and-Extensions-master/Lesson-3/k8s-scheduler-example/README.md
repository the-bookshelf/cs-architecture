## Kubernetes Scheduler Example [![Docker build](https://img.shields.io/docker/automated/onuryilmaz/k8s-scheduler-example.svg)](https://hub.docker.com/r/onuryilmaz/k8s-scheduler-example/tags/)

This repository contains a Kubernetes scheduler code for random node assignment.

### Build and push
```
$ make docker-build
$ make docker-push
```

### Example usage

#### Create a pod with custom scheduler:
```
$ kubectl apply -f deploy/pod.yaml
$ kubectl get pods
NAME  		READY     STATUS    RESTARTS   AGE
nginx          0/1       Pending   0          5s
```

#### Deploy the scheduler into cluster:
```
$ kubectl apply -f deploy/scheduler.yaml
```

#### Check the pods:
```
$ kubectl get pods
NAME        READY     STATUS    RESTARTS   AGE
nginx       1/1       Running   0          44s
scheduler   1/1       Running   0          17s
```

#### Check the logs of scheduler:
```
$ kubectl logs scheduler
Starting scheduler: packt-scheduler
Assigning default/nginx to minikube 
```

### Cleanup
```
$ kubectl delete -f deploy/pod.yaml
$ kubectl delete -f deploy/scheduler.yaml
```

### Dependency Management
* [glide](https://github.com/Masterminds/glide) is used for vendoring and dependency management.
* If you need to change client library version, update `glide.yaml` file and run `glide up`. 
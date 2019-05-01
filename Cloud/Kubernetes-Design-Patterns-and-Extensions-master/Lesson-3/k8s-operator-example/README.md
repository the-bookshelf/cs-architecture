## Kubernetes Operator Example [![Docker build](https://img.shields.io/docker/automated/onuryilmaz/k8s-operator-example.svg)](https://hub.docker.com/r/onuryilmaz/k8s-operator-example/tags/)

This repository contains the Kubernetes operator code for example custom resource of `WeatherReport`.

### Initialize operator code from scratch
```
$ cd $GOPATH/src/github.com/trainingbypackt/
$operator-sdk new k8s-operator-example --api-version=k8s.packt.com/v1 --kind=WeatherReport
```

### Build and push
```
$ operator-sdk build trainingbypackt/k8s-operator-example
$ docker push trainingbypackt/k8s-operator-example
```

### Deploy operator
```
$ kubectl create -f deploy/crd.yaml
$ kubectl create -f deploy/operator.yaml
```


### Example usage

#### Create custom resource
```
$ kubectl create -f deploy/cr.yaml
```

#### Describe status
```
$ kubectl describe weatherreport amsterdam-daily
Name:         amsterdam-daily
Namespace:    default
…
API Version:  k8s.packt.com/v1
Kind:         WeatherReport
Metadata:
…
Spec:
  City:  Amsterdam
  Days:  1
Status:
  Pod:    weather-report-259735700
  State:  Started
```

#### Get weather report logs

```
$ kubectl logs $(kubectl get weatherreport amsterdam-daily -o jsonpath={.status.pod})
 Weather report: Amsterdam, Netherlands

    \  /       Partly cloudy
  _ /"".-.     21 °C          
    \_(   ).   ↘ 19 km/h      
    /(___(__)  10 km          
               0.0 mm 
```

### Cleanup
```
$ kubectl delete -f deploy/cr.yaml
$ kubectl delete -f deploy/operator.yaml
$ kubectl delete -f deploy/crd.yaml
```

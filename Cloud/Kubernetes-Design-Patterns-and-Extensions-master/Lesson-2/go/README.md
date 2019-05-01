## Example Go Application using Kubernetes Client Library

### Tools
* Docker

### Build
**Go binary for outside cluster usage:**
```
make build
```
It will create a binary for your host operating system using cross-platform build inside Go Docker container.
 
**Docker image for inside the cluster usage:**
```
make docker-build
```

### Run
**Outside cluster:**
```
./client
```
 
**Docker image for inside the cluster usage:**
```
make k8s-run k8s-logs
```

### Dependency Management
* [glide](https://github.com/Masterminds/glide) is used for vendoring and dependency management.
* If you need to change client library version, update `glide.yaml` file and run `glide up`. 
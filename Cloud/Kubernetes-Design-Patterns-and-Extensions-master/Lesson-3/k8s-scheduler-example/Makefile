OSFLAG 				:=
GOOS :=
GOARC :=

ifeq ($(OS),Windows_NT)
	OSFLAG += -D WIN32
	GOOS = windows
	ifeq ($(PROCESSOR_ARCHITECTURE),AMD64)
		OSFLAG += -D AMD64
		GOARCH = amd64
	endif
	ifeq ($(PROCESSOR_ARCHITECTURE),x86)
		OSFLAG += -D IA32
		GOARCH = 386
	endif
else
	UNAME_S := $(shell uname -s)
	ifeq ($(UNAME_S),Linux)
		OSFLAG += -D LINUX
		GOOS = linux
	endif
	ifeq ($(UNAME_S),Darwin)
		OSFLAG += -D OSX
		GOOS = darwin
	endif
		UNAME_P := $(shell uname -p)
	ifeq ($(UNAME_P),x86_64)
		OSFLAG += -D AMD64
		GOARCH = amd64
	endif
		ifneq ($(filter %86,$(UNAME_P)),)
	OSFLAG += -D IA32
	GOARCH = 386
		endif
	ifneq ($(filter arm%,$(UNAME_P)),)
		OSFLAG += -D ARM
		GOARCH = arm
	endif
endif

all:
	@echo $(OSFLAG) - $(GOOS) - $(GOARCH)

build: clean
	docker run -v ${PWD}:/go/src/github.com/trainingbypackt/k8s-scheduler-example -w /go/src/github.com/trainingbypackt/k8s-scheduler-example -e GOOS=$(GOOS) -e GOARCH=$(GOARCH) golang:1.9.5 go build -v -o scheduler

clean:
	rm -rf scheduler

docker-build:
	docker build -t trainingbypackt/k8s-scheduler-example .

docker-push:
	docker push trainingbypackt/k8s-scheduler-example

k8s-run:
	kubectl run scheduler --image=trainingbypackt/k8s-scheduler-example

k8s-logs:
	kubectl logs $(shell kubectl get pods --selector=run=scheduler -o jsonpath="{.items[0].metadata.name}")

k8s-stop:
	kubectl delete deployment scheduler
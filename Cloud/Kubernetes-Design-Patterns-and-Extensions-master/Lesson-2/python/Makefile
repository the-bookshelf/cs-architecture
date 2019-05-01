docker-run:
	docker run -it -v $(HOME):/root --net=host trainingbypackt/k8s-client-example:python bash

docker-build:
	docker build -t trainingbypackt/k8s-client-example:python .

docker-push:
	docker push trainingbypackt/k8s-client-example:python

k8s-run:
	kubectl run python-client -it --image=trainingbypackt/k8s-client-example:python

k8s-stop:
	kubectl delete deployment python-client
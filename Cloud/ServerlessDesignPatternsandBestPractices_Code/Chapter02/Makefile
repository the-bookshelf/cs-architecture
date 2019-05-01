ENVDIR=envs
LIBS_DIR=serverless/lib

shell : db
	docker-compose run --rm serverless bash 
.PHONY: shell


db :
	docker-compose up -d db
.PHONY: db


attach :
	$(call exec_bash,$(ENV))
.PHONY: attach


clean :
	@test -d $(LIBS_DIR) || mkdir -p $(LIBS_DIR)
	rm -rf $(LIBS_DIR)/*
.PHONY: clean


# make libs should be run from inside the container
libs :
	@test -d $(LIBS_DIR) || mkdir -p $(LIBS_DIR)
	pip install -t $(LIBS_DIR) -r requirements.txt
	rm -rf $(LIBS_DIR)/*.dist-info
	find $(LIBS_DIR) -name '*.pyc' | xargs rm
	find $(LIBS_DIR) -name tests | xargs rm -rf
.PHONY: libs

# NOTE:
#
# 	Deployments assume you are already running inside the docker container
#
#	To deploy everything, simply: make deploy
#	To deploy a single function: make deploy function=FunctionName, where
#		FunctionName is the named function in your serverless.yml file
#
deploy :
ifeq ($(strip $(function)),)
	cd serverless && sls deploy -s $(ENV)
else
	cd serverless && sls deploy function -s $(ENV) -f $(function)
endif
.PHONY: deploy


tests :
	py.test --cov=serverless/ --cov-report=html tests/
.PHONY: tests

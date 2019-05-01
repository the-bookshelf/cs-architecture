NAME = "verypossible/serverless:1.25.0-python3"

ENVDIR=envs
LIBS_DIR=serverless/lib


run = docker run --rm -it \
		-v `pwd`:/code \
		--env ENV=$(ENV) \
		--env-file envs/$2 \
		--name=map-reduce-$(ENV) $(NAME) $1


shell : check-env env-dir
	$(call run,bash,$(ENV))
.PHONY: shell

env-dir :
	@test -d $(ENVDIR) || mkdir -p $(ENVDIR)
.PHONY: env-dir

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
deploy : check-env
ifeq ($(strip $(function)),)
	cd serverless && sls deploy -s $(ENV)
else
	cd serverless && sls deploy function -s $(ENV) -f $(function)
endif
.PHONY: deploy


check-env:
ifndef ENV
	$(error ENV is undefined)
endif

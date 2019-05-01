# Serverless js-fanout

This project was bootstrapped using the Very Serverless Cookiecutter template. This is a
opinionated setup in order to facilitate developing, running and bootstrapping Serverless projects
authored in Python.

For more information, see the following:

- [Serverless Docker image, used in this template](https://github.com/verypossible/serverless)
- [A thorough explanation of this layout and reasoning behind it](https://verypossible.com/blog/structuring-serverless-applications-with-python)

# Getting started

After you've bootstrapped your project (ie, you've already run `cookiecutter` using this templates and are reading this `README` with your own project name in the title above), you'll need to do a couple of things:

- Add AWS credentials into the `envs/dev` file
- Bootstrap your serverless project

## AWS credentials

Look inside the `envs/dev` file.  You will need to put in AWS credentials which have permission to create AWS resources (Lambda, IAM roles, etc.)

## Bootstrap your Serverless project

**Important!!!**

This project assumes your Serverless project lives in a directory named `serverless`.

Start up your container, and have Serverless setup a new project. **We will make sure your application code lives in the `serverless` directory bu specifying the path (`-p`) argument below:**

    # Host machine
    $ ENV=dev make shell
    # Now you're in your docker container
    > sls create -t aws-python -p serverless -n your-project-name
    
You may also use Python3 by replacing the `-t` value in the instruction above.

## Deployments

The first time you deploy your code, you will use:

    make deploy

Anytime there are changes to AWS resources, you will need to use this command. You can read [more about deployments in the Serverless docs](https://serverless.com/framework/docs/providers/aws/cli-reference/deploy/)

As you are making changes to your code, you can more quickly deploy individual functions which skip the step of managing AWS resources by using:

    make deploy function=FunctionName
   
...where `FunctionName` would be a single entry under the `functions` section of your `serverless.yml` file.
 
See the Serverless docs about [deploying functions if you'd like more details](https://serverless.com/framework/docs/providers/aws/cli-reference/deploy-function/)

# Addendum

As with most templating projects, this setup is really just a small collection of patterns and wrappers around existing tools.  If you look at the contents of the `Makefile`, you'll see the nitty gritty on how this all works.  Much or all of it can be customized to fit your needs.  From experience, we at [Very](https://verypossible.com) have found this to be a lovely way of 

1. managing multiple environments/deployments from the same code base
2. iterating on serverless projects, with quick and independent deployments
3. keeping sensitive config out of version control

Additionally, while this is primarily setup to author Serverless apps in Python, the Docker images used here should work just fine for Node (although, this is untested).

Feedback and improvements are welcomed!

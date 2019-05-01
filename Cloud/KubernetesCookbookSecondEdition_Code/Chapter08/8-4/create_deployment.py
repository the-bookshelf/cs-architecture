from kubernetes import client, config
import json

config.load_kube_config()

resource_config = json.load(open("./nginx-deployment.json"))
api_instance = client.AppsV1Api()
response = api_instance.create_namespaced_deployment(body=resource_config, namespace="default")
print("add new deployment, status={}".format(response.status))


from kubernetes import client, config
import time

config.load_incluster_config()

v1 = client.CoreV1Api()

while True:
    ret = v1.list_pod_for_all_namespaces(watch=False)
    print('There are {:d} pods in the cluster:'.format(len(ret.items)))
    
    for i in ret.items:
        print('{:s}/{:s}'.format(i.metadata.namespace, i.metadata.name))
    
    time.sleep(10)
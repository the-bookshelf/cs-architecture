package main

import (
	"flag"
	"os"
	"path/filepath"
	"time"

	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/client-go/kubernetes"
	_ "k8s.io/client-go/plugin/pkg/client/auth/gcp"
	"k8s.io/client-go/tools/clientcmd"

	"fmt"
	"k8s.io/api/core/v1"
	"k8s.io/client-go/rest"
	"math/rand"
)

var clientset *kubernetes.Clientset

func main() {
	var kubeconfig *string
	var config *rest.Config
	var err error
	schedulerName := flag.String("scheduler-name", "packt-scheduler", "Name of the scheduler")

	// Check for default kubeconfig location
	if home := os.Getenv("HOME"); home != "" {
		kubeconfig = flag.String("kubeconfig", filepath.Join(home, ".kube", "config"), "(optional) absolute path to the kubeconfig file")
	} else {
		kubeconfig = flag.String("kubeconfig", "", "absolute path to the kubeconfig file")
	}
	flag.Parse()

	// Use the current context in kubeconfig
	if *kubeconfig != "" {
		config, err = clientcmd.BuildConfigFromFlags("", *kubeconfig)
		if err != nil {
			// Use in-cluster configuration
			config, err = rest.InClusterConfig()
			if err != nil {
				panic(err.Error())
			}
		}
	}

	// Create clientset from configuration
	clientset, err = kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	fmt.Printf("Starting scheduler: %s\n", *schedulerName)

	for {
		// Request pods from all namespaces
		pods, err := clientset.CoreV1().Pods(v1.NamespaceAll).List(metav1.ListOptions{})
		if err != nil {
			panic(err.Error())
		}
		// Check for pods
		for _, pod := range pods.Items {
			// If scheduler name is set and node is not assigned
			if pod.Spec.SchedulerName == *schedulerName && pod.Spec.NodeName == "" {

				// Schedule the pod to a random node
				err := schedule(pod.Name, randomNode(), pod.Namespace)

				if err != nil {
					panic(err.Error())
				}
			}
		}
		time.Sleep(10 * time.Second)
	}
}

func randomNode() string {

	nodes, err := clientset.CoreV1().Nodes().List(metav1.ListOptions{})
	if err != nil {
		panic(err.Error())
	}

	if len(nodes.Items) < 1 {
		panic("no nodes found")
	}

	return nodes.Items[rand.Intn(len(nodes.Items))].Name
}

func schedule(pod, node, namespace string) error {

	fmt.Printf("Assigning %s/%s to %s\n", namespace, pod, node)

	// Create a binding with pod and node
	binding := v1.Binding{
		ObjectMeta: metav1.ObjectMeta{
			Name: pod,
		},
		Target: v1.ObjectReference{
			Kind:       "Node",
			APIVersion: "v1",
			Name:       node,
		}}

	return clientset.CoreV1().Pods(namespace).Bind(&binding)
}

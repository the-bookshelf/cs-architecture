package main

import (
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"time"

	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/client-go/kubernetes"
	_ "k8s.io/client-go/plugin/pkg/client/auth/gcp"
	"k8s.io/client-go/tools/clientcmd"

	"k8s.io/client-go/rest"
	"k8s.io/api/core/v1"
)

func main() {
	var kubeconfig *string
	var config *rest.Config
	var err error

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
	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	// Check for pods
	for {
		// Request pods from all namespaces
		pods, err := clientset.CoreV1().Pods(v1.NamespaceAll).List(metav1.ListOptions{})
		if err != nil {
			panic(err.Error())
		}

		fmt.Printf("There are %d pods in the cluster:\n", len(pods.Items))

		for _, pod := range pods.Items {
			fmt.Println(pod.Namespace + "/" + pod.Name)
		}

		time.Sleep(10 * time.Second)
	}
}

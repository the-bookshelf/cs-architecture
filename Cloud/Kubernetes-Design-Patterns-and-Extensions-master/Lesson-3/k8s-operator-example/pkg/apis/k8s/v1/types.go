package v1

import (
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
)

// +k8s:deepcopy-gen:interfaces=k8s.io/apimachinery/pkg/runtime.Object

type WeatherReportList struct {
	metav1.TypeMeta `json:",inline"`
	metav1.ListMeta `json:"metadata"`
	Items           []WeatherReport `json:"items"`
}

// +k8s:deepcopy-gen:interfaces=k8s.io/apimachinery/pkg/runtime.Object

type WeatherReport struct {
	metav1.TypeMeta   `json:",inline"`
	metav1.ObjectMeta `json:"metadata"`
	Spec              WeatherReportSpec   `json:"spec"`
	Status            WeatherReportStatus `json:"status,omitempty"`
}

type WeatherReportSpec struct {
	City string `json:"city"`
	Days int    `json:"days"`
}

type WeatherReportStatus struct {
	State string `json:"state"`
	Pod   string `json:"pod"`
}

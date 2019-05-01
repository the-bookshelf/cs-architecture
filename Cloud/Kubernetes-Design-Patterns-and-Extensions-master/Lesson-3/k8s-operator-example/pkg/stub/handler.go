package stub

import (
	apiv1 "github.com/trainingbypackt/k8s-operator-example/pkg/apis/k8s/v1"

	"github.com/operator-framework/operator-sdk/pkg/sdk/action"
	"github.com/operator-framework/operator-sdk/pkg/sdk/handler"
	"github.com/operator-framework/operator-sdk/pkg/sdk/types"
	"github.com/sirupsen/logrus"
	"k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/api/errors"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime/schema"
	"strconv"
	"time"
	"fmt"
)

func NewHandler() handler.Handler {
	return &Handler{}
}

type Handler struct {
	// Fill me
}

func (h *Handler) Handle(ctx types.Context, event types.Event) error {
	switch o := event.Object.(type) {
	case *apiv1.WeatherReport:

		if o.Status.State == "" {
			weatherPod := weatherReportPod(o)
			err := action.Create(weatherPod)
			if err != nil && !errors.IsAlreadyExists(err) {
				logrus.Errorf("Failed to create weather report pod : %v", err)
				o.Status.State = "Failed"
				action.Update(o)
				return err
			}

			o.Status.State = "Started"
			o.Status.Pod = weatherPod.Name
			action.Update(o)
		}
	}
	return nil
}

func weatherReportPod(cr *apiv1.WeatherReport) *v1.Pod {

	url := fmt.Sprintf("http://wttr.in/%s?%d", cr.Spec.City, cr.Spec.Days)

	labels := map[string]string{
		"app":  "weather-report",
		"city": cr.Spec.City,
		"days": strconv.Itoa(cr.Spec.Days),
	}
	return &v1.Pod{
		TypeMeta: metav1.TypeMeta{
			Kind:       "Pod",
			APIVersion: "v1",
		},
		ObjectMeta: metav1.ObjectMeta{
			Name:      "weather-report-" + strconv.Itoa(time.Now().Nanosecond()),
			Namespace: "default",
			OwnerReferences: []metav1.OwnerReference{
				*metav1.NewControllerRef(cr, schema.GroupVersionKind{
					Group:   v1.SchemeGroupVersion.Group,
					Version: v1.SchemeGroupVersion.Version,
					Kind:    "WeatherReport",
				}),
			},
			Labels: labels,
		},
		Spec: v1.PodSpec{
			Containers: []v1.Container{
				{
					Name:    "weather",
					Image:   "tutum/curl",
					Command: []string{"sh", "-c", "curl -s " + url + " && sleep 3600"},
				},
			},
		},
	}
}

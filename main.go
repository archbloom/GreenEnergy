package main

import (
	"github.com/ant0ine/go-json-rest/rest"
	"log"
	"net/http"
	"time"
	"fmt"
	"strconv"
)
type Device struct {
	Device_Id string
	Name string
	Wattage int
	Rating int
	State int
	Total_Uptime time.Duration
	Current_Timestamp time.Time
}
var HoneywellDevicelist = []Device{
	Device{"H001", "Thermostat", 3500, 4, 0, 0, time.Time{}},
	Device{"H002", "HallLight", 42, 5, 0, 0, time.Time{}},
	Device{"H003", "Microwave", 7500, 4, 0, 0, time.Time{}},
	Device{"H004", "Refrigerator", 6875, 4, 0, 0, time.Time{}},
}
var UserDevices = []Device{}
var IndoorProfile = map[string]int{}
var OutdoorProfile = map[string]int{}
type Profile struct {
	Name string
	Status int
}
var IndoorProfileData = []Profile{}
var OutdoorProfileData = []Profile{}
var Monthly_Cycle = map[string][]int{
	"Thermostat" : []int{4,5,3,8,3,5,2,6,8,2,5,7,3,7,3,4,5,3,8,3,5,2,6,8,2,5,7,3,7,3,4},
	"Microwave" : []int{4,5,3,8,3,5,2,6,8,2,5,7,3,7,3,4,5,3,8,3,5,2,6,8,2,5,7,3,7,3,4},
	"Refrigerator" : []int{5,2,3,8,3,5,2,6,2,2,5,5,3,7,3,4,5,6,8,3,5,2,6,8,8,3,7,6,7,3,4},
	"HallLight" : []int{4,9,3,8,3,5,2,3,8,4,5,7,5,6,3,4,5,3,8,3,2,8,6,8,2,5,7,6,7,3,4},
}
func main() {
	api := rest.NewApi()
	api.Use(rest.DefaultDevStack...)
	router, err := rest.MakeRouter(
		rest.Get("/device/", GetAllUserDevices),
		rest.Get("/device/toggle",ToggleState),
		rest.Post("/device",AddDevice),
		rest.Get("/device",GetDevice),
		rest.Get("/profile",GetProfile),
		rest.Post("/profile", UpdateProfile),
		rest.Get("/dashboard",GetDashboard),
		rest.Get("/device/details",GetDeviceDetails),
	)
	if err != nil {
		log.Fatal(err)
	}
	api.SetApp(router)
	log.Fatal(http.ListenAndServe(":8888", api.MakeHandler()))
}

func GetAllUserDevices(w rest.ResponseWriter, r *rest.Request) {
	fmt.Println("Get all the devices")
	w.WriteJson(&UserDevices)
}
func AddDevice(w rest.ResponseWriter, r *rest.Request){
	fmt.Println("Add a new Device")
	d := Device{}
	err := r.DecodeJsonPayload(&d)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if d.Name == "" {
		rest.Error(w, "Device Name required", 400)
		return
	}
	if _,ok := IndoorProfile[d.Name]; !ok {
			IndoorProfile[d.Name]	= d.State
			OutdoorProfile[d.Name]	= d.State
	}
	UserDevices = append(UserDevices,d)

	w.WriteJson(&d)
}
func ToggleState(w rest.ResponseWriter, r *rest.Request){
	
	status := r.FormValue("Status")
	Device_Name := r.FormValue("Device_Name")
	// fmt.Println(Device_Name,status)
	statusint,_ := strconv.Atoi(status) 
	// fmt.Printf("%T\t%T",Device_Name,s)
	for i,val := range UserDevices {
		if val.Name == Device_Name{
			if UserDevices[i].State == 0 && statusint == 1 {
				UserDevices[i].Current_Timestamp = time.Now()
			}else if UserDevices[i].State == 1 && statusint == 0 {
				ctime := time.Now()
				UserDevices[i].Total_Uptime += ctime.Sub(UserDevices[i].Current_Timestamp)
				UserDevices[i].Current_Timestamp = time.Time{}
				fmt.Println("Got Total uptime: ",UserDevices[i].Total_Uptime)
			}
			UserDevices[i].State = statusint
			fmt.Println("State of ",Device_Name," changed to ",statusint)
		}
	}
}
func GetDevice(w rest.ResponseWriter, r *rest.Request){
	did := r.FormValue("Device_Id")
	for _,val := range HoneywellDevicelist {
		if val.Device_Id == did {
			// fmt.Println("Found ",val.Name)
			w.WriteJson(&val)
		}
	}
}

func GetProfile(w rest.ResponseWriter, r *rest.Request){
	name := r.FormValue("Name")
	if name == "Indoor" {
			w.WriteJson(&IndoorProfileData)
	} else {
			w.WriteJson(&OutdoorProfileData)
	}
	
}
func UpdateProfile(w rest.ResponseWriter, r *rest.Request){
	name := r.FormValue("Name")
	fmt.Println(name," Profile Updated!")
	if name == "Indoor" {
		fmt.Println("in indoor update")
		r.DecodeJsonPayload(&IndoorProfileData)
		// fmt.Println(IndoorProfileData)
	}else {
		fmt.Println("in outdoor update")
		r.DecodeJsonPayload(&OutdoorProfileData)
		// fmt.Println(OutdoorProfileData)
	}	
}

func GetDashboard(w rest.ResponseWriter, r *rest.Request){
	w.WriteJson(&UserDevices)
}
func GetDeviceDetails(w rest.ResponseWriter, r *rest.Request){
	
	name := r.FormValue("Name")
	// fmt.Println("Got",name)
	fmt.Println("Get the details of the ",name)
	w.WriteJson(Monthly_Cycle[name])
}
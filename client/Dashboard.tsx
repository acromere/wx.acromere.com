import './css/dashboard.css'
import Clock from "./Clock.tsx";
import {useEffect, useState} from "react";
import WeatherStation from "./WeatherStation.tsx";
import FlightConditions from "./FlightConditions.tsx";
import WeatherService from "./api/WeatherService.ts"

export default function Dashboard() {

  const [station, setstation] = useState({})

  useEffect( () => {
    // load station
    new WeatherService().fetchWeather( (response) =>{
      //
    })
  }, [])

  return (
    <div className="dashboard">
      <div className="content-left col-4">
        <div className="buffer"/>
        <FlightConditions station={station}/>
      </div>
      <div className="content-center col-4">
        <Clock/>
      </div>
      <div className="content-right col-4">
        <div className="buffer"/>
        <WeatherStation station={station}/>
      </div>
    </div>
  )
}
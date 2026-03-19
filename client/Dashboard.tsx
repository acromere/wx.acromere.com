import './css/dashboard.css'
import Clock from "./Clock.tsx";
import {useEffect, useState} from "react";
import WeatherStation from "./WeatherStation.tsx";
import FlightConditions from "./FlightConditions.tsx";
import WeatherService from "./api/WeatherService.ts"

export default function Dashboard() {

  const [station, setStation] = useState({})

  const updateStation = () => {
    new WeatherService().fetchWeather('HERUT', (station) => {
      setStation(station);
    })
  }

  useEffect(() => {
    // Load the initial weather station data
    updateStation();

    // Start reload timer
    let refreshTimer: NodeJS.Timeout = setInterval(updateStation, 60000);

    return () => {
      clearInterval(refreshTimer);
    }
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
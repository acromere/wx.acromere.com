import './css/dashboard.css'
import Clock from "./Clock.tsx";
import {useState} from "react";
import WeatherStation from "./WeatherStation.tsx";
import FlightConditions from "./FlightConditions.tsx";

export default function Dashboard() {

  const [weather, setWeather] = useState({})

  return (
    <div className="dashboard">
      <div className="content-left col-4">
        <div className="buffer"/>
        <FlightConditions weather={weather}/>
      </div>
      <div className="content-center col-4">
        <Clock/>
      </div>
      <div className="content-right col-4">
        <div className="buffer"/>
        <WeatherStation weather={weather}/>
      </div>
    </div>
  )
}
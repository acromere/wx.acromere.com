import './css/dashboard.css'
import Clock from "./Clock.tsx";
import {useEffect, useState} from "react";
import WeatherStation from "./WeatherStation.tsx";
import FlightConditions from "./FlightConditions.tsx";
import WeatherService from "./api/WeatherService.ts"

export default function Dashboard() {

  const DEFAULT_TEMPEST_STATION_ID = '215817'

  const DEFAULT_NWS_STATION_ID = 'HERUT'

  const [station, setStation] = useState({})

  const updateStation: () => void = (): void => {
    const queryString: string = window.location.search;
    const params = new URLSearchParams(queryString);
    let stationId: string = params.get('id')
    if (stationId === null) stationId = DEFAULT_NWS_STATION_ID

    console.log("Station ID=" + stationId);

    new WeatherService().fetchNwsWeather(stationId, (station: any): void => {
      setStation(station);
    }).then()
  }

  useEffect((): () => void => {
    // Load the initial weather station data
    updateStation();

    // Start reload timer
    let refreshTimer: NodeJS.Timeout = setInterval(updateStation, 60000);

    return (): void => {
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
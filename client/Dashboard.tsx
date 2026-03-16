import './css/dashboard.css'
import Clock from "./Clock.tsx";

export default function Dashboard() {
  return (
    <div className="dashboard">
      <div className="content-left col-4">
        <div className="buffer"/>
        {/*<WeatherStation weather={this.state.weather}/>*/}
      </div>
      <div className="content-center col-4">
        <Clock/>
      </div>
      <div className="content-right col-4">
        <div className="buffer"/>
        {/*<FlightConditions weather={this.state.weather}/>*/}
      </div>
    </div>
  )
}
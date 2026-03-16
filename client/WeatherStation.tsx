import {toDatestamp} from './Util'
import Temperature from "./Temperature.tsx";

export default function WeatherStation(props:any) {

  return (
    <div className='weather'>
      <div className='title'>{props.weather.name} Station</div>
      <div className='subtitle'>{toDatestamp(props.weather.timestamp)}</div>
      <Temperature weather={props.weather}/>
      {/*<Stats weather={props.weather}/>*/}
    </div>
  )
}
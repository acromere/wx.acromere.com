import {toDatestamp} from './Util'
import Temperature from "./Temperature.tsx";
import Stats from "./Stats.tsx";

export default function WeatherStation(props:any) {

  return (
    <div className='station'>
      <div className='title'>{props.station.name}</div>
      <div className='subtitle'>{toDatestamp(props.station.timestamp)}</div>
      <Temperature station={props.station}/>
      <Stats station={props.station}/>
    </div>
  )
}
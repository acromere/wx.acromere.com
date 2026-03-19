import {toDatestamp} from './Util'
import Temperature from "./Temperature.tsx";

export default function WeatherStation(props:any) {

  return (
    <div className='station'>
      <div className='title'>{props.station.name} Station</div>
      <div className='subtitle'>{toDatestamp(props.station.timestamp)}</div>
      <Temperature weather={props.station}/>
      {/*<Stats weather={props.station}/>*/}
    </div>
  )
}
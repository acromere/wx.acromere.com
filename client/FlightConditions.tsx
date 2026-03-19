import {ReactNode} from "react";
import {toDatestamp} from "./Util.ts";

export default function FlightConditions(props: any) {

  const summary: string = (props.station.flightCondition && props.station.flightCondition.summary) || '';
  const reasons: string[] = (props.station.flightCondition && props.station.flightCondition.reasons) || [];

  return (
    <div className='flight-conditions'>
      <div className='title'>Flight Conditions</div>
      <div className='subtitle'>{toDatestamp(props.station.timestamp)}</div>
      <div className='summary'>{summary}</div>
      <div className='reason'>
        {reasons.map((reason: string, index: number): ReactNode => (
          <span key={index}>{index > 0 ? ' ' : ''}{reason}</span>
        ))}
      </div>
    </div>
  )
}
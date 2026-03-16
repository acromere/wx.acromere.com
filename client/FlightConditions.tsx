import {ReactNode} from "react";

export default function FlightConditions(props: any) {

  const summary: string = (props.weather.flightCondition && props.weather.flightCondition.summary) || '';
  const reasons: string[] = (props.weather.flightCondition && props.weather.flightCondition.reasons) || [];

  return (
    <div className='flight-conditions'>
      <div className='title'>Flight Conditions</div>
      <div className='summary'>{summary}</div>
      <div className='reason'>
        {reasons.map((reason: string, index: number): ReactNode => (
          <span key={index}>{index > 0 ? ' ' : ''}{reason}</span>
        ))}
      </div>
    </div>
  )
}
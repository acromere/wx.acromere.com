export default function Temperature(props: any) {
  const temperature = parseFloat(props.station.temperature).toFixed(1);
  const unit = props.station.temperatureUnit;

  return (
    <div className="temperature">{temperature}<span className="unit">{unit}</span></div>
  )
}

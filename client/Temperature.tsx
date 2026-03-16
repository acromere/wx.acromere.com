export default function Temperature(props: any) {
  const temperature = parseFloat(props.weather.temperature).toFixed(1);
  const unit = props.weather.temperatureUnit;

  return (
    <div className="temperature">{temperature}<span className="unit">{unit}</span></div>
  )
}

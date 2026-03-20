export default function Stats(props:any) {

  const windSpeed = parseFloat(props.station.windSpeed)
  const gustSpeed = parseFloat(props.station.windGust)

  const windSpeedText:string = windSpeed.toFixed(1);
  const gustSpeedText:string = gustSpeed < 0.0 ? '---' : gustSpeed.toFixed(1);
  const windCardinal = windSpeed < 0.1 ? "---" : props.station.windCardinal;
  const windDirection = windSpeed < 0.1 ? "---" : parseFloat(props.station.windDirection).toFixed(0);
  const humidity = parseFloat(props.station.humidity).toFixed(0);
  const rainTotalDaily = parseFloat(props.station.rainTotalDaily).toFixed(2);
  const feelsLike = parseFloat(props.station.feelsLike).toFixed(1);
  const tempUnit = props.station.temperatureUnit;

  return (
    <div className="stats">
      <table>
        <tbody>
        <tr className="stats">
          <td className="label">feel </td>
          <td className="value">{feelsLike}</td>
          <td className="unit">{tempUnit}</td>
        </tr>
        <tr>
          <td className="label">humid&nbsp;</td>
          <td className="value">&nbsp;{humidity}</td>
          <td className="unit">&nbsp;{props.station.humidityUnit}</td>
        </tr>
        <tr>
          <td className="label">wind&nbsp;</td>
          <td className="value">&nbsp;{windSpeedText}</td>
          <td className="unit">&nbsp;{props.station.windSpeedUnit}</td>
        </tr>
        <tr>
          <td className="label">gust&nbsp;</td>
          <td className="value">&nbsp;{gustSpeedText}</td>
          <td className="unit">&nbsp;{props.station.windSpeedUnit}</td>
        </tr>
        <tr>
          <td className="label">from&nbsp;</td>
          <td className="value">&nbsp;{windCardinal}</td>
          <td className="unit">&nbsp;{windDirection}{props.station.windDirectionUnit}</td>
        </tr>
        <tr>
          <td className="label">rain&nbsp;</td>
          <td className="value">&nbsp;{rainTotalDaily}</td>
          <td className="unit">&nbsp;{props.station.rainUnit}</td>
        </tr>
        </tbody>
      </table>
    </div>
  );
}
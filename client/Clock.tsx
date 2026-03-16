import {useEffect, useState} from "react";
import {getDowName, pad} from "./Util.ts";

export default function Clock() {

  const [time, setTime] = useState('');
  const [date, setDate] = useState('');
  const [ap, setAp] = useState('');
  const [dow, setDow] = useState('');

  const updateTime = () => {
    let timestamp = new Date();
    let yy = timestamp.getFullYear();
    let mo = timestamp.getMonth() + 1;
    let dd = timestamp.getDate();
    let hh = timestamp.getHours();
    let mm = timestamp.getMinutes();
    let ss = timestamp.getSeconds();
    let ap = hh < 12 ? "am" : "pm";
    let day = timestamp.getDay();
    let dw = getDowName(day);

    hh = hh % 12;
    hh = hh === 0 ? 12 : hh;

    setTime(pad(hh, 2) + ':' + pad(mm, 2) + ':' + pad(ss, 2));
    setDate(pad(yy, 4) + '-' + pad(mo, 2) + '-' + pad(dd, 2));
    setAp(ap);
    setDow(dw);
  };

  useEffect(() => {
    setTimeout(updateTime, 0);
    let refreshTimer: NodeJS.Timeout = setInterval(updateTime, 1000);

    return () => {
      clearInterval(refreshTimer);
    }
  }, [])

  return (
    <div className="clock">
      <div className="time">{time}<span className="ampm">{ap}</span></div>
      <div className="date">{date}</div>
      <div className="dow">{dow}</div>
    </div>
  )

}
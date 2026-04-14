import {useEffect, useState} from "react";
import {getDowName, pad} from "./Util.ts";

export default function Clock() {

  const [time, setTime] = useState('')
  const [date, setDate] = useState('')
  const [ap, setAp] = useState('')
  const [dow, setDow] = useState('')
  const [zulu, setZulu] = useState('')

  const updateTime = () => {
    const timestamp = new Date();
    const yy = timestamp.getFullYear();
    const mo = timestamp.getMonth() + 1;
    const dd = timestamp.getDate();
    const hr = timestamp.getHours();
    const mm = timestamp.getMinutes();
    const ap = hr < 12 ? "am" : "pm";
    const day = timestamp.getDay();
    const dw = getDowName(day);

    const utcHh = timestamp.getUTCHours();
    const utcMm: number = timestamp.getUTCMinutes();

    let hh = hr % 12;
    hh = hh === 0 ? 12 : hh;

    setTime(pad(hh, 2) + ':' + pad(mm, 2));
    setDate(pad(yy, 4) + '-' + pad(mo, 2) + '-' + pad(dd, 2));
    setAp(ap);
    setDow(dw);
    setZulu(utcHh + utcMm + "Z");
  };

  useEffect(() => {
    setTimeout(updateTime, 0);
    const refreshTimer: NodeJS.Timeout = setInterval(updateTime, 1000);

    return () => {
      clearInterval(refreshTimer);
    }
  }, [])

  return (
    <div className="clock">
      <div className="time">{time}<span className="ampm">{ap}</span></div>
      <div className="date">{date}</div>
      <div className="dow">{dow}</div>
      <div className="zulu">{zulu}</div>
    </div>
  )

}
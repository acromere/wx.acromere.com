import {useEffect, useState} from "react";
import {getDowName, pad} from "./Util.ts";

export default function Clock() {

  const [ss, setSeconcs] = useState('');
  const [mm, setMinutes] = useState('');
  const [hh, setHours] = useState('');
  const [dd, setDay] = useState('');
  const [mo, setMonth] = useState('');
  const [yy, setYear] = useState('');
  const [ap, setAmPm] = useState('');
  const [dw, setDayOfWeek] = useState('');

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

    setYear(yy.toString());
    setMonth(mo.toString());
    setDay(dd.toString());
    setHours(hh.toString());
    setMinutes(mm.toString());
    setSeconcs(ss.toString());
    setAmPm(ap);
    setDayOfWeek(dw);
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
      <div className="time">{pad(hh, 2)}:{pad(mm, 2)}:{pad(ss, 2)}<span className="ampm">{ap}</span></div>
      <div className="date">{pad(yy, 4)}-{pad(mo, 2)}-{pad(dd, 2)}</div>
      <div className="dow">{dw}</div>
    </div>
  )
}
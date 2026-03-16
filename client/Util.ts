export function toDatestamp(time) {
  let date = new Date(time);

  let hh:number = date.getHours();
  hh = hh % 12;
  hh = hh === 0 ? 12 : hh;

  let year = pad(date.getFullYear(), 4);
  let month = pad(date.getMonth() + 1, 2);
  let day = pad(date.getDate(), 2);
  let hour = pad(hh, 2);
  let minute = pad(date.getMinutes(), 2);
  let second = pad(date.getSeconds(), 2);
  let ampm = amOrPm(date.getHours());

  return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second + ' ' + ampm;
}

export function getDowName(day:number) {
  const weekday = new Array(7);
  weekday[0] = "Sunday";
  weekday[1] = "Monday";
  weekday[2] = "Tuesday";
  weekday[3] = "Wednesday";
  weekday[4] = "Thursday";
  weekday[5] = "Friday";
  weekday[6] = "Saturday";

  return weekday[day];
}

// Hour must be between 0 and 23
export function amOrPm(h:number) {
  return h < 12 ? 'am' : 'pm';
}

export function pad(n: any, z: number, p: number = 0) {
  n = n.toString();
  return n.length >= z ? n : new Array(z - n.length + 1).join(p.toString()) + n;
}

import {ApiService} from "./ApiService";

export default class WeatherService extends ApiService {

	URI = 'https://wx.acromere.com/api/station';

	fetchNwsWeather(stationId: string, success) {
		return fetch(this.URI + '/nws?id=' + stationId, {
			headers: {
				Accept: 'application/json',
			},
		})
			.then(this.checkStatus)
			.then(this.parseJSON)
			.then(success);
	}

  fetchTempestWeather(stationId: string, success) {
    return fetch(this.URI + '/tempest?id=' + stationId, {
      headers: {
        Accept: 'application/json',
      },
    })
      .then(this.checkStatus)
      .then(this.parseJSON)
      .then(success);
  }

}

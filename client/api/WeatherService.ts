import {ApiService} from "./ApiService";

export default class WeatherService extends ApiService {

	URI = 'https://wx.acromere.com/api/station?id=';

	fetchWeather(stationId: string, success) {
		return fetch(this.URI + stationId, {
			headers: {
				Accept: 'application/json',
			},
		})
			.then(this.checkStatus)
			.then(this.parseJSON)
			.then(success);
	}

}

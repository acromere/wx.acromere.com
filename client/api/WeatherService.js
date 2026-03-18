import {ApiService} from "./ApiService";

export default class WeatherService extends ApiService {

	URI = 'https://wx.acromere.com/api/station?id=HERUT';

	fetchWeather(success) {
		return fetch(this.URI, {
			headers: {
				Accept: 'application/json',
			},
		})
			.then(this.checkStatus)
			.then(this.parseJSON)
			.then(success);
	}

}

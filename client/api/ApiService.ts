export class ApiService {

  // constructor() {
  //   // React bindings
  //   this.checkStatus = this.checkStatus.bind(this);
  //   this.parseJSON = this.parseJSON.bind(this);
  // }

  checkStatus(response) {
    if (response.status >= 200 && response.status < 300) {
      return response;
    } else {
      const error = new Error(`HTTP Error ${response.statusText}`);
      //error.status = response.statusText;
      //error.response = response;
      console.log(error);
      throw error;
    }
  }

  parseJSON(response) {
    return response.json();
  }

}
import axios from 'axios';

const API_URL = '/api/v1/auth/';


class AuthService {
  login(user) {
    console.log(user)
    return axios
      .post(API_URL + 'login', user)
      .then(response => {
        console.log(response.data)
        if (response.data.jwt) {
          
         /*
         //unparse payload
          var base64Payload = response.data.jwt.split(".")[1];
          var payload = decodeURIComponent(
            atob(base64Payload)
              .split("")
              .map(function (c) {
                return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
              })
              .join("")
          );
          console.log(JSON.parse(payload));*/
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }
}

export default new AuthService();
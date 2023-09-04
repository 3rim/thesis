import axios from 'axios';

const API_URL = '/api/v1/auth/';


class AuthService {
  login(user) {
    return axios
      .post(API_URL + 'login', user)
      .then(response => {
        if (response.data.jwt) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }
        console.log(response)
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }
}

export default new AuthService();
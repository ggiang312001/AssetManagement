import { API_URL_CHANGE_PASSWORD, API_URL_LOGIN } from '../constants/configUrl';
import axiosClient from './httpCommon';

export const login = (credentials) => {
	return axiosClient.post(API_URL_LOGIN, credentials);
};
export const logout = () => {
	localStorage.removeItem('token');
	localStorage.removeItem('username');
	localStorage.removeItem('role');
	localStorage.removeItem('isFirstLogin');
};

export const changePassword = ({ oldPassword, newPassword }) => {
	return axiosClient.patch(API_URL_CHANGE_PASSWORD, {
		oldPassword,
		newPassword,
	});
};

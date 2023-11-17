import { API_URL_ADMIN } from '../constants/configUrl';
import axiosClient from './httpCommon';

// export const getAllAssetsByLocation = () => {
// 	return axiosClient.get(`${API_URL_ASSETS}/get-all-by-location/`)
// }

export const getListCategories = () => {
	return axiosClient.get(`${API_URL_ADMIN}/categories/`)
}

export const createUser = (data) => {
	return axiosClient.post(`${API_URL_ADMIN}/users/`, data)
}
// export const getAllUsersByLocation = () => {
// 	return axiosClient.get(`${USER_LIST}/get-all-by-location/`)
// }

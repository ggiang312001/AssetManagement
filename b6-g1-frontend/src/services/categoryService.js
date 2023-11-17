import { API_URL_CATEGORIES } from '../constants/configUrl';
import axiosClient from './httpCommon';

export const getCategories = () => {
	return axiosClient.get(API_URL_CATEGORIES);
};

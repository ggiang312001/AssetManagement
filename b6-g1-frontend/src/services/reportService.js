import { API_URL_REPORT } from '../constants/configUrl';
import axiosClient from './httpCommon';

export const getReport = ({ pageNo, pageSize, sortBy, sortDir }) => {
	return axiosClient.get(
		`${API_URL_REPORT}?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=${sortBy}&sortDir=${sortDir}`,
	);
};

export const exportReport = () => {
	return axiosClient.get(`${API_URL_REPORT}/export`, {
		responseType: 'blob',
	});
};

import { API_URL_ASSETS } from '../constants/configUrl';
import axiosClient from './httpCommon';

export const getAssets = ({
	searchTerm,
	cateFill,
	stateFill,
	pageSize,
	pageNo,
	sortBy,
	sortDir,
}) => {
	return axiosClient.get(
		`${API_URL_ASSETS}?searchTerm=${searchTerm}` +
			`&cateFill=${cateFill}&stateFill=${stateFill}` +
			`&pageSize=${pageSize}&pageNo=${pageNo}` +
			`&sortBy=${sortBy}&sortDir=${sortDir}`,
	);
};

export const getAssetDetail = (assetId) => {
	return axiosClient.get(`${API_URL_ASSETS}/${assetId}`);
};

export const createAsset = (data) => {
	return axiosClient.post(`${API_URL_ASSETS}/`, data)
}
export const editAsset = (assetId,data) => {
	return axiosClient.put(`${API_URL_ASSETS}/${assetId}`, data)
}

export const deleteAsset =(assetId) =>{
	return axiosClient.patch(`${API_URL_ASSETS}/${assetId}`)
}

export const checkDeleteAsset =(assetId) =>{
	return axiosClient.get(`${API_URL_ASSETS}/check-history/${assetId}`)
}

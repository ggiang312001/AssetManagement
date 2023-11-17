import { API_URL_RETURN_REQUEST, API_URL_RETURN_REQUEST_STAFF } from '../constants/configUrl';
import axiosClient from './httpCommon';

export const getReturnRequests = ({
	searchTerm,
	pageSize,
	pageNo,
	sortBy,
	sortDir,
	returnedDate,
	state,
}) => {
	return axiosClient.get(
		`${API_URL_RETURN_REQUEST}` +
			`?searchTerm=${searchTerm}&returnedDate=${returnedDate}&state=${state}` +
			`&sortBy=${sortBy}&sortDir=${sortDir}` +
			`&pageNo=${pageNo}&pageSize=${pageSize}`,
	);
	
};

export const postReturnReq = (assignment_id) => {
	return axiosClient.post(`${API_URL_RETURN_REQUEST_STAFF}/assignmentId/${assignment_id}`);
};

export const postReturnReqByAdmin = (assignment_id) => {
	return axiosClient.post(`${API_URL_RETURN_REQUEST}/assignmentId/${assignment_id}`);
};
export const completeReturnReq = (assignmentId) => {
	return axiosClient.put(`${API_URL_RETURN_REQUEST}/${assignmentId}`)
}
export const deleteReturnReq = (assignmentId) => {
	return axiosClient.patch(`${API_URL_RETURN_REQUEST}/${assignmentId}`)
}

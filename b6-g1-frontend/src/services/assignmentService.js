import { API_URL_ASSIGNMENTS, API_URL_ASSIGNMENTS_STAFF, API_URL_RETURN_REQUEST } from '../constants/configUrl';
import { API_URL_ADMIN } from '../constants/configUrl';
import axiosClient from './httpCommon';

export const getAssignments = ({
	searchTerm,
	dateFill,
	stateFill,
	pageSize,
	pageNo,
    sortBy,
	sortDir,
}) => {
	return axiosClient.get(
		`${API_URL_ADMIN}/assignments?searchTerm=${searchTerm}` +
			`&dateFill=${dateFill}&stateFill=${stateFill}` +
			`&pageSize=${pageSize}&pageNo=${pageNo}`+
			`&sortBy=${sortBy}&sortDir=${sortDir}`,
	);
};


export const getAssignmentDetail = (assignment_id) => {
	return axiosClient.get(`${API_URL_ASSIGNMENTS}/${assignment_id}`);
};export const getStaffAssignmentDetail = (assignment_id) => {
	return axiosClient.get(`${API_URL_ASSIGNMENTS_STAFF}/${assignment_id}`);
};

export const getAssignmentOfStaff = ({

	pageSize,
	pageNo,
}) => {
	return axiosClient.get(
		`${API_URL_ASSIGNMENTS_STAFF}?pageSize=${pageSize}` +
			`&pageNo=${pageNo}`,
	
	);
};

export const acceptAssignment = (assignment_id) => {
	return axiosClient.put(`${API_URL_ASSIGNMENTS_STAFF}/${assignment_id}/accept`);
};
export const declineAssignment = (assignment_id) => {
	return axiosClient.put(`${API_URL_ASSIGNMENTS_STAFF}/${assignment_id}/decline`);
};

export const createAssignment = (data) => {
	return axiosClient.post(`${API_URL_ADMIN}/assignments/`, data)
}

export const editAssignment = (data) => {
	return axiosClient.put(`${API_URL_ADMIN}/assignments/${data.assignmentId}`, data)
}
export const deleteAssignment = (assignmentId) => {
	return axiosClient.delete(`${API_URL_ADMIN}/assignments/${assignmentId}`)
}
export const postReturnReq = (assignment_id) => {
	return axiosClient.post(`${API_URL_RETURN_REQUEST}/assignmentId/${assignment_id}`);
};
export const getReturnReq = (id) => {
	return axiosClient.get(`${API_URL_ASSIGNMENTS}/request/${id}`);
};

import { USER_LIST, API_URL_ADMIN } from "../constants/configUrl";
import axiosClient from "./httpCommon";

export const getUserList = ({ searchTerm, pageSize, type, pageNo, sort }) => {
  if (type === null) {
    return axiosClient.get(
      USER_LIST +
        `?pageSize=${pageSize}&pageNo=${pageNo}&searchTerm=${searchTerm}&${sort}`
    );
  } else {
    return axiosClient.get(
      USER_LIST +
        `?pageSize=${pageSize}&pageNo=${pageNo}&role=${type}&searchTerm=${searchTerm}&${sort}`
    );
  }
};
export const getUserByStaffCode = (staffCode) => {
  return axiosClient.get(USER_LIST + `/${staffCode}`);
};

export const createUser = (data) => {
  return axiosClient.post(`${API_URL_ADMIN}/users/`, data);
};

export const editUser = (staffCode, data) => {
  return axiosClient.patch(`${API_URL_ADMIN}/users/${staffCode}`, data);
};

export const disableUser = (staffCode) => {
  return axiosClient.patch(`${API_URL_ADMIN}/users/${staffCode}/disable`);
};

// @ts-nocheck
import React, { useState, createContext } from 'react';

export const AuthContext = createContext();

const AuthContextProvider = ({ children }) => {
	const isAuthenticated = localStorage.getItem('token');
	const username = localStorage.getItem('username');
	const isFirstLogin = localStorage.getItem('isFirstLogin');
	const role = localStorage.getItem('role');

	const [authContext, setAuthContext] = useState({
		isAuthenticated,
		username,
		isFirstLogin,
		role,
	});

	const AuthContextProviderData = { authContext, setAuthContext };
	return (
		<AuthContext.Provider value={AuthContextProviderData}>
			{children}
		</AuthContext.Provider>
	);
};

export default AuthContextProvider;